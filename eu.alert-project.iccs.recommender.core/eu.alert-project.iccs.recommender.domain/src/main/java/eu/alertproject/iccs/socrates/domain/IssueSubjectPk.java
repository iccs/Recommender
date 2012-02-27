package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:34
 */
@Embeddable
public class IssueSubjectPk implements Serializable {

    @Column(name="issue_id")
    private Integer issueId;

    @Column String subject;

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueSubjectPk that = (IssueSubjectPk) o;

        if (issueId != null ? !issueId.equals(that.issueId) : that.issueId != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueId != null ? issueId.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueSubjectPk{" +
                "issue_id=" + issueId +
                ", subject='" + subject + '\'' +
                '}';
    }
}
