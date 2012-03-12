package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
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
    
    private Logger logger = LoggerFactory.getLogger(DefaultIdentitySearchService.class);

    public DefaultIdentitySearchService() {
    }

    @Override
    public List<IdentityBean> findByForClass(String classification,Integer issueId) {

        
        //TODO: @Fotis We need to sort by similarity! ? 
        List<UuidIssue> uuidIssues = uuidIssueDao.findByIssueId(issueId);
        List<IdentityBean> recs = new ArrayList<IdentityBean>();
        for (UuidIssue ui : uuidIssues) {
            //TODO: We need to retrieve the name and surname of the developer from STARDOM
            recs.add(new IdentityBean(ui.getUuid(), "name", "surname"));
        }


        if (recs == null) {
            logger.debug("recs are null");
        }
        return recs;

    }
}
