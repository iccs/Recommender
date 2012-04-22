package eu.alertproject.iccs.socrates.connector.internal;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.*;
import eu.alertproject.iccs.socrates.datastore.api.DatastoreRecommendationService;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.IdentityBean;
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
import java.util.Properties;

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

    @Autowired
    Properties systemProperties;


    @Autowired
    private DatastoreRecommendationService datastoreRecommendationService;

    @Override
    public void process(Message message) throws IOException, JMSException {


        String text = ((TextMessage) message).getText();
        logger.trace("void process() xml {} ",text);

        long start = System.currentTimeMillis();

        RecommendIdentityEnvelope rie =
                EventFactory.<RecommendIdentityEnvelope>fromXml(
                        text,
                        RecommendIdentityEnvelope.class);

        List<Issue> issues = rie.getBody()
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

        String patternId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData().getPatternId();



        

        List<IssueIdentities> issueIdentitieses = new ArrayList<IssueIdentities>();


        for(Issue i : issues){

            Integer bugId = Integer.valueOf(i.getUuid());


            List<IdentityBean> byForClass = datastoreRecommendationService.findByForClass(
                    "core developers",
                    bugId,
                    Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")),
                    Double.valueOf(systemProperties.getProperty("subject.similarity.weight")),
                    Double.valueOf(systemProperties.getProperty("subject.ranking.weight")),
                    Integer.valueOf(systemProperties.getProperty("recommendation.max.results")));
//            List<UuidIssue> byIssueId = uuidIssueDao.findByIssueId(bugId,
//                                    Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")));

            List<Identity> identities =new ArrayList<Identity>();
//            for(UuidIssue ui : byIssueId){
            for(IdentityBean ib : byForClass){
                identities.add(new Identity(ib.getUuid(),"No Name - "+ib.getUuid()));
            }


            issueIdentitieses.add(
                    new IssueIdentities(
                            i,
                            identities
                    )
            );
        }

        //create a response
        final String event = EventFactory.createRecommendationIdentityEvent(
                eventId,
                start,
                System.currentTimeMillis(),
                sequence++,
                patternId,
                issueIdentitieses
        );


        logger.trace("void process() Replying with {}",event);
        jmsTemplate.send(Topics.ALERT_SOCRATES_Identity_Recommendation,new TextMessageCreator(event));


    }



}
