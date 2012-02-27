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
 * Time: 13:18
 */
@Entity
@Table(name="uuid_class",
        uniqueConstraints={@UniqueConstraint(columnNames={"uuid","class"})})
public class UuidClass implements SimpleBean{
    
    private String uuid;

    @Column(name = "class")
    private String clazz;

    private Double weight;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
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

        UuidClass uuidClass = (UuidClass) o;

        if (clazz != null ? !clazz.equals(uuidClass.clazz) : uuidClass.clazz != null) return false;
        if (uuid != null ? !uuid.equals(uuidClass.uuid) : uuidClass.uuid != null) return false;
        if (weight != null ? !weight.equals(uuidClass.weight) : uuidClass.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidClass{" +
                "uuid='" + uuid + '\'' +
                ", clazz='" + clazz + '\'' +
                ", weight=" + weight +
                '}';
    }
}
