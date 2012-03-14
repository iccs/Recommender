package eu.alertproject.iccs.socrates.connector.internal;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.RecommendIdentityEnvelope;
import eu.alertproject.iccs.events.socrates.RecommendIdentityPayload;
import eu.alertproject.iccs.events.socrates.RecommendIssuesPayload;
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
public class RecomendationIdentityRequestListener extends SocratesActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(RecomendationIdentityRequestListener.class);

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
        xStream.processAnnotations(RecommendIdentityEnvelope.class);
        RecommendIdentityEnvelope rie = (RecommendIdentityEnvelope) xStream.fromXML(text);

        List<RecommendIdentityPayload.EventData.Issue> issues = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getIssues();


        Integer eventId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getMeta()
                .getEventId();


        List<RecommendIssuesPayload.EventData.Identity> identities = new ArrayList<RecommendIssuesPayload.EventData.Identity>();


        for(RecommendIdentityPayload.EventData.Issue i : issues){

            Integer bugId = Integer.valueOf(i.getUuid());

            List<UuidIssue> byIssueId = uuidIssueDao.findByIssueId(bugId);
            
            for(UuidIssue ui : byIssueId){
                identities.add(new RecommendIssuesPayload.EventData.Identity(
                        ui.getUuid(),
                        "No Name - "+ui.getUuid()
                ));
            }
        }

        //create a response
        final String event = EventFactory.createRecommendationIdentityEvent(
                eventId,
                start,
                System.currentTimeMillis(),
                sequence++,
                identities
        );


        logger.trace("void process() Replying with {}",event);
        jmsTemplate.send(Topics.ALERT_SOCRATES_Identity_Recommendation,new TextMessageCreator(event));


    }



}
