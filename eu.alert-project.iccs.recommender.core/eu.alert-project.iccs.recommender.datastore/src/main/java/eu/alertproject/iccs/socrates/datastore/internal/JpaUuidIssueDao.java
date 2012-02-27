package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:29
 */
@Repository("uuidIssueDao")
public class JpaUuidIssueDao extends JpaCommonDao<UuidIssue> implements UuidIssueDao{

    protected JpaUuidIssueDao() {
        super(UuidIssue.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidIssue> findByUuid(String uuid) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidIssue u WHERE u.uuidIssuePk.uuid =:uuid");
        query.setParameter("uuid",uuid);

       return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UuidIssue> findByIssueId(Integer id) {

        Query query = getEntityManager().createQuery("SELECT u FROM UuidIssue u WHERE u.uuidIssuePk.issueId =:id");
        query.setParameter("id",id);

       return query.getResultList();
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


}
