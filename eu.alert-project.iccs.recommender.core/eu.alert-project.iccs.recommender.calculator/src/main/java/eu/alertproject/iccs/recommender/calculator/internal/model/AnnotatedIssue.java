/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.recommender.calculator.internal.model;

import java.util.HashMap;

/**
 *
 * @author kostas
 */
public class AnnotatedIssue extends AnnotatedObject {
     private String issueId;

    public AnnotatedIssue(String issueId, HashMap<String, Double> annotations) {
        super(annotations);
        this.issueId = issueId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String commentId) {
        this.issueId = commentId;
    }
    
}
