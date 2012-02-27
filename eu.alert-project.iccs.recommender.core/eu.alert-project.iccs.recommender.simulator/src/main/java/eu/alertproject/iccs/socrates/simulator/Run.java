package eu.alertproject.iccs.socrates.simulator;

import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.socrates.domain.AnnotationPair;
import eu.alertproject.iccs.socrates.domain.ArtefactUpdated;
import eu.alertproject.iccs.socrates.domain.IdentityUpdated;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 15:23
 */
public class Run {

    private static Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) throws IOException, InterruptedException {


        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");

        final JmsTemplate jmsTemplate = (JmsTemplate) classPathXmlApplicationContext.getBean("jmsTemplate");

        final Run run = new Run();

        final List<String> list = IOUtils.readLines(Run.class.getResourceAsStream("/5desk.txt"));

        final CountDownLatch cdl = new CountDownLatch(2);


        Thread  issues = new Thread(){
            @Override
            public void run() {

                List<ArtefactUpdated> artefactUpdateds = run.prepareIssueMap(list);

                ObjectMapper mapper = new ObjectMapper();

                for(ArtefactUpdated artefactUpdated : artefactUpdateds){

                    String s = null;
                    try {
                        s = mapper.writeValueAsString(artefactUpdated);
                        jmsTemplate.send(Topics.ALERT_STARDOM_Issue_Updated, new TextMessageCreator(s));
                    } catch (IOException e) {
                        logger.warn("void run(args) ", e);
                    }finally {

                        try {
                            Thread.sleep(RandomUtils.nextInt(10)*1000);
                        } catch (InterruptedException e) {
                            logger.warn("void run(args) ", e);
                        }

                    }
                }

                cdl.countDown();
            }
        };

        
        

        Thread  identities = new Thread(){
            @Override
            public void run() {
                
                List<String> uuids=  new ArrayList<String>();
                for(int i = 0 ; i < 100 ; i++){
                    uuids.add(UUID.randomUUID().toString());
                }


                

                List<IdentityUpdated> artefactUpdateds = run.prepareUuidMap(
                                                                uuids,
                                                                Arrays.asList("Core","Testers"),
                                                                list);


                ObjectMapper mapper = new ObjectMapper();

                for(ArtefactUpdated au : artefactUpdateds){

                    String s = null;
                    try {
                        s = mapper.writeValueAsString(au);
                        jmsTemplate.send(Topics.ALERT_STARDOM_Identity_Updated, new TextMessageCreator(s));
                    } catch (IOException e) {
                        logger.warn("void run(args) ", e);
                    }finally {

                        try {
                            Thread.sleep(RandomUtils.nextInt(5)*1000);
                        } catch (InterruptedException e) {
                            logger.warn("void run(args) ", e);
                        }

                    }
                }

                cdl.countDown();
            }
        };


        identities.start();
        issues.start();


        cdl.await();

        logger.trace("void main(args) Done");

    }
    
    public List<ArtefactUpdated> prepareIssueMap(List<String> topics){


        List<ArtefactUpdated> data = new ArrayList<ArtefactUpdated>();


        for( int i = 0 ; i < 2000; i++){
            
            List<AnnotationPair> pairs = new ArrayList<AnnotationPair> ();

            List<String> addedTopics = new ArrayList<String>();
            
            while(pairs.size() < 100){
                String topic = topics.get(RandomUtils.nextInt(topics.size()));
                
                if(addedTopics.contains(topic)){
                    logger.trace("List<ArtefactUpdated> prepareIssueMap() Already added {} ",topic);
                    continue;
                }

                AnnotationPair annotationPair = new AnnotationPair();
                annotationPair.setSubject(topic);
                annotationPair.setCount(RandomUtils.nextDouble());
                pairs.add(annotationPair);
                addedTopics.add(topic);

            }

            ArtefactUpdated artefactUpdated = new ArtefactUpdated();
            artefactUpdated.setId(String.valueOf(i));
            artefactUpdated.setAnnotations(pairs);

            data.add(artefactUpdated);
        }


        return data;

    }
    

    public List<IdentityUpdated> prepareUuidMap(List<String> uuids, List<String> classes, List<String> topics){


        
        List<IdentityUpdated> data = new ArrayList<IdentityUpdated>();

        for( int i = 0 ; i < 3000; i++){

            List<AnnotationPair> pairs = new ArrayList<AnnotationPair> ();

            List<String> addedTopics = new ArrayList<String>();

            while(pairs.size() < 100){

                String topic = topics.get(RandomUtils.nextInt(topics.size()));

                if(addedTopics.contains(topic)){
                    logger.trace("List<ArtefactUpdated> prepareIssueMap() Already added {} ",topic);

                    continue;
                }

                AnnotationPair annotationPair = new AnnotationPair();
                annotationPair.setSubject(topic);
                annotationPair.setCount(RandomUtils.nextDouble());
                pairs.add(annotationPair);
                addedTopics.add(topic);

            }

            

            IdentityUpdated identityUpdated = new IdentityUpdated();
            identityUpdated.setId(uuids.get(RandomUtils.nextInt(uuids.size())));
            identityUpdated.setAnnotations(pairs);

            List<IdentityUpdated.CI> cis =  new ArrayList<IdentityUpdated.CI>();

            for(int j =0 ; j < RandomUtils.nextInt(classes.size()); j++){

                IdentityUpdated.CI ci = new IdentityUpdated.CI();
                ci.setClazz(classes.get(RandomUtils.nextInt(classes.size()-1)+1));
                ci.setWeight(RandomUtils.nextDouble());
                cis.add(ci);

            }

            identityUpdated.setCis(cis);
            data.add(identityUpdated);

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

            logger.trace("Message createMessage() {} ",text);
            return  textMessage;
        }

    }


}
