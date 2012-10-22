package eu.alertproject.iccs.socrates.calculator.api;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 9/27/12
 * Time: 11:30 PM
 */
public interface SimilarityComputationService {


    void computeSimilaritesForIdentity(String uuid);
    void computeSimilaritesForIssue(Integer id);
    void computeSimilarityForComponent(String component);
    void computeAllSimilarities();
//    void computeSimilaritiesForAllIdentities();
//    void computeSimilaritiesForAllIssues();
//    void computeSimilaritiesForAllComponents();


}
