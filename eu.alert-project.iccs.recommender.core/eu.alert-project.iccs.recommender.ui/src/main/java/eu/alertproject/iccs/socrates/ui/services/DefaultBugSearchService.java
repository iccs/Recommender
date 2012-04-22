package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.IssueSubjectDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.Bug;
import eu.alertproject.iccs.socrates.domain.IssueSubject;
import eu.alertproject.iccs.socrates.domain.UuidIssue;

import java.io.IOException;
import java.util.*;
import javax.annotation.PostConstruct;

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

    @Autowired
    Properties systemProperties;


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


        Map<String, Double> stringDoubleMap = this.<String, Double>sortHashMapByValuesD(annotationsMap);
        data.put(id, new Bug(id, bugTitle, bugDescription,stringDoubleMap));
//            // make its title "Bug" + bug id

        if (!data.containsKey(id)) {
            return null;
        }

        return data.get(id);
    }


    
    //TODO move this to Utils
    private <T extends Comparable,E extends Comparable> Map<T,E> sortHashMapByValuesD(HashMap<T,E> passedMap) {

        List<T> mapKeys = new ArrayList<T>(passedMap.keySet());
        Collections.<T>sort(mapKeys);
        Collections.reverse(mapKeys);

        List<E> mapValues = new ArrayList<E>(passedMap.values());
        Collections.<E>sort(mapValues);
        Collections.reverse(mapValues);

        Map<T,E> sortedMap = new LinkedHashMap<T,E>();

        Iterator<E> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            E val = valueIt.next();
            Iterator<T> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                T key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}
