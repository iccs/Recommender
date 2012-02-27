package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:05
 */
@Entity
@Table(name="issue_subject",
        uniqueConstraints={@UniqueConstraint(columnNames={"issue_id","subject"})})
public class IssueSubject implements SimpleBean{
    
    @Column(name="issue_id")
    private Integer issueId;
    
    @Column
    private String subject;
    
    @Column
    private Double weight;

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

        if (issueId != null ? !issueId.equals(that.issueId) : that.issueId != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issueId != null ? issueId.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueSubject{" +
                "issueId=" + issueId +
                ", subject='" + subject + '\'' +
                ", weight=" + weight +
                '}';
    }
}
