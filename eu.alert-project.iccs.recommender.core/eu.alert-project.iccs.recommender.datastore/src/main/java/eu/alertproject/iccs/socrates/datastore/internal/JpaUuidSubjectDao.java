package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidSubjectDao;
import eu.alertproject.iccs.socrates.domain.UuidSubject;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:30
 */
public class JpaUuidSubjectDao extends JpaCommonDao<UuidSubject> implements UuidSubjectDao{

    protected JpaUuidSubjectDao() {
        super(UuidSubject.class);
    }

}
