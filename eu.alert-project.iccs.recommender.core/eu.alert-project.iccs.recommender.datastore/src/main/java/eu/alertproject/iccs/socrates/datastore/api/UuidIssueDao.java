package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;

import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:28
 */
public interface UuidIssueDao extends CommonDao<UuidIssue>{

    List<UuidIssue> findByUuid(String uuid);
    void removeByUuid(String id);
    void removeByIssueId(Integer integer);

    List<UuidIssue> findByIssueId(Integer id);
}
