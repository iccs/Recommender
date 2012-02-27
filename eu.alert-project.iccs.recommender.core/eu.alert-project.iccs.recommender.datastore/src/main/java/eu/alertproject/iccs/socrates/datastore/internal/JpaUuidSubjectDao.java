package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidSubjectDao;
import eu.alertproject.iccs.socrates.domain.UuidSubject;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:30
 */
@Repository("uuidSubjectDao")
public class JpaUuidSubjectDao extends JpaCommonDao<UuidSubject> implements UuidSubjectDao{

    protected JpaUuidSubjectDao() {
        super(UuidSubject.class);
    }

    @Override
    public List<UuidSubject> findByUuid(String uuid) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidSubject u WHERE u.uuidSubjectPk.uuid =:uuid");
        query.setParameter("uuid",uuid);

       return query.getResultList();

    }
}
