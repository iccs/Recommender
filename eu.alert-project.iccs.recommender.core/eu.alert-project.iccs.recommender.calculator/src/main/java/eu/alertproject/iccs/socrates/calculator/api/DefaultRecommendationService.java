package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
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
    UuidNameDao uuidNameDao;

    @Autowired
    UuidSubjectDao uuidSubjectDao;

    @Autowired
    IssueSubjectDao issueSubjectDao;

    @Autowired
    SimilarityCalculator similarityCalculator;

    @Autowired
    Properties systemProperties;

    @PostConstruct
    public void init(){
        lock = new ReentrantLock();
    }

    @PreDestroy
    public void destroy(){
        lock.unlock();
    }

    @Override
    @Transactional
    public void updateSimilaritiesForIdentity(IdentityUpdated identityUpdated) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.trace("void updateSimilaritiesForIdentity() Attemping lock ");
        lock.lock();
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


            List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();

            List<UuidSubject> newUuidSubjects  = uuidSubjectDao.findByUuid(identityUpdated.getId());
            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to get UuidSubject {}",stopWatch.toSplitString());

            List<Integer> allIssues = issueSubjectDao.findAllIssues();
            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to get all Issues {}",stopWatch.toSplitString());

            
            
            
            //create annotated object 1: the identity
            HashMap<String,Double> identityAnnotations = new HashMap<String,Double>();
            for (UuidSubject us : newUuidSubjects) {
                identityAnnotations.put(us.getSubject(), us.getWeight());
            }
            AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(identityUpdated.getId(), identityAnnotations);


            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to get the identityAnnotations",stopWatch.toSplitString());


            //initialize issue annotations
            HashMap<String, Double> issueAnnotations = null;
            try {
                //iterate through all possible issues
                stopWatch.split();
                for (Integer i : allIssues) {

                    issueAnnotations = new HashMap<String, Double>();

                    List<IssueSubject> thisIssueSubjects = issueSubjectDao.findByIssueIdLimitByWeight(
                            i,
                            Double.valueOf(systemProperties.getProperty("subject.issue.weight.limit")));

                    for (IssueSubject is : thisIssueSubjects) {
                        issueAnnotations.put(is.getSubject(), is.getWeight());
                    }

                    stopWatch.split();
                    logger.trace("void updateSimilaritiesForIdentity() Took {} to get the all the Issue Subjects",stopWatch.toSplitString());


                    AnnotatedIssue annotatedIssue = new AnnotatedIssue(i.toString(), issueAnnotations);


                    stopWatch.split();
                    Double currentSimilarity =similarityCalculator.getSimilarity(
                            annotatedIdentity, annotatedIssue);
                    stopWatch.split();
                    logger.trace("void updateSimilaritiesForIdentity() Took {} to calculate similarities {}",stopWatch.toSplitString());


                    stopWatch.split();
                    UuidIssue currentUuidIssue=new UuidIssue();
                    currentUuidIssue.setUuidAndIssue(annotatedIdentity.getIdentityId(), i);
                    currentUuidIssue.setSimilarity(currentSimilarity);
                    newSimilarities.add(currentUuidIssue);
                    stopWatch.split();
                    logger.trace("void updateSimilaritiesForIdentity() Took {} to store similarities {}", stopWatch.toSplitString());

                }

            } catch (Exception ex) {
                logger.error("Couldn't update the uuid_issues",ex);
            }

            uuidIssueDao.removeByUuid(identityUpdated.getId());

            stopWatch.split();
            for(UuidIssue u: newSimilarities){
                uuidIssueDao.insert(u);
            }
            stopWatch.split();
            logger.trace("void updateSimilaritiesForIdentity() Took {} to insert uuids{}",stopWatch.toSplitString());



        } finally {
            stopWatch.stop();
            logger.trace("void updateSimilaritiesForIdentity() Took {} in total ",stopWatch.toString());
            lock.unlock();
        }

    }

    @Override
    @Transactional
    public void updateSimilaritiesForIssue(ArtefactUpdated artefactUpdated) {

        lock.lock();

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

                    logger.trace("void updateSimilaritiesForIssue() Inserting {} ",us);
                    issueSubjectDao.insert(us);

                }

            }


            List<IssueSubject> newIssueSubjects = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));
            List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();
            List<String>  uuids = uuidSubjectDao.findAllUuid();
    

            //create annotated object 1: the issue
            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();

            for (IssueSubject is : newIssueSubjects) {
                issueAnnotations.put(is.getSubject(), is.getWeight());
            }
            AnnotatedIssue annotatedIssue = new AnnotatedIssue(artefactUpdated.getId(), issueAnnotations);

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

                    Double currentSimilarity =similarityCalculator.getSimilarity(
                            annotatedIdentity, annotatedIssue);

                    UuidIssue currentUuidIssue=new UuidIssue();
                    currentUuidIssue.setUuidAndIssue(u, Integer.valueOf(artefactUpdated.getId()));
                    currentUuidIssue.setSimilarity(currentSimilarity);
                    newSimilarities.add(currentUuidIssue);

                }

            } catch (Exception ex) {
                logger.error("Couldn't update the uuid_issues",ex);
            }

            uuidIssueDao.removeByIssueId(Integer.valueOf(artefactUpdated.getId()));
            for(UuidIssue u: newSimilarities){
                uuidIssueDao.insert(u);
            }

        } finally {
            lock.unlock();
        }
    }


    @Override
    public void updateSimilaritiesForComponent(ArtefactUpdated artefactUpdated) {
        lock.lock();

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

                    logger.trace("void updateSimilaritiesForIssue() Inserting {} ",us);
                    issueSubjectDao.insert(us);

                }

            }


            List<IssueSubject> newIssueSubjects = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));
            List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();
            List<String>  uuids = uuidSubjectDao.findAllUuid();


            //create annotated object 1: the issue
            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();

            for (IssueSubject is : newIssueSubjects) {
                issueAnnotations.put(is.getSubject(), is.getWeight());
            }
            AnnotatedIssue annotatedIssue = new AnnotatedIssue(artefactUpdated.getId(), issueAnnotations);

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

                    Double currentSimilarity =similarityCalculator.getSimilarity(
                            annotatedIdentity, annotatedIssue);

                    UuidIssue currentUuidIssue=new UuidIssue();
                    currentUuidIssue.setUuidAndIssue(u, Integer.valueOf(artefactUpdated.getId()));
                    currentUuidIssue.setSimilarity(currentSimilarity);
                    newSimilarities.add(currentUuidIssue);

                }

            } catch (Exception ex) {
                logger.error("Couldn't update the uuid_issues",ex);
            }

            uuidIssueDao.removeByIssueId(Integer.valueOf(artefactUpdated.getId()));
            for(UuidIssue u: newSimilarities){
                uuidIssueDao.insert(u);
            }

        } finally {
            lock.unlock();
        }


    }
}
