package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:34
 */
@Embeddable
public class ComponentSubjectPk implements Serializable {

    @Column(name="component")
    private String component;

    @Column String subject;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentSubjectPk that = (ComponentSubjectPk) o;

        if (component != null ? !component.equals(that.component) : that.component != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = component != null ? component.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComponentSubjectPk{" +
                "component='" + component + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
