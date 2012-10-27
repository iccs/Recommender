package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.api.AbstractActiveMQHandler;
import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.socrates.Identity;
import eu.alertproject.iccs.events.socrates.Issue;
import eu.alertproject.iccs.events.socrates.IssueIdentities;
import eu.alertproject.iccs.events.socrates.RecommendIssuesEnvelope;
import eu.alertproject.iccs.socrates.datastore.api.DatastoreRecommendationService;
import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.domain.Bug;
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
public class RecomendationIssueRequestListener extends AbstractActiveMQHandler {

    private Logger logger = LoggerFactory.getLogger(RecomendationIssueRequestListener.class);

    private int sequence = 1;

    @Autowired
    UuidIssueDao uuidIssueDao;

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

        if(!"true".equals(systemProperties.getProperty("subject.similarity.threshold.force"))){
            threshold = threshold == null ?
                    Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")):
                    threshold.doubleValue();

        }else{
            threshold =Double.valueOf(systemProperties.getProperty("subject.similarity.threshold"));
        }



        List<Issue> issues = new ArrayList<Issue>();

        for(IssueIdentities ii: issueIdentities){
            List<Identity> identities = ii.getIdentities();
            for(Identity i : identities){


                List<Bug> bugs = datastoreRecommendationService.retrieveForDevId(i.getUuid(),
                        threshold,
                        Double.valueOf(systemProperties.getProperty("subject.similarity.weight")),
                        Double.valueOf(systemProperties.getProperty("subject.ranking.weight")),
                        Integer.valueOf(systemProperties.getProperty("recommendation.max.results")));

//                List<UuidIssue> byIssueId = uuidIssueDao.findByUuid(i.getUuid(),
//                        Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")));

//                for(UuidIssue ui : byIssueId){
                  for(Bug b : bugs){
                    issues.add(new Issue(
                            String.valueOf(b.getId()),
                            b.getSubject(),
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


        broker.sendTextMessage(Topics.ALERT_SOCRATES_Issue_Recommendation, event);

    }



}
