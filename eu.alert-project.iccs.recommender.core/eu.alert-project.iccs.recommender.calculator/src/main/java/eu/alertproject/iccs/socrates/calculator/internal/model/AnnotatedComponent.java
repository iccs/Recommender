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
public class AnnotatedComponent extends AnnotatedObject {
     private String component;

    public AnnotatedComponent(String component, HashMap<String, Double> annotations) {
        super(annotations);
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String commponent) {
        this.component = component;
    }
    
}
