package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:47
 */
@Embeddable
public class UuidClassPk implements Serializable {

    private String uuid;

    @Column(name = "class")
    private String clazz;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UuidClassPk that = (UuidClassPk) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidClassPk{" +
                "uuid='" + uuid + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}
