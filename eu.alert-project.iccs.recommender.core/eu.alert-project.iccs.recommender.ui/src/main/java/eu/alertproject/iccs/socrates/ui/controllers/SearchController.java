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
@RequestMapping("/search")
public class SearchController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private BugSearchService bugSearchService;

    @Autowired
    private IdentitySearchService identitySearchService;

    @Autowired
    private ClassSearchService classSearchService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("search");
        mv.addObject("bug", null);
        return mv;
    }


    @RequestMapping(value = "/bug/{query}", method = RequestMethod.GET)
    public ModelAndView search(@PathVariable("query") Integer query){

        logger.trace("ModelAndView search() {} ",query);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/partials/bug");
        mv.addObject("bug", bugSearchService.retrieveBugById(query));
        return mv;

    }

    @RequestMapping(value = "/class/{query}", method = RequestMethod.GET)
    public ModelAndView searchClass(@PathVariable("query") Integer query){

        logger.trace("ModelAndView searchClass() {} ",query);


        ModelAndView mv = new ModelAndView();
        mv.setViewName("/partials/classess");
        mv.addObject("classess", classSearchService.retrieveClassByBugId(query));
        return mv;

    }
    
    @RequestMapping(value = "/identities/{query}", method = RequestMethod.GET)
    public ModelAndView searchIdentities(@PathVariable("query") String query){

        logger.trace("ModelAndView searchClass() {} ",query);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/partials/identities");
        mv.addObject("identities", identitySearchService.findByForClass(query));
        return mv;

    }

}
