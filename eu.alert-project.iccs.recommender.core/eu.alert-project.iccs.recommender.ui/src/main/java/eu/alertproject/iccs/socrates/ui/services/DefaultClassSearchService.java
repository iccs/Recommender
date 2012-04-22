package eu.alertproject.iccs.socrates.ui.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 6:58 PM
 */
@Service("classSearchService")
public class DefaultClassSearchService implements ClassSearchService {


    private Logger logger = LoggerFactory.getLogger(DefaultClassSearchService.class);
    private Map<Integer, List<String>> data;
    private final Map<String,String> classNames;
    private final List<String> names;

    public DefaultClassSearchService() throws IOException {


        classNames = new LinkedHashMap<String, String>();
        classNames.put("Core-Developers","core developers");
        classNames.put("Bug-Triagers","bug triagers");
        classNames.put("Newest-Members","testers");


        names = new ArrayList<String>();
        names.add("Core-Developers");
        names.add("Bug-Triagers");
        names.add("Newest-Members");

    }



    @Override
    public List<String> retrieveClassByBugId(Integer id){
        return names;
    }

    @Override
    public String getClassForName(String query) {
        return classNames.get(query);
    }
}


