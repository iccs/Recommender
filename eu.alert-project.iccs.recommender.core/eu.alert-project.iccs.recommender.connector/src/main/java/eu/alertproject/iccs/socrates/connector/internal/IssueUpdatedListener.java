package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.socrates.calculator.api.RecommendationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
public class IssueUpdatedListener extends AbstractArtefactUpdateListener<ArtefactUpdated>{

    private Logger logger = LoggerFactory.getLogger(IssueUpdatedListener.class);

    @Autowired
    RecommendationService recommendationService;

    @Override
    void updateSimilarities(ArtefactUpdated artefactUpdated) {
        logger.trace("void updateSimilarities() {} ",artefactUpdated);
        recommendationService.updateSimilaritiesForIssue(artefactUpdated);
    }

    @Override
    public ArtefactUpdated processText(ObjectMapper mapper, String text) throws IOException {
        ArtefactUpdated artefactUpdated = mapper.readValue(text, ArtefactUpdated.class);
        logger.trace("ArtefactUpdated process() {} ",artefactUpdated);
        return artefactUpdated;
    }

}
