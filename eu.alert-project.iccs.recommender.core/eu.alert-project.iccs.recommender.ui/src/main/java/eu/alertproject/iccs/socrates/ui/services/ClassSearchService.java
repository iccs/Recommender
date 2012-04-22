package eu.alertproject.iccs.socrates.ui.services;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 09/02/12
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClassSearchService {

    List<String> retrieveClassByBugId(Integer id);

    String getClassForName(String query);

}
