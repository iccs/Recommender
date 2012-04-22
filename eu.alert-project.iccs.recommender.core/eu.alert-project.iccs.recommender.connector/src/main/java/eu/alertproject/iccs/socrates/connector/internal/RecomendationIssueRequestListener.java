package eu.alertproject.iccs.socrates.connector.internal;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.*;
import eu.alertproject.iccs.socrates.datastore.api.DatastoreRecommendationService;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.Bug;
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
public class RecomendationIssueRequestListener extends SocratesActiveMQListener{

    private Logger logger = LoggerFactory.getLogger(RecomendationIssueRequestListener.class);

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


        Integer eventId = rie.getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getMeta()
                .getEventId();


        
        List<Issue> issues = new ArrayList<Issue>();

        for(IssueIdentities ii: issueIdentities){
            List<Identity> identities = ii.getIdentities();
            for(Identity i : identities){


                List<Bug> bugs = datastoreRecommendationService.retrieveForDevId(i.getUuid(),
                        Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")),
                        Double.valueOf(systemProperties.getProperty("subject.similarity.weight")),
                        Double.valueOf(systemProperties.getProperty("subject.ranking.weight")),
                        Integer.valueOf(systemProperties.getProperty("recommendation.max.results")));

//                List<UuidIssue> byIssueId = uuidIssueDao.findByUuid(i.getUuid(),
//                        Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")));

//                for(UuidIssue ui : byIssueId){
                  for(Bug b : bugs){
                    issues.add(new Issue(
                            String.valueOf(b.getId()),
                            b.getSubject()
                    ));
                }
            }
        }

        //create a response
        String event = EventFactory.createRecommendationIssuesEvent(
                eventId,
                start,
                System.currentTimeMillis(),
                sequence++,
                issues
        );

        jmsTemplate.send(
                Topics.ALERT_SOCRATES_Issue_Recommendation,
                new TextMessageCreator(event));

    }



}
