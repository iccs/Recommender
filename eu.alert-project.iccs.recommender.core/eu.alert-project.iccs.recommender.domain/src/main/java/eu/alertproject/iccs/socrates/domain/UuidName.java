package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:18
 */
@Entity
@Table(name="uuid_name")
public class UuidName implements SimpleBean{
    

    @EmbeddedId
    private UuidNamePk uuidNamePk;


    public String getUuid() {
        return uuidNamePk.getUuid();
    }
    public String getName() {
        return uuidNamePk.getName();
    }

    public void setUuidAndName(String uuid, String name) {
        uuidNamePk = new UuidNamePk();
        uuidNamePk.setUuid(uuid);
        uuidNamePk.setName(name);
    }


    public UuidNamePk getUuidClassPk() {
        return uuidNamePk;
    }

    public void setUuidNamePk(UuidNamePk uuidNamePk) {
        this.uuidNamePk = uuidNamePk;
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
    public String toString() {
        return "UuidName{" +
                "uuidNamePk=" + uuidNamePk +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidName)) return false;

        UuidName uuidName = (UuidName) o;

        if (uuidNamePk != null ? !uuidNamePk.equals(uuidName.uuidNamePk) : uuidName.uuidNamePk != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuidNamePk != null ? uuidNamePk.hashCode() : 0;
    }
}
