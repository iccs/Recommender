package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:18
 */
@Entity
@Table(name="uuid_class")
public class UuidClass implements SimpleBean{
    

    @EmbeddedId
    private UuidClassPk uuidClassPk;

    private Double weight;

    public String getUuid() {
        return uuidClassPk.getUuid();
    }
    public String getClazz() {
        return uuidClassPk.getClazz();
    }

    public void setUuidAndClass(String uuid, String clazz) {
        uuidClassPk = new UuidClassPk();
        uuidClassPk.setUuid(uuid);
        uuidClassPk.setClazz(clazz);
    }


    public UuidClassPk getUuidClassPk() {
        return uuidClassPk;
    }

    public void setUuidClassPk(UuidClassPk uuidClassPk) {
        this.uuidClassPk = uuidClassPk;
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

        if (uuidClassPk != null ? !uuidClassPk.equals(uuidClass.uuidClassPk) : uuidClass.uuidClassPk != null)
            return false;
        if (weight != null ? !weight.equals(uuidClass.weight) : uuidClass.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuidClassPk != null ? uuidClassPk.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidClass{" +
                "uuidClassPk=" + uuidClassPk +
                ", weight=" + weight +
                '}';
    }
}
