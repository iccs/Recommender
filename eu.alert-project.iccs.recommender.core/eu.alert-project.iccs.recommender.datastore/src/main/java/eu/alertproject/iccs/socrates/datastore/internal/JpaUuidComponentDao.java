package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidComponentDao;
import eu.alertproject.iccs.socrates.domain.UuidComponent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:29
 */
@Repository("uuidComponentDao")
public class JpaUuidComponentDao extends JpaCommonDao<UuidComponent> implements UuidComponentDao{

    protected JpaUuidComponentDao() {
        super(UuidComponent.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidComponent> findByUuid(String uuid,  double similarity) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidComponent u WHERE u.uuidComponentPk.uuid =:uuid AND u.similarity >= :similarity");
        query.setParameter("uuid",uuid);
        query.setParameter("similarity",similarity);

       return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidComponent> findByComponent(String component, double similarity) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidComponent  u WHERE u.uuidComponentPk.component =:id AND u.similarity >= :similarity");
        query.setParameter("id",component);
        query.setParameter("similarity",similarity);

       return query.getResultList();
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {

        Query query = getEntityManager().createQuery("DELETE FROM UuidComponent u WHERE u.uuidComponentPk.uuid=:uuid");
        query.setParameter("uuid",uuid);
        query.executeUpdate();
    }

    @Override
    public void removeByComponent(String component) {

        Query query = getEntityManager().createQuery("DELETE FROM UuidComponent u WHERE u.uuidComponentPk.component=:component");
        query.setParameter("component",component);
        query.executeUpdate();
    }


}
