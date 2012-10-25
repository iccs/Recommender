package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.ComponentSubject;
import eu.alertproject.iccs.socrates.domain.IssueSubject;

import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:04
 */
public interface ComponentSubjectDao extends CommonDao<ComponentSubject> {

    List<ComponentSubject> findByComponent(String component);
    List<String> findAllComponents();
    @SuppressWarnings("unchecked")
    List<ComponentSubject> findByComponentLimitByWeight(String component, Double weight);

    List<ComponentSubject> findAllByWeight(Double weight);
}
