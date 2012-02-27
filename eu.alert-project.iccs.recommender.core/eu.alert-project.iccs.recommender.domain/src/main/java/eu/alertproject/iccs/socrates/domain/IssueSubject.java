package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:05
 */
@Entity
@Table(name="issue_subject")
public class IssueSubject implements SimpleBean{

    @EmbeddedId
    private IssueSubjectPk issueSubjectPk;

    @Column
    private Double weight;

    public Integer getIssueId() {
        return issueSubjectPk.getIssueId();
    }
    
    public String getSubject() {
        return issueSubjectPk.getSubject();
    }

    public void setIssueAndSubject(Integer issueId, String subject) {

        issueSubjectPk = new IssueSubjectPk();
        issueSubjectPk.setIssueId(issueId);
        issueSubjectPk.setSubject(subject);
    }

    public IssueSubjectPk getIssueSubjectPk() {
        return issueSubjectPk;
    }

    public void setIssueSubjectPk(IssueSubjectPk issueSubjectPk) {
        this.issueSubjectPk = issueSubjectPk;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

        IssueSubject that = (IssueSubject) o;

        if (issueSubjectPk != null ? !issueSubjectPk.equals(that.issueSubjectPk) : that.issueSubjectPk != null)
            return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueSubjectPk != null ? issueSubjectPk.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueSubject{" +
                "issueSubjectPk=" + issueSubjectPk +
                ", weight=" + weight +
                '}';
    }
}
