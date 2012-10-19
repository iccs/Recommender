package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.api.AbstractActiveMQHandler;
import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.Identity;
import eu.alertproject.iccs.events.socrates.IssueIdentities;
import eu.alertproject.iccs.events.socrates.Module;
import eu.alertproject.iccs.events.socrates.RecommendIssuesEnvelope;
import eu.alertproject.iccs.socrates.datastore.api.DatastoreRecommendationService;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 14:44
 */
public class RecomendationModuleRequestListener extends AbstractActiveMQHandler {

    private Logger logger = LoggerFactory.getLogger(RecomendationModuleRequestListener.class);

    private int sequence = 1;

    @Autowired
    UuidIssueDao uuidComponentDao;

    @Autowired
    Properties systemProperties;

    @Autowired
    private DatastoreRecommendationService datastoreRecommendationService;

    @Override
    public void process(ActiveMQMessageBroker broker, Message message) throws IOException, JMSException {

        String text = ((TextMessage) message).getText();

        long start = System.currentTimeMillis();

        RecommendIssuesEnvelope rie = EventFactory.<RecommendIssuesEnvelope>fromXml(
                text,
                RecommendIssuesEnvelope.class
        );

        List<IssueIdentities> issueIdentities = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getIssueIdentities();


        String eventId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getMeta()
                .getEventId();


        Double threshold = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getRanking();

        threshold = threshold == null ?
                Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")):
                threshold.doubleValue();


        List<Module> modules = new ArrayList<Module>();

        for(IssueIdentities ii: issueIdentities){
            List<Identity> identities = ii.getIdentities();
            for(Identity i : identities){


                modules= datastoreRecommendationService.retrieveModulesForDevId(i.getUuid(),
                        threshold,
                        Double.valueOf(systemProperties.getProperty("subject.similarity.weight")),
                        Double.valueOf(systemProperties.getProperty("subject.ranking.weight")),
                        Integer.valueOf(systemProperties.getProperty("recommendation.max.results")));
            }
        }

        //create a response
        String event = EventFactory.createRecommendationModuleEvent(
                eventId,
                start,
                System.currentTimeMillis(),
                sequence++,
                modules
        );

        logger.trace("void process([broker, message]) {} ",event);

        broker.sendTextMessage(Topics.ALERT_SOCRATES_Module_Recommendation, event);

    }



}
