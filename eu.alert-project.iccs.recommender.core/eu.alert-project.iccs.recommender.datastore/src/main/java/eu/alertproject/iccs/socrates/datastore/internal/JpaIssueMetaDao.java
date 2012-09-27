package eu.alertproject.iccs.socrates.datastore.internal;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.socrates.datastore.api.IssueMetaDao;
import eu.alertproject.iccs.socrates.domain.IssueMeta;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/28/12
 * Time: 2:17 AM
 */
@Repository("issueMetaDao")
public class JpaIssueMetaDao  extends JpaCommonDao<IssueMeta> implements IssueMetaDao {{
}
    protected JpaIssueMetaDao() {
        super(IssueMeta.class);
    }
}