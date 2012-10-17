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
@Table(name="uuid_subject",
        uniqueConstraints={@UniqueConstraint(columnNames={"uuid","subject"})})
public class UuidSubject implements SimpleBean{

    @EmbeddedId
    private UuidSubjectPk uuidSubjectPk;

    private Double weight;

    public String getUuid() {
        return uuidSubjectPk.getUuid();
    }

    public String getSubject() {
        return uuidSubjectPk.getSubject();
    }


    public void setUuidAndSubject(String uuid, String subject){
        uuidSubjectPk = new UuidSubjectPk();
        uuidSubjectPk.setUuid(uuid);
        uuidSubjectPk.setSubject(subject);
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

        if (uuidSubjectPk != null ? !uuidSubjectPk.equals(that.uuidSubjectPk) : that.uuidSubjectPk != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuidSubjectPk != null ? uuidSubjectPk.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "UuidSubject{" +
                "uuidSubjectPk=" + uuidSubjectPk +
                ", weight=" + weight +
                '}';
    }
}
