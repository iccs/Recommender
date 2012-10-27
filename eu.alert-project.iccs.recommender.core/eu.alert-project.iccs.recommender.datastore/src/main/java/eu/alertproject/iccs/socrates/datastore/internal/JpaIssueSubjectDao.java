package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueSubjectDao;
import eu.alertproject.iccs.socrates.domain.IssueSubject;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:23
 */
@Repository("issueSubjectDao")
public class JpaIssueSubjectDao extends JpaCommonDao<IssueSubject> implements IssueSubjectDao{

    protected JpaIssueSubjectDao() {
        super(IssueSubject.class);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<IssueSubject> findByIssueId(Integer id) {

        Query query = getEntityManager().createQuery("SELECT i FROM IssueSubject i WHERE i.issueSubjectPk.issueId = :id  ");
        query.setParameter("id", id);
        return query.getResultList();

    }
    @SuppressWarnings("unchecked")
    @Override
    public List<IssueSubject> findByIssueIdLimitByWeight(Integer id, Double weight) {

        Query query = getEntityManager().createQuery("SELECT i FROM IssueSubject i " +
                "WHERE " +
                "i.issueSubjectPk.issueId = :id " +
                "AND " +
                "i.weight >= :weight");
        query.setParameter("id", id);
        query.setParameter("weight", weight);
        return query.getResultList();

    }

    @Override
    public List<Integer> findAllIssues() {

        Query query = getEntityManager().createNativeQuery("SELECT * FROM all_issues");

        List resultList = query.getResultList();

        return resultList;

    }
}
