package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.domain.IdentityBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IdentitySearchService {
    IdentityBean findByUuid(String uuid);

}
