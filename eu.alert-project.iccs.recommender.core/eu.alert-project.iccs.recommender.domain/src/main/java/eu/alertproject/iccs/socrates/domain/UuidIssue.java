package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:12
 */
@Entity
@Table(name="uuid_issue",
        uniqueConstraints={@UniqueConstraint(columnNames={"uuid","issue_id"})})
public class UuidIssue implements SimpleBean{
    
    private String uuid;
    private Integer issue_id;
    private Double similarity;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(Integer issue_id) {
        this.issue_id = issue_id;
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

        if (issue_id != null ? !issue_id.equals(uuidIssue.issue_id) : uuidIssue.issue_id != null) return false;
        if (similarity != null ? !similarity.equals(uuidIssue.similarity) : uuidIssue.similarity != null) return false;
        if (uuid != null ? !uuid.equals(uuidIssue.uuid) : uuidIssue.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (issue_id != null ? issue_id.hashCode() : 0);
        result = 31 * result + (similarity != null ? similarity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidIssue{" +
                "uuid='" + uuid + '\'' +
                ", issue_id=" + issue_id +
                ", similarity=" + similarity +
                '}';
    }
}
