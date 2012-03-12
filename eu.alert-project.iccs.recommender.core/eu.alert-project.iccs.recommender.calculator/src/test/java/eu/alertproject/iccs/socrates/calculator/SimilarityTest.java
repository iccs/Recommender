/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.socrates.calculator;

import java.util.HashMap;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedObject;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIssue;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedIdentity;
import eu.alertproject.iccs.socrates.calculator.internal.model.AnnotatedComment;
import eu.alertproject.iccs.socrates.calculator.internal.text.AnnotatedObjectSimilarity;
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
    }

    @After
    public void tearDown() {
    }


    @Test
    public void testIssueToIdentityZeroCommon() {
        try {

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


            AnnotatedObjectSimilarity annotatedObjectSimilarity = new AnnotatedObjectSimilarity(annotatedIssue, annotatedIdentity);
            annotatedObjectSimilarity.calculateSimilarity();
            Double similarity = annotatedObjectSimilarity.getSimilarity();
            System.out.println("Issue to identity similarity - zero common: " + similarity);
            assertNotNull(similarity);
            assertEquals(0.0, similarity, 0.0001);


        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Test
    public void testIssueToIdentity() {
        try {

            HashMap<String, Double> identityAnnotations = new HashMap<String, Double>();
            identityAnnotations.put("cat", 1.0);
            identityAnnotations.put("elephant", 2.0);
            identityAnnotations.put("dog", 0.5);
            annotatedIdentity = new AnnotatedIdentity("10938", identityAnnotations);


            HashMap<String, Double> issueAnnotations = new HashMap<String, Double>();
            issueAnnotations.put("cat", 1.0);
            issueAnnotations.put("lamp", 2.0);
            issueAnnotations.put("dog", 0.5);
            annotatedIssue = new AnnotatedIssue("1394", issueAnnotations);


            AnnotatedObjectSimilarity annotatedObjectSimilarity = new AnnotatedObjectSimilarity(annotatedIssue, annotatedIdentity);
            annotatedObjectSimilarity.calculateSimilarity();
            Double similarity = annotatedObjectSimilarity.getSimilarity();
            System.out.println("Issue to identity similarity: " + similarity);
            assertNotNull(similarity);
            assertEquals(0.23809523809523808, similarity, 0.0001);

        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    @Test
    public void testCommentToIdentity() {
        try {
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

            AnnotatedObjectSimilarity annotatedObjectSimilarity = new AnnotatedObjectSimilarity(annotatedComment, annotatedIdentity);
            annotatedObjectSimilarity.calculateSimilarity();
            Double similarity = annotatedObjectSimilarity.getSimilarity();
            System.out.println("Comment to identity similarity: " + similarity);
            assertNotNull(similarity);
            assertEquals(0.6546536707079771, similarity, 0.0001);
        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
