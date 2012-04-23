package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidNameDao;
import eu.alertproject.iccs.socrates.domain.UuidName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * User: fotis
 * Date: 23/04/12
 * Time: 21:22
 */
@Repository("uuidNameDao")
public class JpaUuidNameDao extends JpaCommonDao<UuidName> implements UuidNameDao{
    private Logger logger = LoggerFactory.getLogger(JpaUuidNameDao.class);

    public JpaUuidNameDao() {
        super(UuidName.class);
    }

    @Override
    public String findNameByUuid(String uuid) {
        Query query = getEntityManager().createQuery("select u from UuidName u where u.uuidNamePk.uuid=:uuid");
        
        query.setParameter("uuid",uuid);
        
        String ret= "No Name";
        try{
            UuidName singleResult = (UuidName) query.getSingleResult();
            ret = singleResult.getName();
        }catch (NoResultException e){
            logger.warn("Could not find name for uuid = {} ",uuid);
            
        }

        return ret;
    }

    @Override
    public UuidName findByUuid(String uuid) {
        Query query = getEntityManager().createQuery("select u from UuidName u where u.uuidNamePk.uuid=:uuid");

        query.setParameter("uuid",uuid);

        UuidName ret= null;
        try{
            ret = (UuidName) query.getSingleResult();
        }catch (NoResultException e){
            logger.warn("Could not find name for uuid = {} ",uuid);

        }

        return ret;
    }
}
