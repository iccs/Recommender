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
@Table(name="uuid_subject",
        uniqueConstraints={@UniqueConstraint(columnNames={"uuid","subject"})})
public class UuidSubject implements SimpleBean{
    
    private String uuid;
    private String subject;
    private Double weight;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

        UuidSubject that = (UuidSubject) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }



    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidSubject{" +
                "uuid='" + uuid + '\'' +
                ", subject='" + subject + '\'' +
                ", weight=" + weight +
                '}';
    }
}
