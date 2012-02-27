package eu.alertproject.iccs.socrates.domain;

import java.util.List;

/**
 * User: fotis
 * Date: 27/02/12
 * Time: 12:23
 */
public class IdentityUpdated extends ArtefactUpdated{
    private List<CI> cis;


    public List<CI> getCis() {
        return cis;
    }

    public void setCis(List<CI> cis) {
        this.cis = cis;
    }

    public static class CI {

        private double weight;
        private String clazz;

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }
    }
}
