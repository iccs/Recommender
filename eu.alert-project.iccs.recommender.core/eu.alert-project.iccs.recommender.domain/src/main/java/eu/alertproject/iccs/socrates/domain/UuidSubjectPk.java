package eu.alertproject.iccs.socrates.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 16:50
 */
@Embeddable
public class UuidSubjectPk implements Serializable {

    private String uuid;
    private String subject;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UuidSubjectPk that = (UuidSubjectPk) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UuidSubjectPk{" +
                "uuid='" + uuid + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
