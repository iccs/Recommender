package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.ui.bean.Bug;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 6:58 PM
 */

public class StaticBugSearchService implements BugSearchService{

    private Logger logger = LoggerFactory.getLogger(StaticBugSearchService.class);
    private Map<Integer,Bug> data;
    
    public StaticBugSearchService() throws IOException {

        data = new HashMap<Integer, Bug>();

        data.put(1010, new Bug(1010, "I am a super bug","This is my long description"));
        data.put(2050, new Bug(2050, "There is a problem with ACPI","When I close the lid on my laptop it continues running"));

    }

    @Override
    public Bug retrieveBugById(Integer id){

        logger.trace("Bug retrieveBugById()");
        //return the same bug
        if(!data.containsKey(id)){
            return null;
        }
        return data.get(id);
    }
    
}


