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
    public String toString() {
        return "IssueSubjectPk{" +
                "issue_id=" + issueId +
                ", subject='" + subject + '\'' +
                '}';
    }
}
