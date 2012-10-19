package eu.alertproject.iccs.socrates.datastore.api;

import eu.alertproject.iccs.events.socrates.Module;
import eu.alertproject.iccs.socrates.domain.Bug;
import eu.alertproject.iccs.socrates.domain.IdentityBean;

import java.util.List;

/**
 * User: fotis
 * Date: 21/04/12
 * Time: 23:27
 */
public interface DatastoreRecommendationService {

    List<IdentityBean> findByForClass(
            String classification,
            Integer issueId,
            double threshold,
            double similarityWeight,
            double rankingWeight,
            int maxRecommendations);

    List<Bug> retrieveForDevId(String uuid,
                               double threshold,
                               double similarityWeight,
                               double rankingWeight,
                               int maxRecommendations);

    List<Module> retrieveModulesForDevId(String uuid,
                               double threshold,
                               double similarityWeight,
                               double rankingWeight,
                               int maxRecommendations);
}
