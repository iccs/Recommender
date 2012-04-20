package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidSubjectDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import eu.alertproject.iccs.socrates.domain.UuidSubject;
import eu.alertproject.iccs.socrates.ui.bean.Bug;
import eu.alertproject.iccs.socrates.ui.bean.IdentityBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.util.logging.resources.logging;

/**
 * Created by IntelliJ IDEA. User: fotis Date: 08/02/12 Time: 11:56 PM To change
 * this template use File | Settings | File Templates.
 */
@Service("identitySearchService")
public class DefaultIdentitySearchService implements IdentitySearchService {

    @Autowired
    UuidIssueDao uuidIssueDao;
    @Autowired
    UuidSubjectDao uuidSubjectDao;
    private Logger logger = LoggerFactory.getLogger(DefaultIdentitySearchService.class);
    private NavigableSet<Double> descRecsKeySet;
    private final Integer MAX_RECS_NO = 10;

    public DefaultIdentitySearchService() {
    }

    @Override
    public List<IdentityBean> findByForClass(String classification, Integer issueId) {


        //TODO: @Fotis We need to sort by similarity! ? 
        List<UuidIssue> uuidIssues = uuidIssueDao.findByIssueId(issueId);
        List<IdentityBean> recs = new ArrayList<IdentityBean>();
        TreeMap<Double, IdentityBean> recsFull = new TreeMap<Double, IdentityBean>();
        Integer ranking=1;
        for (UuidIssue ui : uuidIssues) {
            //TODO: We need to retrieve the name and surname of the developer from STARDOM
            recsFull.put(ui.getSimilarity(), new IdentityBean(ui.getUuid(), "name", "surname",ui.getSimilarity(),ranking));
            ranking++;
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

    @Override
    public IdentityBean findByUuid(String uuid) {
        logger.trace("Identity Retrieve by Id()");
        //return the same bug


        List<UuidSubject> thisUuidSubjects = uuidSubjectDao.findByUuid(uuid);

        String identityDescription = "";
        for (UuidSubject us : thisUuidSubjects) {
            identityDescription = identityDescription + "'" + us.getSubject() + "' ";
        }
        logger.debug(identityDescription);
//            // create a Bug & add it to data            
        IdentityBean identityBean = new IdentityBean(uuid, "name", identityDescription,0.0,0);
        return identityBean;
    }
}
