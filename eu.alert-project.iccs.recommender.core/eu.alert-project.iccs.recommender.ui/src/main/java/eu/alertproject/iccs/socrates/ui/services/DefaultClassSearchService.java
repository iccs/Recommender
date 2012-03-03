package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.ui.bean.Bug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 6:58 PM
 */
@Service("classSearchService")
public class DefaultClassSearchService implements ClassSearchService{

    private Logger logger = LoggerFactory.getLogger(DefaultClassSearchService.class);
    private Map<Integer,List<String>> data;

    public DefaultClassSearchService() throws IOException {

        data = new HashMap<Integer, List<String>>();
        
        List<String> one = new ArrayList<String>();
        one.add("Top-20");
        one.add("Bug-Triagers");

        List<String> two = new ArrayList<String>();
        two.add("Hackers");
        two.add("Bug-Triagers");

        data.put(1010, one);
        data.put(2050, two);

    }

    @Override
    public List<String> retrieveClassByBugId(Integer id){

        //return the same bug
        if(!data.containsKey(id)){
            return null;
        }
        return data.get(id);
    }
    
}


