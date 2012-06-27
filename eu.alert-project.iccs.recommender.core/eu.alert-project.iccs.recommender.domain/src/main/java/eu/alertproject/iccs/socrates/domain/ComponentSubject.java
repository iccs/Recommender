package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 13:05
 */
@Entity
@Table(name="issue_subject")
public class ComponentSubject implements SimpleBean{

    @EmbeddedId
    private ComponentSubjectPk componentSubjectPk;

    @Column
    private Double weight;

    public String getComponent() {
        return componentSubjectPk.getComponent();
    }
    
    public String getSubject() {
        return componentSubjectPk.getSubject();
    }

    public void setComponenAndSubject(String component, String subject) {

        ComponentSubjectPk componentSubjectPk = new ComponentSubjectPk();
        componentSubjectPk.setComponent(component);
        componentSubjectPk.setSubject(subject);

    }

    public ComponentSubjectPk getComponentSubjectPk() {
        return componentSubjectPk;
    }

    public void setComponentSubjectPk(ComponentSubjectPk componentSubjectPk) {
        this.componentSubjectPk = componentSubjectPk;
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

        ComponentSubject that = (ComponentSubject) o;

        if (componentSubjectPk != null ? !componentSubjectPk.equals(that.componentSubjectPk) : that.componentSubjectPk != null)
            return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = componentSubjectPk != null ? componentSubjectPk.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComponentSubject{" +
                "componentSubjectPk=" + componentSubjectPk +
                ", weight=" + weight +
                '}';
    }
}
