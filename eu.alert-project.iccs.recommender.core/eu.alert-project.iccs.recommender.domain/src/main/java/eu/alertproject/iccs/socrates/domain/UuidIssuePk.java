package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:43
 */
@Embeddable
public class UuidIssuePk implements Serializable {

    @Column(name="issue_id")
    private Integer issueId;
    
    @Column 
    private String uuid;


    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UuidIssuePk that = (UuidIssuePk) o;

        if (issueId != null ? !issueId.equals(that.issueId) : that.issueId != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueId != null ? issueId.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueUuidPk{" +
                "issueId=" + issueId +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
