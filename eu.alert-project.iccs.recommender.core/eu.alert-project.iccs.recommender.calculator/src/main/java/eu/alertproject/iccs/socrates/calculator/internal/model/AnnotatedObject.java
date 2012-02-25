/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.socrates.calculator.internal.model;

import java.util.HashMap;

/**
 *
 * @author kostas
 */
public abstract class AnnotatedObject  {

    public AnnotatedObject(HashMap<String, Double> annotations) {
        this.annotations = annotations;
    }
    
    private HashMap<String,Double> annotations;

    public HashMap<String, Double> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(HashMap<String, Double> annotations) {
        this.annotations = annotations;
    }
    

   
    
    
}
