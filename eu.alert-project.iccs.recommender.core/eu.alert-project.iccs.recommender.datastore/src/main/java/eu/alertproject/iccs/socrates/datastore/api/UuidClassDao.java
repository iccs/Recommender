package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.UuidClass;

import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:25
 */
public interface UuidClassDao extends CommonDao<UuidClass> {
    void removeByUuid(String id);
    UuidClass findByUuidAndClass(String uuid, String classification);

    Double getMaxWeight(String classification);
    List<UuidClass> findByUuid(String uuid);
}
