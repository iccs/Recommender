package eu.alertproject.iccs.socrates.domain;

import java.io.Serializable;
import java.util.List;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 14:43
 */
public class ArtefactUpdated implements Serializable {
    
    private String id;
    private List<AnnotationPair> annotations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AnnotationPair> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationPair> annotations) {
        this.annotations = annotations;
    }
}
