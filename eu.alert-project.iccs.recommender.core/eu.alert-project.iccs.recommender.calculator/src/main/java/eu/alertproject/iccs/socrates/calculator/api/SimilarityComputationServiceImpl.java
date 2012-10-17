package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedComponent;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import eu.alertproject.iccs.socrates.datastore.api.*;
import eu.alertproject.iccs.socrates.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    IssueSubjectDao issueSubjectDao;


    @Autowired
    Properties systemProperties;


//    @Override
//    @Transactional
//    public void computeSimilaritiesForAllComponents(){
//
//        logger.info("void computeSimilaritiesForAllComponents([]) ");
//        List<String> allComponents = componentSubjectDao.findAllComponents();
//
//        for(String component: allComponents){
//            computeSimilarityForComponent(component);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void computeSimilaritiesForAllIssues() {
//
//        logger.info("void computeSimilaritiesForAllIssues([]) ");
//
//        List<Integer> allIssues = issueSubjectDao.findAllIssues();
//
//        for(Integer issue: allIssues){
//            computeSimilaritesForIssue(issue);
//        }
//
//    }

    @Override
    @Transactional
    public void computeAllSimilarities() {

        logger.info("void computeSimilaritiesForAllIdentities([]) ");
        List<String> allUuid = uuidSubjectDao.findAllUuid();

        for(String uuid : allUuid){
            computeSimilaritesForIdentity(uuid);
        }

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
    public void computeSimilaritesForIdentity(String uuid){

        logger.trace("void computeSimilaritesForIdentity([uuid]) {}",uuid);

        List<UuidSubject> newUuidSubjects  = uuidSubjectDao.findByUuid(uuid);


        //create annotated object 1: the identity
        HashMap<String,Double> identityAnnotations = new HashMap<String,Double>();
        for (UuidSubject us : newUuidSubjects) {
            identityAnnotations.put(us.getSubject(), us.getWeight());
        }
        AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(uuid, identityAnnotations);




        List<Integer> allIssues = issueSubjectDao.findAllIssues();
        List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();

        //initialize issue annotations
        HashMap<String, Double> issueAnnotations = null;
        try {
            //iterate through all possible issues
            for (Integer i : allIssues) {

                issueAnnotations = new HashMap<String, Double>();

                List<IssueSubject> thisIssueSubjects = issueSubjectDao.findByIssueIdLimitByWeight(
                        i,
                        Double.valueOf(systemProperties.getProperty("subject.issue.weight.limit")));

                for (IssueSubject is : thisIssueSubjects) {
                    issueAnnotations.put(is.getSubject(), is.getWeight());
                }
                AnnotatedIssue annotatedIssue = new AnnotatedIssue(i.toString(), issueAnnotations);

                Double currentSimilarity =this.getSimilarity(annotatedIdentity, annotatedIssue);

                UuidIssue currentUuidIssue=new UuidIssue();
                currentUuidIssue.setUuidAndIssue(annotatedIdentity.getIdentityId(), i);
                currentUuidIssue.setSimilarity(currentSimilarity);
                newSimilarities.add(currentUuidIssue);
            }



        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_issues",ex);
        }

        uuidIssueDao.removeByUuid(uuid);
        for(UuidIssue u: newSimilarities){
            uuidIssueDao.insert(u);
        }



        /**
         * Update the similarity to the components
         *
         */
        //initialize issue annotations
        HashMap<String, Double> componentAnnotations = null;
        List<String> allComponents= componentSubjectDao.findAllComponents();
        List<UuidComponent> newComponentSimilarities = new ArrayList<UuidComponent>();


        try {
            //iterate through all possible components
            for (String c : allComponents) {

                componentAnnotations = new HashMap<String, Double>();

                List<ComponentSubject> thisComponentSubjects = componentSubjectDao.findByComponentLimitByWeight(
                        c,
                        Double.valueOf(systemProperties.getProperty("subject.component.weight.limit")));

                for (ComponentSubject cs : thisComponentSubjects) {
                    componentAnnotations.put(cs.getSubject(), cs.getWeight());
                }

                AnnotatedComponent annotatedComponent = new AnnotatedComponent(c, componentAnnotations);

                Double currentSimilarity =this.getSimilarity(annotatedIdentity, annotatedComponent);

                UuidComponent currentUuidComponent =new UuidComponent();
                currentUuidComponent.setUuidAndComponent(annotatedIdentity.getIdentityId(), c);
                currentUuidComponent.setSimilarity(currentSimilarity);
                newComponentSimilarities.add(currentUuidComponent);
            }



        } catch (Exception ex) {
            logger.error("Couldn't update the uuid_issues",ex);
        }

        uuidComponentDao.removeByUuid(uuid);
        for(UuidComponent u: newComponentSimilarities){
            uuidComponentDao.insert(u);
        }

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
        logger.trace("Double getSimilarity() {}/Math.sqrt({}*{})={}",
            new Object[]{
                numerator,
                norm1,
                norm2,
                similarity});

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
