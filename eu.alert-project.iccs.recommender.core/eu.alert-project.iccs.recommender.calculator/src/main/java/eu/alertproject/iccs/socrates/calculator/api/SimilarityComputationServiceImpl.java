package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedComponent;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import eu.alertproject.iccs.socrates.datastore.api.*;
import eu.alertproject.iccs.socrates.domain.*;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.TransactionManager;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/27/12
 * Time: 11:24 PM
 */
@Service("similarityComputationService")
public class SimilarityComputationServiceImpl implements SimilarityComputationService{

    private Logger logger = LoggerFactory.getLogger(SimilarityComputationServiceImpl.class);

    @Autowired
    ComponentSubjectDao componentSubjectDao;

    @Autowired
    UuidSubjectDao uuidSubjectDao;
    @Autowired
    UuidComponentDao uuidComponentDao;
    @Autowired
    UuidIssueDao uuidIssueDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    IssueSubjectDao issueSubjectDao;


    @Autowired
    Properties systemProperties;
    private ExecutorService executorService;


    @PostConstruct
    public void post(){
    }

    @Override
    public void computeAllSimilarities() {


        List<String> allUuid = getAllUuids();
        final int size = allUuid.size();
        logger.info("void computeSimilaritiesForAllIdentities([]) About to process {} ", size);

        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        executorService = Executors.newFixedThreadPool(10);
        final AtomicInteger count = new AtomicInteger(0);
        for(final String uuid : allUuid){

            /**
             * Since we are computing similarities for a different UUID there is no need
             * for this process to be serial
             */
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    /**
                     * You cannot annotate a method with @Transactinonal an call it locally on a class.
                     * A Transaction will not be initialized in this case.
                     *
                     * Becase in essence we are keeping a lot in memory by carrying out this entire
                     * operation in a single transaction we are handling transactions here manually.
                     *
                     * http://static.springsource.org/spring/docs/current/reference/transaction.html
                     *
                     */

                    transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                            long start = System.currentTimeMillis();

                            boolean success = false;
                            int tries = 2;

                            while(tries > 0){

                                try {
                                    computeSimilaritesForIdentity(uuid);
                                    success=true;

                                } catch (Exception e) {
                                }finally{
                                    if(!success){
                                        logger.debug("Deadlock here, trying again");
                                        tries--;
                                    }else{
                                        tries=0;
                                    }
                                }
                            }
                            if(!success){
                                transactionStatus.setRollbackOnly();
                            }



                            logger.debug("Computing similarities {} in {}", ((double) count.get() / (double) size),
                                    System.currentTimeMillis() - start);
                            count.incrementAndGet();
                        }
                    });

                }
            });
        }
    }

    @PreDestroy
    public void shutdown(){

        executorService.shutdownNow();

    }

    @Transactional
    private List<String> getAllUuids(){
        return uuidSubjectDao.findAllUuid();
    }

    @Override
    public void computeSimilarityForComponent(String component){
        logger.trace("void computeSimilarityForComponent([component]) {} ",component);

        List<ComponentSubject> newComponentSubjects = componentSubjectDao.findByComponent(component);

        List<UuidComponent> newSimilarities = new ArrayList<UuidComponent>();
        List<String>  uuids = uuidSubjectDao.findAllUuid();



        HashMap<String, Double> componentAnnotations = new HashMap<String, Double>();
        for (ComponentSubject is : newComponentSubjects) {
            componentAnnotations.put(is.getSubject(), is.getWeight());
        }
        AnnotatedComponent annotatedComponent = new AnnotatedComponent(component, componentAnnotations);

        //initialize identity annotations\
        HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();

        try {
            //iterate through all possible identities

            for (String u : uuids) {

                identityAnnotations.clear();
                List<UuidSubject> thisIdentitySubjects = uuidSubjectDao.findByUuidLimitByWeight(u,
                        Double.valueOf(systemProperties.getProperty("subject.uuid.weight.limit")));


                for (UuidSubject us : thisIdentitySubjects) {
                    identityAnnotations.put(us.getSubject(), us.getWeight());
                }
                AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(u, identityAnnotations);

                Double currentSimilarity =this.getSimilarity(
                        annotatedIdentity, annotatedComponent);

                UuidComponent currentUuidComponent=new UuidComponent();
                currentUuidComponent.setUuidAndComponent(u, component);
                currentUuidComponent.setSimilarity(currentSimilarity);
                newSimilarities.add(currentUuidComponent);

            }

        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_component",ex);
        }


        uuidComponentDao.removeByComponent(component);

        for(UuidComponent u: newSimilarities){
            uuidComponentDao.insert(u);
        }
    }

    @Override
    public void computeSimilaritesForIssue(Integer id) {

        logger.trace("void computeSimilaritesForIssue([id]) {}",id);

        List<IssueSubject> newIssueSubjects = issueSubjectDao.findByIssueId(id);

        //create annotated object 1: the issue
        HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();

        for (IssueSubject is : newIssueSubjects) {
            issueAnnotations.put(is.getSubject(), is.getWeight());
        }
        AnnotatedIssue annotatedIssue = new AnnotatedIssue(String.valueOf(id), issueAnnotations);

        //initialize identity annotations\
        HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();
        List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();
        List<String>  uuids = uuidSubjectDao.findAllUuid();


        try {
            //iterate through all possible identities

            for (String u : uuids) {

                identityAnnotations.clear();
                List<UuidSubject> thisIdentitySubjects = uuidSubjectDao.findByUuidLimitByWeight(u,
                        Double.valueOf(systemProperties.getProperty("subject.uuid.weight.limit")));


                for (UuidSubject us : thisIdentitySubjects) {
                    identityAnnotations.put(us.getSubject(), us.getWeight());
                }
                AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(u, identityAnnotations);

                Double currentSimilarity =this.getSimilarity(annotatedIdentity, annotatedIssue);

                UuidIssue currentUuidIssue=new UuidIssue();
                currentUuidIssue.setUuidAndIssue(u, id);
                currentUuidIssue.setSimilarity(currentSimilarity);
                newSimilarities.add(currentUuidIssue);

            }

        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_issues",ex);
        }

        uuidIssueDao.removeByIssueId(Integer.valueOf(id));
        for(UuidIssue u: newSimilarities){
            uuidIssueDao.insert(u);
        }

    }




    @Override
    @Transactional
    public void computeSimilaritesForIdentity(String uuid){

        logger.trace("void computeSimilaritesForIdentity([uuid]) In transaction ? {}",org.springframework.transaction.support.
                TransactionSynchronizationManager.isActualTransactionActive());

        StopWatch sw = new StopWatch();
        sw.start();
        List<UuidSubject> newUuidSubjects  = uuidSubjectDao.findByUuid(uuid);

        sw.split();
        logger.trace("void computeSimilaritesForIdentity([uuid]) Took {} to findByUuid",sw.toString());


        //create annotated object 1: the identity
        HashMap<String,Double> identityAnnotations = new HashMap<String,Double>();
        for (UuidSubject us : newUuidSubjects) {
            identityAnnotations.put(us.getSubject(), us.getWeight());
        }
        AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(uuid, identityAnnotations);
        sw.split();
        logger.trace("Sim ({}) Took {} to create annotated identities",uuid,sw.toString());




        List<Integer> allIssues = issueSubjectDao.findAllIssues();
        sw.split();
        logger.trace("Sim ({}) Took {} to find all ({}) issues",
                new Object[]{
                        uuid,
                        sw.toString(),
                        allIssues.size()});

        List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();

        //initialize issue annotations
        HashMap<String, Double> issueAnnotations = null;
        try {
            //iterate through all possible issues
            for (Integer i : allIssues) {

                issueAnnotations = new HashMap<String, Double>();

                sw.split();
                Double weight = Double.valueOf(systemProperties.getProperty("subject.issue.weight.limit"));
                List<IssueSubject> thisIssueSubjects = issueSubjectDao.findByIssueIdLimitByWeight(
                        i,
                        weight);
                sw.split();
                logger.trace("Sim({}) Took {} to issueSubjectDao.findByIssueIdLimitByWeight({},{}) = {}",
                        new Object[]{
                        uuid,
                        sw.toString(),
                        i,
                        weight,
                        thisIssueSubjects.size()});


                for (IssueSubject is : thisIssueSubjects) {
                    issueAnnotations.put(is.getSubject(), is.getWeight());
                }
                AnnotatedIssue annotatedIssue = new AnnotatedIssue(i.toString(), issueAnnotations);


                sw.split();
                Double currentSimilarity =this.getSimilarity(annotatedIdentity, annotatedIssue);
                logger.trace("Sim ({}) {} to getSimilarity ",uuid,sw.toSplitString());

                UuidIssue currentUuidIssue=new UuidIssue();
                currentUuidIssue.setUuidAndIssue(annotatedIdentity.getIdentityId(), i);
                currentUuidIssue.setSimilarity(currentSimilarity);
                newSimilarities.add(currentUuidIssue);
            }



        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_issues",ex);
        }

        sw.split();

        /**
         * A mysql deadlock occurs when 2 or more thread attempt to delete the same
         */

        uuidIssueDao.removeByUuid(uuid);
        for(UuidIssue u: newSimilarities){
            uuidIssueDao.insert(u);
        }

        sw.split();
        logger.trace("void computeSimilaritesForIdentity({}) Took {} reset uuidIssues",uuid,sw.toString());

        /**
         * Update the similarity to the components
         *
         */
        //initialize issue annotations
        HashMap<String, Double> componentAnnotations = null;

        logger.trace("void computeSimilaritesForIdentity([uuid]) Fetching all components ");

        Double weight = Double.valueOf(systemProperties.getProperty("subject.component.weight.limit"));

//        List<String> allComponents= componentSubjectDao.findAllComponents();
        List<UuidComponent> newComponentSimilarities = new ArrayList<UuidComponent>();


        try {
            /**
             * This list is returned with component in alphabetical order
             *
             * The reason for the difference in logic from that of the issues is that there
             * is no logic in looping over all the components in order to check IF  it has
             * subjects with a weight > system.property("subject.component.weight.limit").
             *
             * The way it used to be meant that whatever happens we have to do throught the
             * 26,000 components in the case of KDE and carry out client side that filter.
             *
             * Using a look ahead iterator we are able to push the "filtering" by
             * weight to the database, and we only look once throught the "necessary" data.
             *
             */
            List<ComponentSubject> allByWeight = componentSubjectDao.findAllByWeight(weight);
            logger.trace("void computeSimilaritesForIdentity([uuid]) Retreived {} components", allByWeight.size());


            Iterator<ComponentSubject> iterator = allByWeight.iterator();

            ComponentSubject next =null;
            String component ="";
            while (true || iterator.hasNext()){

                componentAnnotations = new HashMap<String, Double>();

                if(next == null){
                    next = iterator.next();
                    component=next.getComponent();
                    logger.trace("void computeSimilaritesForIdentity([uuid]) First component in the queue {}",component);
                }else{
                    logger.trace("void computeSimilaritesForIdentity([uuid]) Working with {}",component);
                }

                while(true || iterator.hasNext()){

                    logger.trace("\tAdding {} ",next.getSubject());
                    componentAnnotations.put(next.getSubject(), next.getWeight());


                    ComponentSubject lookAhead = iterator.next();
                    if(!lookAhead.getComponent().equals(component)){
                        logger.trace("\t Found another component moving ahead {}",lookAhead.getComponent());
                        next=lookAhead;
                        component = lookAhead.getComponent();
                        break;
                    }

                }

                AnnotatedComponent annotatedComponent = new AnnotatedComponent(component, componentAnnotations);
                Double currentSimilarity =this.getSimilarity(annotatedIdentity, annotatedComponent);
                UuidComponent currentUuidComponent =new UuidComponent();
                currentUuidComponent.setUuidAndComponent(annotatedIdentity.getIdentityId(), component);
                currentUuidComponent.setSimilarity(currentSimilarity);
                newComponentSimilarities.add(currentUuidComponent);


            }

        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_issues",ex);
        }

        /**
         * A mysql lock exception occurs if we do not lock here between all threads
         */
        uuidComponentDao.removeByUuid(uuid);
        for(UuidComponent u: newComponentSimilarities){
            uuidComponentDao.insert(u);
        }

        logger.trace("Sim({}) ****** Finished *******",uuid);
        sw.stop();

    }

    public final Double getSimilarity(AnnotatedObject item1, AnnotatedObject item2) {
        if ((item1==null)||(item2==null)){
            throw new IllegalArgumentException("Items have not been initialized. Initialize the items and try again");
        }

        //create a set with the words they have in common
        Set<String> commonWordsSet = new HashSet<String>(item1.getAnnotations().keySet());
        commonWordsSet.retainAll(item2.getAnnotations().keySet());

//                Iterator item1Itr =item1.getAnnotations().keySet().iterator();
//        System.out.println("========Item 1 Words==============");
//        while (item1Itr.hasNext()) {
//            System.out.print(item1Itr.next() +" ");
//        }
//
//                        Iterator item2Itr =item2.getAnnotations().keySet().iterator();
//        System.out.println("========Item 2 Words==============");
//        while (item2Itr.hasNext()) {
//            System.out.print(item2Itr.next() +" ");
//        }
//
//
//
//        Iterator commonWordSetItr = commonWordsSet.iterator();
//        System.out.println("========Common Words==============");
//        while (commonWordSetItr.hasNext()) {
//            System.out.print(commonWordSetItr.next() +" ");
//        }


        //Calculates the cosine similarity. For the numerator we need only the common words, for the denominator the norms of the words found in each vector.
        double numerator = 0, norm1 = 0, norm2 = 0;

        /**
         * There is not point calculating the similarity if the
         * common wordset is empty as well as if any annotated
         * object has no annotations
         *
         */
        if(     commonWordsSet.size() <=0
           ||   item1.getAnnotations().keySet().size() <=0
           ||   item2.getAnnotations().keySet().size() <=0
                ){
            return 0.0;
        }

        for (String k : commonWordsSet) {
            numerator += item1.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }



        for (String k : item1.getAnnotations().keySet()) {
            norm1 += item1.getAnnotations().get(k) * item1.getAnnotations().get(k);
        }
        for (String k : item2.getAnnotations().keySet()) {
            norm2 += item2.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }


        //this could return NaN if either norm1 or norm2 are 0
        if(norm1 <=0.0 || norm2 <=0.0){
            return 0.0;
        }


        Double similarity = numerator / Math.sqrt(norm1 * norm2);
//        logger.trace("Double getSimilarity() {}/Math.sqrt({}*{})={}",
//            new Object[]{
//                numerator,
//                norm1,
//                norm2,
//                similarity});

        return similarity;

    }

    public final Double getIntegerSimilarity(HashMap<Integer, Integer> itemOne, HashMap<Integer, Integer> itemTwo) {
        if ((itemOne == null) || (itemTwo == null)) {
            throw new IllegalArgumentException("Items have not been initialized. Initialize the items and try again");
        }
        Set<Integer> commonWordsSet = new HashSet<Integer>(itemOne.keySet());
        commonWordsSet.retainAll(itemTwo.keySet());


        //Calculates the cosine similarity. For the numerator we need only the common words, for the denominator the norms of the words found in each vector.
        double numerator = 0, norm1 = 0, norm2 = 0;



        if (commonWordsSet.size() <= 0
                || itemOne.keySet().size() <= 0
                || itemTwo.keySet().size() <= 0) {
            return 0.0;
        }

        for (Integer k : commonWordsSet) {
            numerator += itemOne.get(k) * itemTwo.get(k);
        }



        for (Integer k : itemOne.keySet()) {
            norm1 += itemOne.get(k) * itemOne.get(k);
        }
        for (Integer k : itemTwo.keySet()) {
            norm2 += itemTwo.get(k) * itemTwo.get(k);
        }


        //this could return NaN if either norm1 or norm2 are 0
        if (norm1 <= 0.0 || norm2 <= 0.0) {
            return 0.0;
        }


        Double similarity = numerator / Math.sqrt(norm1 * norm2);
//        logger.trace("Double getSimilarity() {}/Math.sqrt({}*{})={}",
//                new Object[]{
//                    numerator,
//                    norm1,
//                    norm2,
//                    similarity});

        return similarity;

    }


}
