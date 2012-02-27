package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.domain.ArtefactUpdated;
import eu.alertproject.iccs.socrates.domain.IdentityUpdated;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:36
 */
public interface RecommendationService {

    void updateSimilaritiesForIdentity(IdentityUpdated identityUpdated);
    void updateSimilaritiesForIssue(ArtefactUpdated artefactUpdated);

}
