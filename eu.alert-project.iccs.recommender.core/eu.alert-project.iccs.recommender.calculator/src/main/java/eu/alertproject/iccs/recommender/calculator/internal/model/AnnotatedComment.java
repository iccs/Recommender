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
public class AnnotatedComment extends AnnotatedObject{
  
    private String commentId;

    public AnnotatedComment(String commentId, HashMap<String, Double> annotations) {
        super(annotations);
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    
    
}
