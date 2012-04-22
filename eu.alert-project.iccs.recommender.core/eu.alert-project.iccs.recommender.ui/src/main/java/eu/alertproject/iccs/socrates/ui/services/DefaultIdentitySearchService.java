package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.UuidClassDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidSubjectDao;
import eu.alertproject.iccs.socrates.domain.IdentityBean;
import eu.alertproject.iccs.socrates.domain.UuidClass;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import eu.alertproject.iccs.socrates.domain.UuidSubject;
import org.springframework.stereotype.Service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA. User: fotis Date: 08/02/12 Time: 11:56 PM To change
 * this template use File | Settings | File Templates.
 */
@Service("identitySearchService")
public class DefaultIdentitySearchService implements IdentitySearchService {

    private Logger logger = LoggerFactory.getLogger(DefaultIdentitySearchService.class);

    @Autowired
    UuidSubjectDao uuidSubjectDao;

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
        IdentityBean identityBean = new IdentityBean(uuid, "name", identityDescription,0.0,0.0);
        return identityBean;
    }
}
