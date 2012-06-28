package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:43
 */
@Embeddable
public class UuidComponentPk implements Serializable {

    @Column(name="component")
    private String component;
    
    @Column 
    private String uuid;


    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidComponentPk)) return false;

        UuidComponentPk that = (UuidComponentPk) o;

        if (component != null ? !component.equals(that.component) : that.component != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = component != null ? component.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidComponentPk{" +
                "component='" + component + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
