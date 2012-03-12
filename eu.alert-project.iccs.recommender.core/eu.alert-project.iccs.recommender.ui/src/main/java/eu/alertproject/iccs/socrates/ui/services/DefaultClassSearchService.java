package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.datastore.api.UuidIssueDao;
import eu.alertproject.iccs.socrates.ui.bean.Bug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

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

    public DefaultClassSearchService() throws IOException {

       

    }

    @Override
    public List<String> retrieveClassByBugId(Integer id){

        
        //TODO: @Fotis should this be related to the bug? or not? I would say no
        List<String> classList = new ArrayList<String>();
        classList.add("Core-Developers");
        classList.add("Bug-Triagers");
        classList.add("Newest-Members");

        return classList;
    }
    
}


