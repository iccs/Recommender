package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.events.internal.ComponentUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:36
 */
public interface RecommendationService {

    void updateSimilaritiesForIdentity(IdentityUpdated identityUpdated);
    void updateSimilaritiesForIssue(ArtefactUpdated artefactUpdated);
    void updateSimilaritiesForComponent(ComponentUpdated artefactUpdated);
}
