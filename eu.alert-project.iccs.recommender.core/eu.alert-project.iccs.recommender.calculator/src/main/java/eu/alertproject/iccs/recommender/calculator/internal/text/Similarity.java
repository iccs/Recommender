/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.recommender.calculator.internal.text;

/**
 *
 * @author Kostas Christidis
 */
public interface Similarity {
    
  
    public void calculateSimilarity() throws Exception;
    public Double getSimilarity() throws Exception;
    
}
