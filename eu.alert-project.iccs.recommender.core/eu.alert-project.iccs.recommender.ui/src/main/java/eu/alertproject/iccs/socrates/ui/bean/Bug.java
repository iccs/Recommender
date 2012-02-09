package eu.alertproject.iccs.socrates.ui.bean;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bug {
    private int id;
    private String subject;
    private String description;

    public Bug(int id, String subject, String description) {
        this.id = id;
        this.subject = subject;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bug bug = (Bug) o;

        if (id != bug.id) return false;
        if (description != null ? !description.equals(bug.description) : bug.description != null) return false;
        if (subject != null ? !subject.equals(bug.subject) : bug.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String   toString() {
        return "Bug{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
