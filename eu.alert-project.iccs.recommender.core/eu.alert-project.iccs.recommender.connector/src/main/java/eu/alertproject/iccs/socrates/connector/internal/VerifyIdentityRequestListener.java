package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.api.AbstractActiveMQHandler;
import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.Identity;
import eu.alertproject.iccs.events.socrates.Issue;
import eu.alertproject.iccs.events.socrates.VerifyIdentityEnvelope;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
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
public class VerifyIdentityRequestListener extends AbstractActiveMQHandler {

    private Logger logger = LoggerFactory.getLogger(VerifyIdentityRequestListener.class);


    @Autowired
    UuidIssueDao uuidIssueDao;

    @Autowired
    Properties systemProperties;

    @Override
    public void process(ActiveMQMessageBroker broker, Message message) throws IOException, JMSException {


        String text = ((TextMessage) message).getText();

        long start = System.currentTimeMillis();

        VerifyIdentityEnvelope rie =  EventFactory.<VerifyIdentityEnvelope>fromXml(text,VerifyIdentityEnvelope.class);


        Issue issue = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getIssue();

        Identity identity = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getIdentity();

        String patternId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getPatternId();


        String eventId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getMeta()
                .getEventId();


        List<Identity> identities = new ArrayList<Identity>();

        List<UuidIssue> byIssueId = uuidIssueDao.findByIssueId(
                        Integer.valueOf(issue.getUuid()),
                        Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")));

        boolean found = false;
        for(UuidIssue ui : byIssueId){
            
            if(ui.getUuid().equals(identity.getUuid())){
                found = true;
                break;

            }
            
        }

        //create a response
        final String event = EventFactory.createVerifyIdentityEvent(
                eventId,
                start,
                System.currentTimeMillis(),
                broker.requestSequence(),
                identity,
                issue,
                patternId,
                found);

        broker.sendTextMessage(Topics.ALERT_SOCRATES_Identity_Verification, event);
    }



}
