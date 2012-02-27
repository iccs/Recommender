package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.IssueSubject;

import javax.persistence.Entity;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:04
 */
public interface IssueSubjectDao extends CommonDao<IssueSubject> {

    List<IssueSubject> findByIssueId(Integer integer);

}
