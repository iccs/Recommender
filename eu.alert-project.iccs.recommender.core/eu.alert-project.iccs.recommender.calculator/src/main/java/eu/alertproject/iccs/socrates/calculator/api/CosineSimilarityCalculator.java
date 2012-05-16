package eu.alertproject.iccs.socrates.calculator.api;

import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: fotis
 * Date: 21/04/12
 * Time: 15:09
 */
@Service("similarityCalculator")
public class CosineSimilarityCalculator implements SimilarityCalculator {
    private Logger logger = LoggerFactory.getLogger(CosineSimilarityCalculator.class);



    @Override
    public Double getSimilarity(AnnotatedObject item1, AnnotatedObject item2) {
        if ((item1==null)||(item2==null)){
            throw new IllegalArgumentException("Items have not been initialized. Initialize the items and try again");
        }

        //create a set with the words they have in common
        Set<String> commonWordsSet = new HashSet<String>(item1.getAnnotations().keySet());
        commonWordsSet.retainAll(item2.getAnnotations().keySet());
        
//                Iterator item1Itr =item1.getAnnotations().keySet().iterator();
//        System.out.println("========Item 1 Words==============");
//        while (item1Itr.hasNext()) {
//            System.out.print(item1Itr.next() +" ");
//        }
//        
//                        Iterator item2Itr =item2.getAnnotations().keySet().iterator();
//        System.out.println("========Item 2 Words==============");
//        while (item2Itr.hasNext()) {
//            System.out.print(item2Itr.next() +" ");
//        }
//        
//        
//        
//        Iterator commonWordSetItr = commonWordsSet.iterator();
//        System.out.println("========Common Words==============");
//        while (commonWordSetItr.hasNext()) {
//            System.out.print(commonWordSetItr.next() +" ");
//        }

        System.out.println("Common words found " + commonWordsSet.size());
        //Calculates the cosine similarity. For the numerator we need only the common words, for the denominator the norms of the words found in each vector.
        double numerator = 0, norm1 = 0, norm2 = 0;

        /**
         * There is not point calculating the similarity if the
         * common wordset is empty as well as if any annotated
         * object has no annotations
         *
         */
        if(     commonWordsSet.size() <=0
           ||   item1.getAnnotations().keySet().size() <=0
           ||   item2.getAnnotations().keySet().size() <=0
                ){
            return 0.0;
        }

        for (String k : commonWordsSet) {
            numerator += item1.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }



        for (String k : item1.getAnnotations().keySet()) {
            norm1 += item1.getAnnotations().get(k) * item1.getAnnotations().get(k);
        }
        for (String k : item2.getAnnotations().keySet()) {
            norm2 += item2.getAnnotations().get(k) * item2.getAnnotations().get(k);
        }


        //this could return NaN if either norm1 or norm2 are 0
        if(norm1 <=0.0 || norm2 <=0.0){
            return 0.0;
        }


        Double similarity = numerator / Math.sqrt(norm1 * norm2);
        logger.trace("Double getSimilarity() {}/Math.sqrt({}*{})={}",
            new Object[]{
                numerator,
                norm1,
                norm2,
                similarity});

        return similarity;

    }
   
    public Double getIntegerSimilarity(HashMap<Integer, Integer> itemOne, HashMap<Integer, Integer> itemTwo) {
        if ((itemOne == null) || (itemTwo == null)) {
            throw new IllegalArgumentException("Items have not been initialized. Initialize the items and try again");
        }
        Set<Integer> commonWordsSet = new HashSet<Integer>(itemOne.keySet());
        commonWordsSet.retainAll(itemTwo.keySet());

        System.out.println("Common words found " + commonWordsSet.size());
        //Calculates the cosine similarity. For the numerator we need only the common words, for the denominator the norms of the words found in each vector.
        double numerator = 0, norm1 = 0, norm2 = 0;



        if (commonWordsSet.size() <= 0
                || itemOne.keySet().size() <= 0
                || itemTwo.keySet().size() <= 0) {
            return 0.0;
        }

        for (Integer k : commonWordsSet) {
            numerator += itemOne.get(k) * itemTwo.get(k);
        }



        for (Integer k : itemOne.keySet()) {
            norm1 += itemOne.get(k) * itemOne.get(k);
        }
        for (Integer k : itemTwo.keySet()) {
            norm2 += itemTwo.get(k) * itemTwo.get(k);
        }


        //this could return NaN if either norm1 or norm2 are 0
        if (norm1 <= 0.0 || norm2 <= 0.0) {
            return 0.0;
        }


        Double similarity = numerator / Math.sqrt(norm1 * norm2);
        logger.trace("Double getSimilarity() {}/Math.sqrt({}*{})={}",
                new Object[]{
                    numerator,
                    norm1,
                    norm2,
                    similarity});

        return similarity;

    }
}
