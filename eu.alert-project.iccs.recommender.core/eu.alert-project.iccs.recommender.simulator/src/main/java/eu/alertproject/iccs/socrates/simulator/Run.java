package eu.alertproject.iccs.socrates.simulator;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.events.internal.ComponentUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.events.internal.IssueUpdated;
import eu.alertproject.iccs.socrates.domain.ComponentSubject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * User: fotis Date: 25/02/12 Time: 15:23
 */
public class Run {

    private static Logger logger = LoggerFactory.getLogger(Run.class);
    private static final int ISSUE_COUNT = 2000;
    private static final int COMPONENT_COUNT= 2000;

    public static void main(String[] args) throws IOException, InterruptedException {


        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");

        final JmsTemplate jmsTemplate = (JmsTemplate) classPathXmlApplicationContext.getBean("jmsTemplate");

        final Run run = new Run();

        final List<String> uuids = IOUtils.readLines(Run.class.getResourceAsStream("/uuids"));
        final List<String> issues = IOUtils.readLines(Run.class.getResourceAsStream("/issues"));
        final List<String> list = IOUtils.readLines(Run.class.getResourceAsStream("/5desk.txt"));

        final CountDownLatch cdl = new CountDownLatch(2);
        final int IDENTITIES_COUNT = 300;

        Thread issuesThread = new Thread() {

            @Override
            public void run() {

                List<ArtefactUpdated> artefactUpdateds = run.prepareIssueMap(issues,list);

                ObjectMapper mapper = new ObjectMapper();

                for (ArtefactUpdated artefactUpdated : artefactUpdateds) {

                    String s = null;
                    try {
                        s = mapper.writeValueAsString(artefactUpdated);
                        jmsTemplate.send(Topics.ICCS_STARDOM_Issue_Updated, new TextMessageCreator(s));
                    } catch (IOException e) {
                        logger.warn("void run(args) ", e);
                    } finally {

                        try {
                            Thread.sleep(RandomUtils.nextInt(10) * 1000);
                        } catch (InterruptedException e) {
                            logger.warn("void run(args) ", e);
                        }

                    }
                }

                cdl.countDown();
            }
        };




        Thread identities = new Thread() {

            @Override
            public void run() {


                List<IdentityUpdated> artefactUpdateds = run.prepareUuidMap(
                        uuids,
                        Arrays.asList("Core", "Testers"),
                        list);


                ObjectMapper mapper = new ObjectMapper();

                for (ArtefactUpdated au : artefactUpdateds) {

                    String s = null;
                    try {
                        s = mapper.writeValueAsString(au);
                        jmsTemplate.send(Topics.ICCS_STARDOM_Identity_Updated, new TextMessageCreator(s));
                    } catch (IOException e) {
                        logger.warn("void run(args) ", e);
                    } finally {

                        try {
                            Thread.sleep(RandomUtils.nextInt(5) * 1000);
                        } catch (InterruptedException e) {
                            logger.warn("void run(args) ", e);
                        }

                    }
                }

                cdl.countDown();
            }
        };


        Thread components = new Thread() {

            @Override
            public void run() {


                List<ArtefactUpdated> artefactUpdateds = run.prepareComponentMap(
                        Arrays.asList("kde-bluetooth-utils","solid","kde-hardware-libs","none"),
                        list
                );


                ObjectMapper mapper = new ObjectMapper();

                for (ArtefactUpdated au : artefactUpdateds) {

                    String s = null;
                    try {
                        s = mapper.writeValueAsString(au);
                        jmsTemplate.send(Topics.ICCS_STARDOM_Component_Updated, new TextMessageCreator(s));
                    } catch (IOException e) {
                        logger.warn("void run(args) ", e);
                    } finally {

                        try {
                            Thread.sleep(RandomUtils.nextInt(5) * 1000);
                        } catch (InterruptedException e) {
                            logger.warn("void run(args) ", e);
                        }

                    }
                }

                cdl.countDown();
            }
        };


        identities.start();
        components.start();
        issuesThread.start();


        cdl.await();

        logger.trace("void main(args) Done");

    }

    public List<ArtefactUpdated> prepareIssueMap(List<String> issues, List<String> topics) {

        List<ArtefactUpdated> data = new ArrayList<ArtefactUpdated>();


        for (int i = 0; i < ISSUE_COUNT; i++) {

            List<Keui.Concept> pairs = new ArrayList<Keui.Concept>();
            List<String> addedTopics = new ArrayList<String>();

            while (pairs.size() < 100) {
                String topic = topics.get(RandomUtils.nextInt(topics.size()));

                if (addedTopics.contains(topic)) {
                    logger.trace("List<ArtefactUpdated> prepareIssueMap() Already added {} ", topic);
                    continue;
                }

                Keui.Concept concept = new Keui.Concept();
                concept.setUri(topic);
                concept.setWeight(RandomUtils.nextInt());
                pairs.add(concept);
                addedTopics.add(topic);

            }

            IssueUpdated issueUpdated = new IssueUpdated();
            issueUpdated.setDate(new Date());
            issueUpdated.setId(String.valueOf(issues.get(RandomUtils.nextInt(issues.size()))));
            issueUpdated.setSubject("This is the subject of issue " + issueUpdated.getId());
            issueUpdated.setConcepts(pairs);

            data.add(issueUpdated);
        }


        return data;

    }

    public List<IdentityUpdated> prepareUuidMap(List<String> uuids, List<String> classes, List<String> topics) {

        List<IdentityUpdated> data = new ArrayList<IdentityUpdated>();

        for (int i = 0; i < 3000; i++) {

            List<Keui.Concept> pairs = new ArrayList<Keui.Concept>();
            List<String> addedTopics = new ArrayList<String>();

            while (pairs.size() < 100) {

                String topic = topics.get(RandomUtils.nextInt(topics.size()));

                if (addedTopics.contains(topic)) {
                    logger.trace("List<ArtefactUpdated> prepareIssueMap() Already added {} ", topic);

                    continue;
                }

                Keui.Concept concept = new Keui.Concept();
                concept.setUri(topic);
                concept.setWeight(RandomUtils.nextInt());
                pairs.add(concept);
                addedTopics.add(topic);

            }



            IdentityUpdated identityUpdated = new IdentityUpdated();
            identityUpdated.setId(uuids.get(RandomUtils.nextInt(uuids.size())));
            identityUpdated.setConcepts(pairs);

            Map<String, Double> cis = new HashMap<String, Double>();

            for (int j = 0; j < RandomUtils.nextInt(classes.size()); j++) {
                cis.put(classes.get(RandomUtils.nextInt(classes.size() - 1) + 1), RandomUtils.nextDouble());
            }

            identityUpdated.setCis(cis);
            data.add(identityUpdated);

        }

        return data;

    }

    public List<ArtefactUpdated> prepareComponentMap(List<String> components, List<String> topics) {

        List<ArtefactUpdated> data = new ArrayList<ArtefactUpdated>();


        for (int i = 0; i < COMPONENT_COUNT; i++) {

            List<Keui.Concept> pairs = new ArrayList<Keui.Concept>();
            List<String> addedTopics = new ArrayList<String>();

            while (pairs.size() < 100) {
                String topic = topics.get(RandomUtils.nextInt(topics.size()));

                if (addedTopics.contains(topic)) {
                    logger.trace("List<ArtefactUpdated> prepareIssueMap() Already added {} ", topic);
                    continue;
                }

                Keui.Concept concept = new Keui.Concept();
                concept.setUri(topic);
                concept.setWeight(RandomUtils.nextInt());
                pairs.add(concept);
                addedTopics.add(topic);

            }

            String componentStr = components.get(RandomUtils.nextInt(components.size()));
            ComponentUpdated cu = new ComponentUpdated();
            cu.setId("uri#"+componentStr);
            cu.setComponent(componentStr);
            cu.setConcepts(pairs);

            data.add(cu);
        }


        return data;

    }

    static class TextMessageCreator implements MessageCreator {

        private String text;

        TextMessageCreator(String text) {
            this.text = text;
        }

        @Override
        public Message createMessage(Session session) throws JMSException {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(text);

            logger.trace("Message createMessage() {} ", text);
            return textMessage;
        }
    }
}
