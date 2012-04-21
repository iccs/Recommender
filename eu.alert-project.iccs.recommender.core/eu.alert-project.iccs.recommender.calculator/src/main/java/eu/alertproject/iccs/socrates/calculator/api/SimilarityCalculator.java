package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;

/**
 * User: fotis
 * Date: 21/04/12
 * Time: 15:09
 */
public interface SimilarityCalculator {
    
    public Double getSimilarity(AnnotatedObject item1, AnnotatedObject item2);
}
