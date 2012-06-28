package eu.alertproject.iccs.socrates.ui.controllers;

import eu.alertproject.iccs.socrates.datastore.api.DatastoreRecommendationService;
import eu.alertproject.iccs.socrates.ui.services.BugSearchService;
import eu.alertproject.iccs.socrates.ui.services.ClassSearchService;
import eu.alertproject.iccs.socrates.ui.services.IdentitySearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 20:12
 */

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    private Logger logger = LoggerFactory.getLogger(DeveloperController.class);


    @Autowired
    private DatastoreRecommendationService datastoreRecommendationService;

    @Autowired
    private Properties systemProperties;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("developer");
        mv.addObject("developer", null);
        return mv;
    }


    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public ModelAndView search(@PathVariable("query") String query){

        logger.error("ModelAndView search() {} ",query);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/partials/developer");
        mv.addObject("developer",
                datastoreRecommendationService.retrieveForDevId(
                        query,
                        Double.valueOf(systemProperties.getProperty("subject.similarity.threshold")),
                                        Double.valueOf(systemProperties.getProperty("subject.similarity.weight")),
                                        Double.valueOf(systemProperties.getProperty("subject.ranking.weight")),
                                        Integer.valueOf(systemProperties.getProperty("recommendation.max.results"))));
        return mv;

    }

  

}
