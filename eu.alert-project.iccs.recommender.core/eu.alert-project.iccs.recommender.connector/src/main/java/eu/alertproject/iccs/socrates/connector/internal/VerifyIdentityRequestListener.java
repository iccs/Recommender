package eu.alertproject.iccs.socrates.connector.internal;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.Identity;
import eu.alertproject.iccs.events.socrates.Issue;
import eu.alertproject.iccs.events.socrates.RecommendIdentityEnvelope;
import eu.alertproject.iccs.events.socrates.VerifyIdentityEnvelope;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.UuidIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 14:44
 */
public class VerifyIdentityRequestListener extends SocratesActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(VerifyIdentityRequestListener.class);

    private int sequence = 1;
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Autowired
    UuidIssueDao uuidIssueDao;

    @Override
    public void process(Message message) throws IOException, JMSException {


        String text = ((TextMessage) message).getText();
        logger.trace("void process() xml {} ",text);

        long start = System.currentTimeMillis();

        XStream xStream = new XStream();
        xStream.processAnnotations(VerifyIdentityEnvelope.class);
        VerifyIdentityEnvelope rie = (VerifyIdentityEnvelope) xStream.fromXML(text);

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


        Integer eventId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getMeta()
                .getEventId();


        List<Identity> identities = new ArrayList<Identity>();

        List<UuidIssue> byIssueId = uuidIssueDao.findByIssueId(Integer.valueOf(issue.getUuid()));

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
                sequence++,
                identity,
                issue,
                found);

        logger.trace("void process() Replying with {}",event);
        jmsTemplate.send(Topics.ALERT_SOCRATES_Identity_Verification,new TextMessageCreator(event));


    }



}
