package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.internal.ComponentUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.events.internal.IssueUpdated;
import eu.alertproject.iccs.socrates.datastore.api.*;
import eu.alertproject.iccs.socrates.domain.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:40
 */
@Service("recommendationService")
public class DefaultRecommendationService implements RecommendationService{
    private Logger logger = LoggerFactory.getLogger(DefaultRecommendationService.class);

    private ReentrantLock lock;

    @Autowired
    UuidClassDao uuidClassDao;

    @Autowired
    UuidIssueDao uuidIssueDao;

    @Autowired
    IssueMetaDao issueMetaDao;


    @Autowired
    UuidNameDao uuidNameDao;

    @Autowired
    UuidSubjectDao uuidSubjectDao;

    @Autowired
    IssueSubjectDao issueSubjectDao;

    @Autowired
    ComponentSubjectDao componentSubjectDao;

    @Autowired
    UuidComponentDao uuidComponentDao;


    @Autowired
    SimilarityComputationService similarityComputationService;

    @Autowired
    Properties systemProperties;

    private boolean realtimeEnabled;
    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void init(){
        lock = new ReentrantLock();
        realtimeEnabled = systemProperties.getProperty("similarity.realtime").toLowerCase().equals("true");
        final long ms = Long.valueOf(systemProperties.getProperty("similarity.timer"));
        logger.trace("void init([]) Hello World");
        if(systemProperties.getProperty("similarity.calculateOnBoot").equals("true")){

            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    getLock("Updating Similarities");
                    similarityComputationService.computeAllSimilarities();
                    releaseLock("similarities updated");
                }
            },30,TimeUnit.SECONDS);
        }

        if(!systemProperties.getProperty("similarity.realtime").equals("true")){
            //this means we are going for scheduled tasks
            if(ms > 0 ){

                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleWithFixedDelay(
                        new Runnable(){
                            public void run(){

                                try{
                                    getLock("timer");

                                    long start = System.currentTimeMillis();

                                    logger.info("Updating similarities {} ",new Date());
                                    similarityComputationService.computeAllSimilarities();

                                    logger.info("Last run took {} minutes ",
                                            ((double)System.currentTimeMillis()-start)/1000.0/60.0);
                                }catch (Exception e){
                                    logger.warn("An unexpected erro was encountered {}",e);
                                }finally {
                                    releaseLock("timer");
                                }


                            }
                        },ms,ms,TimeUnit.SECONDS);


            }


        }

    }

    @PreDestroy
    public void destroy(){

        if(scheduler !=null){
            scheduler.shutdown();
        }
        //get the lock first before
        releaseLock("destroy");

    }

    @Override
    @Transactional
    public void updateSimilaritiesForIdentity(IdentityUpdated identityUpdated) {


        getLock("updateSimilaritiesForIdentity");

        try {

//
//            //store the new name
//            UuidName byUuid = uuidNameDao.findByUuid(identityUpdated.getId());
//            if(byUuid == null){
//                UuidName un = new UuidName();
//                un.setUuidAndName(identityUpdated.getId(), identityUpdated.getName());
//                uuidNameDao.insert(un);
//            }else{
//                byUuid.setUuidAndName(byUuid.getUuid(),byUuid.getName());
//                uuidNameDao.update(byUuid);
//            }


            Map<String,Double> cis = identityUpdated.getCis();
            uuidClassDao.removeByUuid(identityUpdated.getId());


            for (String key : cis.keySet()) {
                UuidClass uuidClass = new UuidClass();
                uuidClass.setUuidAndClass(identityUpdated.getId(),key);
                uuidClass.setWeight(cis.get(key));

                uuidClassDao.insert(uuidClass);
            }

            List<Keui.Concept> concepts = filterConcepts(identityUpdated.getConcepts());
            List<UuidSubject> uuidSubjects  = uuidSubjectDao.findByUuid(identityUpdated.getId());

            List<String> processed = new ArrayList<String>();

            if(concepts != null){

                for(Keui.Concept ap: concepts){


                    UuidSubject us = null;

                    //check if one exists
                    Iterator<UuidSubject> iterator = uuidSubjects.iterator();
                    while(iterator.hasNext()){
                        UuidSubject next = iterator.next();

                        if(StringUtils.equalsIgnoreCase(ap.getUri(),next.getSubject())){
                            //update previous
                            next.setWeight(next.getWeight()+ap.getWeight());
                            logger.trace("void updateSimilaritiesForIdentity([identityUpdated]) Updating {}", next);
                            us  = uuidSubjectDao.update(next);
                            logger.trace("void updateSimilaritiesForIdentity([identityUpdated]) Updating Result {}",us);
                            iterator.remove();
                        }

                    }

                    if(us == null && !processed.contains(ap.getUri())){
                        //create new
                        us = new UuidSubject();
                        us.setWeight(Double.valueOf(ap.getWeight()));
                        us.setUuidAndSubject(identityUpdated.getId(), ap.getUri());

                        logger.trace("void updateSimilaritiesForIdentity() Inserting {} from {} ",us,ap);

                        uuidSubjectDao.insert(us);

                        processed.add(ap.getUri());

                    }

                }
            }

            if(realtimeEnabled){
                similarityComputationService.computeSimilaritesForIdentity(identityUpdated.getId());
            }

        } finally {
            releaseLock("updateSimilaritiesForIdentity");
        }

    }

    @Override
    @Transactional
    public void updateSimilaritiesForIssue(IssueUpdated artefactUpdated) {


        getLock("updateSimilaritiesForIssue");
        try {

            List<Keui.Concept> annotations = filterConcepts(artefactUpdated.getConcepts());

            List<IssueSubject> issueSubjects  = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));

            /**
             * This is only possible for update issue events where a comment hasn't been added!
             */
            if(annotations == null ){
                logger.warn("Issue {} has null annotations skipping issue update ",artefactUpdated.getId());
                return;
            }
            for(Keui.Concept ap: annotations){

                IssueSubject us = null;

                //check if one exists
                Iterator<IssueSubject> iterator = issueSubjects.iterator();
                while(iterator.hasNext()){
                    IssueSubject next = iterator.next();

                    if(StringUtils.equalsIgnoreCase(ap.getUri(),next.getSubject())){
                        //update previous
                        next.setWeight(next.getWeight()+ap.getWeight());
                        us  = issueSubjectDao.update(next);
                        iterator.remove();
                    }
                    

                }
                
                if(us == null ){

                    //create new
                    us = new IssueSubject();
                    us.setWeight(Double.valueOf(ap.getWeight()));
                    us.setIssueAndSubject(Integer.valueOf(artefactUpdated.getId()), ap.getUri());


                    IssueMeta byId = issueMetaDao.findById(Integer.valueOf(artefactUpdated.getId()));
                    if(byId == null){
                        byId= new IssueMeta();
                        byId.setId(Integer.valueOf(artefactUpdated.getId()));
                        byId = issueMetaDao.insert(byId);

                    }
                    byId.setDate(artefactUpdated.getDate());
                    byId.setSubject(artefactUpdated.getSubject());
                    issueMetaDao.update(byId);


                    logger.trace("void updateSimilaritiesForIssue() Inserting {} ",us);
                    issueSubjectDao.insert(us);

                }

            }


            if(realtimeEnabled){
                similarityComputationService.computeSimilaritesForIssue(
                        Integer.valueOf(artefactUpdated.getId()));
            }

        } finally {
            releaseLock("updateSimilaritiesForIssue");
        }
    }


    @Override
    @Transactional
    public void updateSimilaritiesForComponent(ComponentUpdated artefactUpdated) {

        getLock("updateSimilaritiesForComponent");


        try {

            List<Keui.Concept> annotations = filterConcepts(artefactUpdated.getConcepts());


            List<ComponentSubject> byComponent = componentSubjectDao.findByComponent(artefactUpdated.getComponent());

            /**
             * This is only possible for update issue events where a comment hasn't been added!
             */
            if(annotations == null ){
                logger.warn("Issue {} has null annotations skipping component similarity",artefactUpdated.getId());
                return;
            }

            for(Keui.Concept ap: annotations){

                ComponentSubject cs = null;

                //check if one exists
                Iterator<ComponentSubject> iterator = byComponent.iterator();
                while(iterator.hasNext()){

                    ComponentSubject next = iterator.next();

                    if(StringUtils.equalsIgnoreCase(ap.getUri(),next.getSubject())){
                        //update previous
                        next.setWeight(next.getWeight()+ap.getWeight());
                        cs = componentSubjectDao.update(next);
                        iterator.remove();
                    }
                }



                if(cs == null ){

                    //create new
                    cs = new ComponentSubject();
                    cs.setWeight(Double.valueOf(ap.getWeight()));
                    cs.setComponenAndSubject(artefactUpdated.getComponent(), ap.getUri());
                    logger.trace("void updateSimilaritiesForComponent() Inserting {} ",cs);
                    componentSubjectDao.insert(cs);

                }
            }

            if(realtimeEnabled){
                //do
                similarityComputationService.computeSimilarityForComponent(artefactUpdated.getComponent());
            }


        } finally {
            releaseLock("updateSimilaritiesForComponent");

        }
    }



    private void getLock(String method){
        logger.trace("void getLock([]) Attempting to lock {}",method);
        lock.lock();
        logger.trace("void getLock([method]) lock aquired {} ",method);
    }

    private void releaseLock(String method){
        logger.trace("void releaseLock([]) {}",method);
        lock.unlock();
    }


    /**
     * The following method is necessary since we may have multiple concepts with the
     * same uri arriving in the list which need to be combined
     */
    private List<Keui.Concept> filterConcepts(List<Keui.Concept> incoming){

        List<Keui.Concept> results = new ArrayList<Keui.Concept>();

            if(incoming == null){
            return results;
        }
        for(Keui.Concept c: incoming){
            if(!results.contains(c)){
                results.add(c);
            }else{
                int index = results.indexOf(c);
                Keui.Concept concept = results.get(index);
                concept.setWeight(concept.getWeight()+c.getWeight());
                results.set(index,concept);
            }
        }

        return results;

    }
}
