package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.UuidComponent;
import eu.alertproject.iccs.socrates.domain.UuidIssue;

import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:28
 */
public interface UuidComponentDao extends CommonDao<UuidComponent>{

    List<UuidComponent> findByUuid(String uuid, double similarity);
    List<UuidComponent> findByComponent(String component, double similarity);
    void removeByUuid(String id);
    void removeByComponent(String component);

}
