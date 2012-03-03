package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
import eu.alertproject.iccs.socrates.calculator.internal.text.AnnotatedObjectSimilarity;
import eu.alertproject.iccs.socrates.datastore.api.*;
import eu.alertproject.iccs.socrates.domain.*;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
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
    UuidSubjectDao uuidSubjectDao;

    @Autowired
    IssueSubjectDao issueSubjectDao;


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
        lock.lock();

        try {

            List<IdentityUpdated.CI> cis = identityUpdated.getCis();
            uuidClassDao.removeByUuid(identityUpdated.getId());

            for(IdentityUpdated.CI ci : cis){

                UuidClass uuidClass = new UuidClass();
                uuidClass.setUuidAndClass(ci.getClazz(), identityUpdated.getId());
                uuidClass.setWeight(ci.getWeight());

                uuidClassDao.insert(uuidClass);
            }




            List<AnnotationPair> annotations = identityUpdated.getAnnotations();


            List<UuidSubject> uuidSubjects  = uuidSubjectDao.findByUuid(identityUpdated.getId());
            for(AnnotationPair ap: annotations){

                UuidSubject us = null;

                //check if one exists
                Iterator<UuidSubject> iterator = uuidSubjects.iterator();
                while(iterator.hasNext()){
                    UuidSubject next = iterator.next();
                    
                    if(StringUtils.equalsIgnoreCase(ap.getSubject(),next.getSubject())){
                        //update previous
                        next.setWeight(next.getWeight()+ap.getCount());
                        us  = uuidSubjectDao.update(next);
                        iterator.remove();
                    }
                    
                }
                
                
                if(us == null ){
                    //create new
                    us = new UuidSubject();
                    us.setWeight(ap.getCount());
                    us.setUuidAndSubject(identityUpdated.getId(), ap.getSubject());

                    logger.trace("void updateSimilaritiesForIdentity() Inserting {} ",us);
                    us = uuidSubjectDao.insert(us);
                }

            }



            List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();
            List<UuidSubject> newUuidSubjects  = uuidSubjectDao.findByUuid(identityUpdated.getId());
            List<Integer> allIssues = issueSubjectDao.findAllIssues();
            
            
            
            
            //create annotated object 1: the identity
            
            HashMap<String,Double> identityAnnotations = new HashMap<String,Double>();
            for (UuidSubject us : newUuidSubjects) {
                identityAnnotations.put(us.getSubject(), us.getWeight());
            }
            AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(identityUpdated.getId(), identityAnnotations);


            //initialize issue annotations
            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();
            try {
                //iterate through all possible issues
                for (Integer i : allIssues) {
                    issueAnnotations.clear();
                    List<IssueSubject> thisIssueSubjects = issueSubjectDao.findByIssueId(i);
                    for (IssueSubject is : thisIssueSubjects) {
                        issueAnnotations.put(is.getSubject(), is.getWeight());
                    }
                    AnnotatedIssue annotatedIssue = new AnnotatedIssue(i.toString(), issueAnnotations);

                    Double currentSimilarity = new AnnotatedObjectSimilarity(annotatedIdentity, annotatedIssue).calculateSimilarity();
                    UuidIssue currentUuidIssue=new UuidIssue();
                    currentUuidIssue.setUuidAndIssue(annotatedIdentity.getIdentityId(), i);
                    currentUuidIssue.setSimilarity(currentSimilarity);
                    newSimilarities.add(currentUuidIssue);
                }

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DefaultRecommendationService.class.getName()).log(Level.SEVERE, null, ex);
            }

            // return a List<UuidIssue>
            


            uuidIssueDao.removeByUuid(identityUpdated.getId());
            for(UuidIssue u: newSimilarities){
                uuidIssueDao.insert(u);
            }


        } finally {
            lock.unlock();
        }



    }



    @Override
    @Transactional
    public void updateSimilaritiesForIssue(ArtefactUpdated artefactUpdated) {
        lock.lock();

        try {

            List<AnnotationPair> annotations = artefactUpdated.getAnnotations();

            List<IssueSubject> issueSubjects  = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));
            
            for(AnnotationPair ap: annotations){

                IssueSubject us = null;

                //check if one exists
                Iterator<IssueSubject> iterator = issueSubjects.iterator();
                while(iterator.hasNext()){
                    IssueSubject next = iterator.next();

                    if(StringUtils.equalsIgnoreCase(ap.getSubject(),next.getSubject())){
                        //update previous
                        next.setWeight(next.getWeight()+ap.getCount());
                        us  = issueSubjectDao.update(next);
                        iterator.remove();
                    }
                    

                }
                
                if(us == null ){
                    //create new
                    us = new IssueSubject();
                    us.setWeight(ap.getCount());
                    us.setIssueAndSubject(Integer.valueOf(artefactUpdated.getId()), ap.getSubject());

                    logger.trace("void updateSimilaritiesForIssue() Inserting {} ",us);
                    issueSubjectDao.insert(us);
                }

            }


            List<IssueSubject> newIssueSubjects = issueSubjectDao.findByIssueId(Integer.valueOf(artefactUpdated.getId()));
            List<UuidIssue> newSimilarities = new ArrayList<UuidIssue>();
            List<String>  uuids = uuidSubjectDao.findAllUuid();
    
            //TODO Kostas do work here
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
                    List<UuidSubject> thisIdentitySubjects = uuidSubjectDao.findByUuid(u);

                    for (UuidSubject us : thisIdentitySubjects) {
                        identityAnnotations.put(us.getSubject(), us.getWeight());
                    }
                    AnnotatedIdentity annotatedIdentity = new AnnotatedIdentity(u, identityAnnotations);

                    Double currentSimilarity = new AnnotatedObjectSimilarity(annotatedIdentity, annotatedIssue).calculateSimilarity();
                    UuidIssue currentUuidIssue=new UuidIssue();
                    currentUuidIssue.setUuidAndIssue(u, Integer.valueOf(artefactUpdated.getId()));
                    currentUuidIssue.setSimilarity(currentSimilarity);
                    newSimilarities.add(currentUuidIssue);
                }

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(DefaultRecommendationService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            // return a List<UuidIssue>


            uuidIssueDao.removeByIssueId(Integer.valueOf(artefactUpdated.getId()));
            for(UuidIssue u: newSimilarities){
                uuidIssueDao.insert(u);
            }


        } finally {
            lock.unlock();
        }
    }
}
