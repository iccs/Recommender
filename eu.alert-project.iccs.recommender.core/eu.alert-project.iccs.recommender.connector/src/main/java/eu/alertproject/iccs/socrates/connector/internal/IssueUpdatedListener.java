package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.socrates.domain.ArtefactUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
public class IssueUpdatedListener extends SocratesActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(IssueUpdatedListener.class);

    @Override
    void updateSimilarities(ArtefactUpdated artefactUpdated) {
        logger.trace("void updateSimilarities() {} ",artefactUpdated);
    }

}
