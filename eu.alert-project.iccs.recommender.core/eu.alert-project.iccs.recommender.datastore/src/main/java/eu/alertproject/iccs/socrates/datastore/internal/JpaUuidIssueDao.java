package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:29
 */
@Repository("uuidIssueDao")
public class JpaUuidIssueDao extends JpaCommonDao<UuidIssue> implements UuidIssueDao{

    private Logger logger = LoggerFactory.getLogger(JpaUuidIssueDao.class);

    protected JpaUuidIssueDao() {
        super(UuidIssue.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidIssue> findByUuid(String uuid,  double similarity) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidIssue u WHERE u.uuidIssuePk.uuid =:uuid AND u.similarity >= :similarity ORDER BY u.similarity DESC");
        query.setParameter("uuid",uuid);
        query.setParameter("similarity",similarity);

       return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidIssue> findByIssueId(Integer id, double similarity) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidIssue u WHERE u.uuidIssuePk.issueId =:id AND u.similarity >= :similarity " +
                "order by u.similarity DESC");
        query.setParameter("id", id);
        query.setParameter("similarity",similarity);


       return query.getResultList();
    }

    @Override
    public void removeAll() {
        getEntityManager().createNativeQuery("DELETE FROM uuid_issue").executeUpdate();
    }


    @Override
    public void removeByUuid(String uuid) {

        Query query = getEntityManager().createQuery("DELETE FROM UuidIssue u WHERE u.uuidIssuePk.uuid=:uuid");
        query.setParameter("uuid",uuid);
        query.executeUpdate();
    }

    @Override
    public void removeByIssueId(Integer id) {

        Query query = getEntityManager().createQuery("DELETE FROM UuidIssue u WHERE u.uuidIssuePk.issueId=:issueId");
        query.setParameter("issueId",id);
        query.executeUpdate();
    }

    @Override
    public UuidIssue findByUuidAndIssueId(String uuid, Integer issueId) {


        Query query = getEntityManager().createQuery(
                "SELECT u FROM UuidIssue u WHERE u.uuidIssuePk.issueId =:id AND u.uuidIssuePk.uuid =:uuid "+
                "order by u.similarity DESC");

        query.setMaxResults(1);
        query.setParameter("id", issueId);
        query.setParameter("uuid", uuid);

        UuidIssue ui = null;

        try{
            ui = (UuidIssue) query.getSingleResult();

        }catch (NoResultException e){
            logger.warn("Couldn't find UuidIssue for uuid={} and issueId={}",uuid,issueId);
        }


        return ui;



    }
}
