package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:29
 */
public class JpaUuidIssueDao extends JpaCommonDao<UuidIssue> implements UuidIssueDao{

    protected JpaUuidIssueDao() {

        super(UuidIssue.class);

    }
}
