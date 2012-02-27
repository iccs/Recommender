package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueUuidDao;
import eu.alertproject.iccs.socrates.domain.IssueUuid;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:24
 */
@Repository("issueUuidDao")
public class JpaIssueUuidDao extends JpaCommonDao<IssueUuid> implements IssueUuidDao{

    protected JpaIssueUuidDao() {
        super(IssueUuid.class);
    }

    @Override
    public void removeById(Integer integer) {

        Query query = getEntityManager().createQuery("DELETE FROM IssueUuid i WHERE i.issueUuidPk.issueId=:id");
        query.setParameter("id",integer);

        int i = query.executeUpdate();




    }
}
