package eu.alertproject.iccs.socrates.connector.internal;

import eu.alertproject.iccs.events.api.AbstractActiveMQListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 14:48
 */
public abstract class SocratesActiveMQListener extends AbstractActiveMQListener{

    @Autowired
    Properties systemProperties;

    @PostConstruct
    public void post(){

        setProcessDisabled(
                Boolean.valueOf(systemProperties.getProperty("activemq.processDisabled")));

    }


}
