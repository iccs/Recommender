package eu.alertproject.iccs.socrates.domain;

/**
 * Created by IntelliJ IDEA. User: fotis Date: 08/02/12 Time: 11:57 PM To change
 * this template use File | Settings | File Templates.
 */
public class IdentityBean {

    private String uuid;
    private String name;
    private String lastname;
    private Double similarity;
    private Double ranking;

    public IdentityBean() {
    }

    public IdentityBean(String uuid, String name, String lastname, Double similarity, Double ranking) {
        this.uuid = uuid;
        this.name = name;
        this.lastname = lastname;
        this.similarity = similarity;
        this.ranking = ranking;
    }

    public Double getRanking() {
        return ranking;
    }

    public void setRanking(Double ranking) {
        this.ranking = ranking;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IdentityBean that = (IdentityBean) o;

        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IdentityBean{"
                + "uuid='" + uuid + '\''
                + ", name='" + name + '\''
                + ", lastname='" + lastname + '\''
                + ", similarity='" + similarity + '\''
                + ", ranking='" + ranking + '\''
                + '}';
    }
}
