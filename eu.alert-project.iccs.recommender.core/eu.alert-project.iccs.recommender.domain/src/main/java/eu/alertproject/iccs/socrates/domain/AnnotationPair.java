package eu.alertproject.iccs.socrates.domain;

import java.io.Serializable;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 14:44
 */
public class AnnotationPair implements Serializable {
    
    private String subject;
    private Integer count;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
