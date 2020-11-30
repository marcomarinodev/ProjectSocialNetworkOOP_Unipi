package src.microblogproject;

import java.io.FileNotFoundException;
import java.util.*;

public interface ProtectedSocialNetworkInterface extends SocialNetworkInterface {
    /**
     * OVERVIEW: 
     * ProtectedSocialNetwork è un tipo di dato mutabile che rappresenta una rete sociale
     * a partire dai metodi e attributi ereditati dalla classe SocialNetwork. 
     * In questa estensione si può controllare che il conenuto di un testo di un post
     * durante la sua pubblicazione (addPost) sia privo di parole cattive 
     * (che si possono reperire nel not_allowed_words.json).
     * Inoltre, si possono segnalare contenuti offensivi ed ogni segnalazione viene memorizzata
     * in posts_to_control.json.
     * 
     * TYPICAL ELEMENT:
     *  <socialNetwork, postFollowersTags, topInfluencers, badWords> :
     *      - socialNetwork : {user_0, ...} ==> { {a_0, ...}, {b_0, ...}, ...}
     *          socialNetwork(user) = {a : user segue a}
     * 
     *      - postFollowersTags : {post_0, ...} ==> { ({aa_0, ...}, {bb_0, ...}), ({aa_1, ...}, {bb_1, ..}), ...}
     *          postFollowersTags(p) = { (aa, bb) : aa ha messo like/segue al post p && bb è stato menzionato/taggato nel post p}
     * 
     *      - topInfluencers : {user_0, ...} ==> {a, b, c, ...}
     *          topInfluencers(user) = a : a è il numero di persone che hanno messo like ai post di user
     * 
     *      - badWords : {word1, ..., wordN}
     *          badWords(wordI) = str as String : str è una parola offensiva
     *               
     */


    /**
     * --> Aggiunge una segnalazione al post indicato con idToReport da parte di username
     *  con un grado di pericolo pari a dangerDegree e con una motivazione.
     * 
     * REQUIRES: username != null && idToReport != null && dangerDegre != null &&
     *          motivation != null && super.socialNetwork.keySet().contains(username) &&
     *          (for all pst in super.postFollowersTags.keySet() ==>
     *              ∃ pst.getID() == idToReport) &&
     *          (for all pst in super.postFollowersTags.keySet() ==>
     *              ∃ (pst.getID() == idToReport && pst.getAuthor() != username))
     * 
     * THROWS: se username == null || idToReport == null || dangerDegre == null ||
     *          motivation == null lancia NullPointerException
     *         se !super.socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException
     *         se (for all pst in super.postFollowersTags.keySet() ==> 
     *                                  ∄ pst.getID() == idToReport) lancia NoPostInNetworkException
     *         se (for all pst in super.postFollowersTags.keySet() ==>
     *              ∃ (pst.getID() == idToReport && pst.getAuthor() == username)) lancia IllegalFollowException
     *
     * 
     * EFFECTS: aggiunge al json posts_to_control.json "idToReport" : [ "degree" : dangerDegree, "motivation" : motivation]
     */
    public void addDangerousReport(String username, UUID idToReport, Integer dangerDegree, String motivation) throws UserNotInSocialNetworkException,
    NoPostInNetworkException, FileNotFoundException, IllegalFollowException;
}
