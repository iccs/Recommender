/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.socrates.calculator.internal.text;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Calculates the similarity between two annotated objects
 * @author Kostas Christidis
 */
public class AnnotatedObjectSimilarity implements Similarity {

    private Double similarity;      // the similarity between the two objects
    private AnnotatedObject item1;

    public AnnotatedObjectSimilarity(AnnotatedObject item1, AnnotatedObject item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public AnnotatedObject getItem1() {
        return item1;
    }

    public void setItem1(AnnotatedObject item1) {
        this.item1 = item1;
    }

    public AnnotatedObject getItem2() {
        return item2;
    }

    public void setItem2(AnnotatedObject item2) {
        this.item2 = item2;
    }
    private AnnotatedObject item2;

    @Override
    public Double calculateSimilarity() throws Exception{
        if ((item1==null)||(item2==null)){
            throw new Exception("Items have not been initialized. Initialize the items and try again");
        }
        
        //create a set with the words they have in common
        Set<String> commonWordsSet = new HashSet(item1.getAnnotations().keySet());
        commonWordsSet.retainAll(item2.getAnnotations().keySet());
        
        //Calculates the cosine similarity. For the numerator we need only the common words, for the denominator the norms of the words found in each vector.
        double numerator = 0, norm1 = 0, norm2 = 0;
        for (String k : commonWordsSet) {
            numerator += item1.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }
        for (String k : item1.getAnnotations().keySet()) {
            norm1 += item1.getAnnotations().get(k) * item1.getAnnotations().get(k);
        }
        for (String k : item2.getAnnotations().keySet()) {
            norm2 += item2.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }
        similarity = numerator / Math.sqrt(norm1 * norm2);
        return similarity;

    }

    @Override
    public Double getSimilarity() throws Exception{
        if (similarity!=null){
            return similarity;
        }
        else{
            throw new Exception("the similarity has not been calculated yet. Calculate the similarity and then get it from this class");
        } 
            
    }
}
