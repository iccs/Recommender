package eu.alertproject.iccs.socrates.domain;

import java.io.Serializable;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 14:44
 */
public class AnnotationPair implements Serializable {
    
    private String subject;
    private Double count;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
