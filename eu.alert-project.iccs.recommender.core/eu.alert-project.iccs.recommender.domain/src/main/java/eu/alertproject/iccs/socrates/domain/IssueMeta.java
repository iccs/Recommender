package eu.alertproject.iccs.socrates.domain;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 8/28/12
 * Time: 10:15 PM
 */
@Entity
@Table(name="issue_meta")
public class IssueMeta implements SimpleBean{

    @Id
    private Integer id;


    @Column(name="date_opened")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(name="subject")
    private String subject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

        IssueMeta issueMeta = (IssueMeta) o;

        if (date != null ? !date.equals(issueMeta.date) : issueMeta.date != null) return false;
        if (id != null ? !id.equals(issueMeta.id) : issueMeta.id != null) return false;
        if (subject != null ? !subject.equals(issueMeta.subject) : issueMeta.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueMeta{" +
                "id=" + id +
                ", date=" + date +
                ", subject='" + subject + '\'' +
                '}';
    }
}
