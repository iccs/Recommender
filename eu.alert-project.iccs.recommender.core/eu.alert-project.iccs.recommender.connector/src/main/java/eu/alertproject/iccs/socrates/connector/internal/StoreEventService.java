package eu.alertproject.iccs.socrates.connector.internal;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 20/04/12
 * Time: 00:26
 */
@Service("storeEventService")
public class StoreEventService {

    @Autowired
    Properties systemProperties;

    AtomicInteger id = new AtomicInteger();
    
    
    public void store(String prefix, String text) throws IOException {

        if(Boolean.valueOf(systemProperties.getProperty("storage.enable"))){

            //store
            FileUtils.copyInputStreamToFile(
                new ByteArrayInputStream(text.getBytes()),
                new File(
                        systemProperties.getProperty("storage.path"),
                        File.separatorChar+prefix+"_"+id.incrementAndGet()+".json.txt"
                )
            );

        }
    }
}
