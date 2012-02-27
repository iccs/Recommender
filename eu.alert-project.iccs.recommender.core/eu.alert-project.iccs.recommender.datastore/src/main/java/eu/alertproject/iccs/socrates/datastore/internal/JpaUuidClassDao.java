package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidClassDao;
import eu.alertproject.iccs.socrates.domain.UuidClass;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:26
 */
public class JpaUuidClassDao extends JpaCommonDao<UuidClass> implements UuidClassDao{

    protected JpaUuidClassDao() {
        super(UuidClass.class);
    }


}
