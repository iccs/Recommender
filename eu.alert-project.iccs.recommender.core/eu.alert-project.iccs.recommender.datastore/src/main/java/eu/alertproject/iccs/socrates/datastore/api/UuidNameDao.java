package eu.alertproject.iccs.socrates.datastore.api;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.socrates.domain.UuidClass;
import eu.alertproject.iccs.socrates.domain.UuidName;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:25
 */
public interface UuidNameDao extends CommonDao<UuidName> {
    
    String findNameByUuid(String uuid);

    UuidName findByUuid(String uuid);
}
