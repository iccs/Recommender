package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueUuidDao;
import eu.alertproject.iccs.socrates.domain.IssueUuid;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:24
 */
public class JpaIssueUuidDao extends JpaCommonDao<IssueUuid> implements IssueUuidDao{

    protected JpaIssueUuidDao() {
        super(IssueUuid.class);
    }
}
