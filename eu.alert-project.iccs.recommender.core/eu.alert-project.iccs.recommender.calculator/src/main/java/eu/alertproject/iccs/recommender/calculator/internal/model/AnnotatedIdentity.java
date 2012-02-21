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
public class AnnotatedIdentity extends AnnotatedObject{
      private String identityId;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public AnnotatedIdentity(String identityId, HashMap<String, Double> annotations) {
        super(annotations);
        this.identityId = identityId;
    }
}
