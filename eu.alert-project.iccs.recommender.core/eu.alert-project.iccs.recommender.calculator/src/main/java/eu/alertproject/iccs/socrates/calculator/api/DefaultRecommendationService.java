package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.internal.ComponentUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.events.internal.IssueUpdated;
import eu.alertproject.iccs.socrates.datastore.api.*;
import eu.alertproject.iccs.socrates.domain.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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
        if(!systemProperties.getProperty("similarity.realtime").equals("true")){
            //this means we are going for scheduled tasks
            if(ms > 0 ){

                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleWithFixedDelay(
                        new Runnable(){
                            public void run(){

                                try{

//                    while(runTimer.get()){
//
//                        Thread.sleep(ms);
                                    getLock("timer");

                                    long start = System.currentTimeMillis();

                                    logger.info("Updating similarities {} ",new Date());
                                    similarityComputationService.computeAllSimilarities();

                                    logger.info("Last run took {} minutes ",
                                            ((double)System.currentTimeMillis()-start)/1000.0/60.0);
//
//                    logger.info("void run([]) Timer stopped");


//                }catch (InterruptedException e){
//                    logger.warn("This thread was interrupted {}",e);
                                }catch (Exception e){
                                    logger.warn("An unexpected erro was encountered {}",e);
                                }finally {
                                    releaseLock("timer");
                                }


                            }
                        },ms,ms,TimeUnit.SECONDS);

//                new Thread(timer).start();
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        getLock("updateSimilaritiesForIdentity");
        stopWatch.split();
        logger.trace("void updateSimilaritiesForIdentity() Attemping lock took {}",stopWatch.toSplitString());

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



            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to insert uuid classes",stopWatch.toSplitString());

            List<Keui.Concept> concepts = identityUpdated.getConcepts();
            List<UuidSubject> uuidSubjects  = uuidSubjectDao.findByUuid(identityUpdated.getId());

            List<Keui.Concept> processed = new ArrayList<Keui.Concept>();

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
                            us  = uuidSubjectDao.update(next);
                            iterator.remove();
                        }

                    }

                    stopWatch.split();
                    logger.trace("void updateSimilaritiesForIdentity() Took {} to update subjects ",stopWatch.toSplitString());



                    if(us == null && ! processed.contains(ap)){
                        //create new
                        us = new UuidSubject();
                        us.setWeight(Double.valueOf(ap.getWeight()));
                        us.setUuidAndSubject(identityUpdated.getId(), ap.getUri());

                        logger.trace("void updateSimilaritiesForIdentity() Inserting {} ",us);

                        us = uuidSubjectDao.insert(us);

                        processed.add(ap);

                    }

                }
            }

            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to handle concepts{}",stopWatch.toSplitString());


            if(realtimeEnabled){
                similarityComputationService.computeSimilaritesForIdentity(identityUpdated.getId());
            }

        } finally {
            stopWatch.stop();
            logger.trace("void updateSimilaritiesForIdentity() Took {} in total ",stopWatch.toString());
            releaseLock("updateSimilaritiesForIdentity");
        }

    }

    @Override
    @Transactional
    public void updateSimilaritiesForIssue(IssueUpdated artefactUpdated) {


        getLock("updateSimilaritiesForIssue");
        try {

            List<Keui.Concept> annotations = artefactUpdated.getConcepts();

            List<IssueSubject> issueSubjects  = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));
            
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


                    IssueMeta is = new IssueMeta();
                    is.setDate(artefactUpdated.getDate());
                    is.setSubject(artefactUpdated.getSubject());
                    is.setId(Integer.valueOf(artefactUpdated.getId()));


                    issueMetaDao.insert(is);

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

            List<Keui.Concept> annotations = artefactUpdated.getConcepts();


            List<ComponentSubject> byComponent = componentSubjectDao.findByComponent(artefactUpdated.getComponent());
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

}
