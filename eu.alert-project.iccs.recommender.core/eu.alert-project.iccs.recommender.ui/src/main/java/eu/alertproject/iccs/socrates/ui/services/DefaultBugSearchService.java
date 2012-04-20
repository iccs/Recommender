package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.IssueSubjectDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidClassDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidSubjectDao;
import eu.alertproject.iccs.socrates.domain.IssueSubject;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import eu.alertproject.iccs.socrates.ui.bean.Bug;
import eu.alertproject.iccs.socrates.ui.bean.IdentityBean;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.collections15.map.FastHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA. User: fotis Date: 08/02/12 Time: 6:58 PM
 */
@Service("bugSearchService")
public class DefaultBugSearchService implements BugSearchService {

    private Logger logger = LoggerFactory.getLogger(DefaultBugSearchService.class);
    private Map<Integer, Bug> data;
    private final Integer MAX_RECS_NO = 10;
    @Autowired
    IssueSubjectDao issueSubjectDao;
    @Autowired
    UuidIssueDao uuidIssueDao;

    public DefaultBugSearchService() throws IOException {


        data = new HashMap<Integer, Bug>();


    }

    @PostConstruct
    public void init() {

        logger.trace("void init()");

        if (issueSubjectDao == null) {
            logger.error("null autowired object found a");
        } else {
            logger.trace("void init() {} ", issueSubjectDao);
        }

        if (uuidIssueDao == null) {
            logger.error("null autowired object found {} ", issueSubjectDao);
        }

    }

    @Override
    public Bug retrieveBugById(Integer id) {

        logger.trace("Bug retrieveBugById()");
        //return the same bug
        data = new HashMap<Integer, Bug>();
        String bugTitle, bugDescription;
//        Bug currentBug;
        List<IssueSubject> thisIssueSubjects = issueSubjectDao.findByIssueId(id);
        bugTitle = "Bug #" + id;
        bugDescription = "";
        HashMap<String,Double> annotationsMap= new FastHashMap<String, Double>();
        for (IssueSubject is : thisIssueSubjects) {
            bugDescription = bugDescription + "'" + is.getSubject() + "' " +"<em>" +is.getWeight()+"</em>";
              annotationsMap.put(is.getSubject(), is.getWeight());
        }
//            logger.debug(bugDescription);
//            // create a Bug & add it to data            


        data.put(id, new Bug(id, bugTitle, bugDescription,annotationsMap));
//            // make its title "Bug" + bug id



        if (!data.containsKey(id)) {
            return null;
        }

        return data.get(id);
    }

    @Override
    public List<Bug> retrieveForDevId(String uuid) {



        //TODO: @Fotis We need to sort by similarity! ? 
        List<UuidIssue> uuidIssues = uuidIssueDao.findByUuid(uuid);
        List<Bug> recs = new ArrayList<Bug>();

        TreeMap<Double, Bug> recsFull = new TreeMap<Double, Bug>();
        String bugDescription="";
        for (UuidIssue ui : uuidIssues) {
            bugDescription="";
            //TODO: We need to retrieve the name and surname of the developer from STARDOM
            
            List<IssueSubject> issueSubjects = issueSubjectDao.findByIssueId(ui.getIssueId());
            HashMap<String,Double> annotationsMap= new FastHashMap<String, Double>();
           for (IssueSubject is : issueSubjects) {
               bugDescription += " " +is.getSubject() +" ";
               annotationsMap.put(is.getSubject(), is.getWeight());
           }
            recsFull.put(ui.getSimilarity(), new Bug(ui.getIssueId(), "bug #" + ui.getIssueId(), bugDescription,annotationsMap));
        }
        Set<Double> descRecsKeySet = recsFull.descendingKeySet();
        Iterator keySetIterator = descRecsKeySet.iterator();
        Integer counter = 0;
        while (keySetIterator.hasNext()) {
            recs.add(recsFull.get(keySetIterator.next()));
            counter++;
            if (counter > MAX_RECS_NO) {
                break;
            }
        }
        if (recs == null) {
            logger.debug("recs are null");
        }
        return recs;




       


    }
}
