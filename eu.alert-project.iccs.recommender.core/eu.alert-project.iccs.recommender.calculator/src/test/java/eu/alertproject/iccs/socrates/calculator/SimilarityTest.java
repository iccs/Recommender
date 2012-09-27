/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.socrates.calculator;

import java.util.HashMap;

import eu.alertproject.iccs.socrates.calculator.api.SimilarityComputationServiceImpl;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedComment;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kostas Christidis
 */
public class SimilarityTest {

    private AnnotatedComment annotatedComment;
    private AnnotatedIdentity annotatedIdentity;
    private AnnotatedIssue annotatedIssue;
    private AnnotatedObject annotatedObject;
    private SimilarityComputationServiceImpl similarityCalculator;

    public SimilarityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        similarityCalculator = new SimilarityComputationServiceImpl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIssueToIdentityZeroCommon() {
        try {
            long start = System.currentTimeMillis();



            HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();
            identityAnnotations.put("cat", 1.0);
            identityAnnotations.put("elephant", 2.0);
            identityAnnotations.put("dog", 0.5);
            annotatedIdentity = new AnnotatedIdentity("10938", identityAnnotations);


            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();
            issueAnnotations.put("llama", 1.0);
            issueAnnotations.put("lamp", 2.0);
            issueAnnotations.put("lion", 0.5);
            annotatedIssue = new AnnotatedIssue("1394", issueAnnotations);


            Double similarity =
                    similarityCalculator.getSimilarity(annotatedIssue, annotatedIdentity);
            System.out.println("Issue to identity similarity - zero common: " + similarity);
            assertNotNull(similarity);
            assertEquals(0.0, similarity, 0.0001);

            long end = System.currentTimeMillis();
            System.out.println("Execution time for zero common was " + (end - start) + " ms.");

        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Test
    public void testIssueToIdentity() {
        try {
            long start = System.currentTimeMillis();

            HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();
            identityAnnotations.put("cat", 1.0);
            identityAnnotations.put("elephant", 2.0);
            identityAnnotations.put("dog", 0.5);
            loadWithRandomWords(identityAnnotations, 10000);
            annotatedIdentity = new AnnotatedIdentity("10938", identityAnnotations);


            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();
            issueAnnotations.put("cat", 1.0);
            issueAnnotations.put("lamp", 2.0);
            issueAnnotations.put("dog", 0.5);
            loadWithRandomWords(issueAnnotations, 10000);
            loadWithWordsFromOtherMap(issueAnnotations,identityAnnotations,5000);
            annotatedIssue = new AnnotatedIssue("1394", issueAnnotations);


            Double similarity =
                    similarityCalculator.getSimilarity(annotatedIssue, annotatedIdentity);
            System.out.println("Issue to identity similarity: " + similarity);
            assertNotNull(similarity);
//            assertEquals(0.23809523809523808, similarity, 0.0001);


            long end = System.currentTimeMillis();
            System.out.println("Execution time for issue similarity as " + (end - start) + " ms.");

        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }




    }
    
       @Test
    public void testIssueToIdentityInteger() {
        try {
            long start = System.currentTimeMillis();
             System.out.println(" \n =================================== \n Integer Similarity =======\n ");

            HashMap<Integer, Integer> identityAnnotations = new HashMap<Integer, Integer>();
            loadWithRandomWordsInteger(identityAnnotations, 10000);

            HashMap<Integer, Integer> issueAnnotations = new HashMap<Integer, Integer>();
            loadWithRandomWordsInteger(issueAnnotations, 10000);
            loadWithWordsFromOtherMapInteger(issueAnnotations, identityAnnotations, 5000);

            Double similarity =
                    similarityCalculator.getIntegerSimilarity(issueAnnotations, identityAnnotations);
            System.out.println(" \n =================================== \n Issue to identity similarity: " + similarity);
            assertNotNull(similarity);
//            assertEquals(0.23809523809523808, similarity, 0.0001);


            long end = System.currentTimeMillis();
            System.out.println("Execution time for Integer issue similarity as " + (end - start) + " ms.");

        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Test
    public void testCommentToIdentity() {
        try {
            long start = System.currentTimeMillis();

            HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();
            identityAnnotations.put("cat", 1.0);
            identityAnnotations.put("elephant", 2.0);
            identityAnnotations.put("dog", 0.5);
            annotatedIdentity = new AnnotatedIdentity("10938", identityAnnotations);

            HashMap<String, Double> commentAnnotations = new HashMap<String, Double>();
            commentAnnotations.put("lamp", 1.0);
            commentAnnotations.put("elephant", 1.0);
            commentAnnotations.put("dog", 0.5);
            annotatedComment = new AnnotatedComment("10138", commentAnnotations);

            Double similarity =
                    similarityCalculator.getSimilarity(annotatedComment, annotatedIdentity);
            System.out.println("Comment to identity similarity: " + similarity);
            assertNotNull(similarity);
            assertEquals(0.6546536707079771, similarity, 0.0001);
            long end = System.currentTimeMillis();
            System.out.println("Execution time for identity comment similarity as " + (end - start) + " ms.");

        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadWithRandomWords(HashMap<String, Double> identityAnnotations, int numWords) {
        Integer wordLength=15;
        Integer maxWeight=10;
        Random randomGenerator = new Random();
        for (int i=0;i<numWords; i++){
           String word=generateRandomWord(wordLength);
           Double weight = 1.0*(randomGenerator.nextInt(maxWeight-1) +1);
           identityAnnotations.put(word,weight);
            
       }
    }

    
    private String generateRandomWord(Integer wordLength) {

        Random randomGenerator= new Random();
        StringBuffer sb = new StringBuffer(wordLength);
        int c = 'A';
        int r1 = 0;

        for (int i = 0; i < wordLength; i++) {
            r1 = (int) (randomGenerator.nextInt(3));
            switch (r1) {
                case 0:
                    c = '0' + randomGenerator.nextInt(10);
                    break;
                case 1:
                    c = 'a' + randomGenerator.nextInt(26);
                    break;
                case 2:
                    c = 'A' + randomGenerator.nextInt(26);
                    break;
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

    private void loadWithWordsFromOtherMap(HashMap<String, Double> mapOne, HashMap<String, Double> mapTwo, Integer numberOfAdditions) {
      
            for (String key : mapTwo.keySet()) {
                try {
                    mapOne.put(key, mapTwo.get(key));
                    numberOfAdditions--;
                  } catch (Exception e) {
                    e.printStackTrace();
                    }
                if (numberOfAdditions < 0){
                    break;
                }




      

        }
    }

    private void loadWithWordsFromOtherMapInteger(HashMap<Integer, Integer> mapOne, HashMap<Integer, Integer> mapTwo, int numberOfAdditions) {
        for (Integer key : mapTwo.keySet()) {
            try {
                mapOne.put(key, mapTwo.get(key));
                numberOfAdditions--;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (numberOfAdditions < 0) {
                break;
            }

        }
    }

    private void loadWithRandomWordsInteger(HashMap<Integer, Integer> identityAnnotations, int numWords) {
        Integer wordLength = 10000000;
        Integer maxWeight = 10;
        Random randomGenerator = new Random();
        for (int i = 0; i < numWords; i++) {
            Integer word = randomGenerator.nextInt(wordLength - 1) + 1;
            Integer weight = randomGenerator.nextInt(maxWeight - 1) + 1;
            identityAnnotations.put(word, weight);
        }
    }
}
