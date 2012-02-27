package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueSubjectDao;
import eu.alertproject.iccs.socrates.domain.IssueSubject;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:23
 */
public class JpaIssueSubjectDao extends JpaCommonDao<IssueSubject> implements IssueSubjectDao{

    protected JpaIssueSubjectDao() {
        super(IssueSubject.class);
    }

}
