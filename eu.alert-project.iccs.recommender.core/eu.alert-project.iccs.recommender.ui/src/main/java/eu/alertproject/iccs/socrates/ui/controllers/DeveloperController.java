package eu.alertproject.iccs.socrates.ui.controllers;

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
    private IdentitySearchService identitySearchService;
    
    @Autowired
    private BugSearchService bugSearchService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("developer");
        mv.addObject("developer", null);
        return mv;
    }


    @RequestMapping(value = "/developer/search/{query}", method = RequestMethod.GET)
    public ModelAndView search(@PathVariable("query") String query){

        logger.error("ModelAndView search() {} ",query);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/partials/developer");
        mv.addObject("developer", bugSearchService.retrieveForDevId(query));
        return mv;

    }

  

}
