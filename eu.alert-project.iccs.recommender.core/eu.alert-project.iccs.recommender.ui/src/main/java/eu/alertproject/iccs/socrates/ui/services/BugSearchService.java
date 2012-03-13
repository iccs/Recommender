package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.ui.bean.Bug;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BugSearchService {

    Bug retrieveBugById(Integer id);

    List<Bug> retrieveForDevId(String uuid);
}
