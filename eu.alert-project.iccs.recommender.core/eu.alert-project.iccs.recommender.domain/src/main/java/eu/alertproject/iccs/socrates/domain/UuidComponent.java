package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:12
 */
@Entity
@Table(name="uuid_component")
public class UuidComponent implements SimpleBean{

    @EmbeddedId
    private UuidComponentPk uuidComponentPk;

    private Double similarity;


    
    public String getComponent() {
        return uuidComponentPk.getComponent();
    }

    public String getUuid() {
        return uuidComponentPk.getUuid();
    }
    
    
    public void setUuidAndComponent(String uuid, String component) {
        uuidComponentPk = new UuidComponentPk();
        uuidComponentPk.setUuid(uuid);
        uuidComponentPk.setComponent(component);
    }

    public UuidComponentPk getUuidComponentPk() {
        return uuidComponentPk;
    }

    public void setUuidComponentPk(UuidComponentPk uuidComponentPk) {
        this.uuidComponentPk = uuidComponentPk;
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
        if (!(o instanceof UuidComponent)) return false;

        UuidComponent that = (UuidComponent) o;

        if (similarity != null ? !similarity.equals(that.similarity) : that.similarity != null) return false;
        if (uuidComponentPk != null ? !uuidComponentPk.equals(that.uuidComponentPk) : that.uuidComponentPk != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuidComponentPk != null ? uuidComponentPk.hashCode() : 0;
        result = 31 * result + (similarity != null ? similarity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidComponent{" +
                "uuidComponentPk=" + uuidComponentPk +
                ", similarity=" + similarity +
                '}';
    }
}
