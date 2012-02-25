package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.socrates.domain.ArtefactUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 12:41
 */
public class IdentityUpdatedListener extends SocratesActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(IdentityUpdatedListener.class);


    @Override
    void updateSimilarities(ArtefactUpdated artefactUpdated) {

        logger.trace("void updateSimilarities() {} ",artefactUpdated);

    }
}
