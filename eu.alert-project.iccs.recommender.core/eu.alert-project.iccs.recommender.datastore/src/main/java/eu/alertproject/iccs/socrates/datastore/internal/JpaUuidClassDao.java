package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidClassDao;
import eu.alertproject.iccs.socrates.domain.UuidClass;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:26
 */
@Repository("uuidClassDao")
public class JpaUuidClassDao extends JpaCommonDao<UuidClass> implements UuidClassDao{

    protected JpaUuidClassDao() {
        super(UuidClass.class);
    }


    @Override
    public void removeByUuid(String uuid) {

        Query query = getEntityManager().createQuery("DELETE FROM UuidClass u WHERE u.uuidClassPk.uuid=:uuid");
        query.setParameter("uuid",uuid);
        query.executeUpdate();

    }
    
        @Override
    public List<UuidClass> findByUuidAndClass(String uuid, String classification) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidClass u WHERE u.uuidClassPk.uuid =:uuid AND u.uuidClassPk.clazz=:classification");
        query.setParameter("uuid", uuid);
        query.setParameter("classification", classification);
        return query.getResultList();
    }

}
