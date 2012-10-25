package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.ComponentSubjectDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueSubjectDao;
import eu.alertproject.iccs.socrates.domain.ComponentSubject;
import eu.alertproject.iccs.socrates.domain.IssueSubject;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:23
 */
@Repository("componentSubjectDao")
public class JpaComponentSubjectDao extends JpaCommonDao<ComponentSubject> implements ComponentSubjectDao{

    protected JpaComponentSubjectDao() {
        super(ComponentSubject.class);
    }


    @Override
    public List<ComponentSubject> findByComponent(String component) {

        Query query = getEntityManager().createQuery("SELECT i FROM ComponentSubject i WHERE i.componentSubjectPk.component = :id ");
        query.setParameter("id", component);
        return query.getResultList();

    }

    @Override
    public List<ComponentSubject> findByComponentLimitByWeight(String component, Double weight) {
        Query query = getEntityManager().createQuery("SELECT i FROM ComponentSubject i " +
                "WHERE " +
                "i.componentSubjectPk.component = :id " +
                "AND " +
                "i.weight >= :weight");
        query.setParameter("id", component);
        query.setParameter("weight", weight);
        return query.getResultList();

    }

    @Override
    public List<ComponentSubject> findAllByWeight(Double weight) {
        Query query = getEntityManager().createQuery("SELECT i FROM ComponentSubject i " +
                "WHERE " +
                "i.weight >= :weight " +
                "order by i.componentSubjectPk.component, i.weight DESC ");
        query.setParameter("weight", weight);
        return query.getResultList();

    }

    @Override
    public List<String> findAllComponents() {

        Query query = getEntityManager().createNativeQuery("SELECT * FROM all_components");

        List resultList = query.getResultList();

        return resultList;

    }
}
