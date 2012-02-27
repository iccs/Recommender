package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:12
 */
@Entity
@Table(name="uuid_issue")
public class UuidIssue implements SimpleBean{

    @EmbeddedId
    private IssueUuidPk uuidIssuePk;

    private Double similarity;


    
    public Integer getIssueId() {
        return uuidIssuePk.getIssueId();
    }

    public String getUuid() {
            return uuidIssuePk.getUuid();
        }
    
    
    public void setUuidAndIssue(String uuid, Integer issueId) {
        uuidIssuePk = new IssueUuidPk();
        uuidIssuePk.setUuid(uuid);
        uuidIssuePk.setIssueId(issueId);
    }

    public IssueUuidPk getUuidIssuePk() {
        return uuidIssuePk;
    }

    public void setUuidIssuePk(IssueUuidPk uuidIssuePk) {
        this.uuidIssuePk = uuidIssuePk;
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

        UuidIssue uuidIssue = (UuidIssue) o;

        if (similarity != null ? !similarity.equals(uuidIssue.similarity) : uuidIssue.similarity != null) return false;
        if (uuidIssuePk != null ? !uuidIssuePk.equals(uuidIssue.uuidIssuePk) : uuidIssue.uuidIssuePk != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuidIssuePk != null ? uuidIssuePk.hashCode() : 0;
        result = 31 * result + (similarity != null ? similarity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidIssue{" +
                "uuidIssuePk=" + uuidIssuePk +
                ", similarity=" + similarity +
                '}';
    }
}
