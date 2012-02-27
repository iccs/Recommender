package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:12
 */
@Entity
@Table(name="issue_uuid")
public class IssueUuid implements SimpleBean{
    
    @EmbeddedId
    private IssueUuidPk issueUuidPk;

    private Double similarity;

    public Integer getIssueId() {
        return issueUuidPk.getIssueId();
    }

    public String getUuid() {
        return issueUuidPk.getUuid();
    }

    public void setIssueAndUuid(Integer issueId, String uuid) {
        issueUuidPk = new IssueUuidPk();
        issueUuidPk.setIssueId(issueId);
        issueUuidPk.setUuid(uuid);
    }

    public IssueUuidPk getIssueUuidPk() {
        return issueUuidPk;
    }

    public void setIssueUuidPk(IssueUuidPk issueUuidPk) {
        this.issueUuidPk = issueUuidPk;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public Integer getId() {
        throw new NotImplementedException();
    }

    @Override
    public void setId(Integer id) {
        throw new NotImplementedException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueUuid issueUuid = (IssueUuid) o;

        if (issueUuidPk != null ? !issueUuidPk.equals(issueUuid.issueUuidPk) : issueUuid.issueUuidPk != null)
            return false;
        if (similarity != null ? !similarity.equals(issueUuid.similarity) : issueUuid.similarity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueUuidPk != null ? issueUuidPk.hashCode() : 0;
        result = 31 * result + (similarity != null ? similarity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueUuid{" +
                "issueUuidPk=" + issueUuidPk +
                ", similarity=" + similarity +
                '}';
    }
}
