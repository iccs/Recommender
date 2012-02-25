package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.api.AbstractActiveMQListener;
import eu.alertproject.iccs.socrates.domain.ArtefactUpdated;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 14:48
 */
public abstract class SocratesActiveMQListener extends AbstractActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(SocratesActiveMQListener.class);

    @Override
    public void process(Message message) throws IOException, JMSException {

        ObjectMapper mapper = new ObjectMapper();
        String text = ((TextMessage) message).getText();

        logger.trace("void onMessage() Text to parse {} ", text);
        ArtefactUpdated artefactUpdated = mapper.readValue(text, ArtefactUpdated.class);
        logger.trace("void process() {} ",artefactUpdated);

        updateSimilarities(artefactUpdated);
    }

    abstract void updateSimilarities(ArtefactUpdated artefactUpdated);

}
