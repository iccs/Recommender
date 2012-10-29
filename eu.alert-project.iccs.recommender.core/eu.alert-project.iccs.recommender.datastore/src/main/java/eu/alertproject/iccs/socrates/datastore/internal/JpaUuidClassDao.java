package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidClassDao;
import eu.alertproject.iccs.socrates.domain.UuidClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:26
 */
@Repository("uuidClassDao")
public class JpaUuidClassDao extends JpaCommonDao<UuidClass> implements UuidClassDao{
    private Logger logger = LoggerFactory.getLogger(JpaUuidClassDao.class);

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
    public Double getMaxWeight(String classification){
        Query nativeQuery = getEntityManager().createNativeQuery(
                "select max(weight) from uuid_class where class=?1");

        nativeQuery.setParameter(1,classification);

        return ((Number)nativeQuery.getSingleResult()).doubleValue();

    }
    
    @Override
    public UuidClass findByUuidAndClass(String uuid, String classification) {

        Query query = getEntityManager().createQuery(
                "SELECT u FROM UuidClass u " +
                        "WHERE u.uuidClassPk.uuid =:uuid AND u.uuidClassPk.clazz=:classification ORDER BY u.weight DESC");

        query.setMaxResults(1);
        query.setParameter("classification", classification);
        query.setParameter("uuid", uuid);

        UuidClass uc = null;
        try{
            uc = (UuidClass) query.getSingleResult();
        }catch (NoResultException e){
            logger.warn("Couldn't find result for uuid={}, class={}", uuid, classification);
        }

        return uc;
    }

    @Override
    public List<UuidClass> findByClass(String classification,int maxResults) {

        Query query = getEntityManager().createQuery(
                "SELECT u FROM UuidClass u " +
                        "WHERE u.uuidClassPk.clazz=:classification ORDER BY u.weight DESC");

        query.setMaxResults(maxResults);
        query.setParameter("classification", classification);

        List<UuidClass> uc = new ArrayList<UuidClass>();
        try{
            uc = query.getResultList();
        }catch (NoResultException e){
            logger.warn("Couldn't find result for class={}", classification);
        }

        return uc;
    }

    @Override
    public List<UuidClass> findByUuid(String uuid) {

        Query query = getEntityManager().createQuery(
                "SELECT u FROM UuidClass u " +
                        "WHERE u.uuidClassPk.uuid =:uuid ORDER BY u.weight DESC");

        query.setParameter("uuid", uuid);

        List<UuidClass> uc = new ArrayList<UuidClass>();
        try{
            uc = (List<UuidClass>) query.getResultList();
        }catch (NoResultException e){
            logger.warn("Couldn't find result for uuid={}", uuid);
        }


        return uc;
    }

}
