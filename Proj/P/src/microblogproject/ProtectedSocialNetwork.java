package src.microblogproject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProtectedSocialNetwork extends SocialNetwork implements ProtectedSocialNetworkInterface {

    /**
     * ABSTRACTION FUNCTION:
     *  f(obj) : O -> A
     *      
     *      <socialNetwork, postFollowersTags, topInfluencers, badWords> :
     *          - socialNetwork : String --> Set<String> :
     *              - O = obj.socialNetwork.keySet()
     *              - A = obj.socialNetwork.values()
     *              - for all user in O ==> socialNetwork(user) = obj.socialNetwork.get(user)
     * 
     *          - postFollowersTags : Post --> List<SortedSet<String>> :
     *              - O = obj.postFollowersTags.keySet()
     *              - A = obj.postFollowersTags.values()
     *              - for all ps in O ==> postFollowersTags(ps) = obj.postFollowersTags.get(ps)
     *              - for all lst in postFollowersTags(ps) ==> lst.get(0) as likes of ps; lst.get(1) as tags of ps
     * 
     *          - topInfluencers : String --> Integer :
     *              - O = obj.topInfluencers.keySet()
     *              - A = obj.topInfluencers.values()
     *              - for all user in O ==> topInfluencers(user) = obj.topInfluencers.get(user)
     * 
     *          - badWords : Set<String> :
     *              O = {0, ..., dimensionObtained - 1}
     *              A = obj.badWords
     *              for all index in O ==> badWords.get(index) = obj.badWords.get(index) as String
     * 
     * REPRESENTATION INVARIANT:
     * f(obj): O -> Bool =
     *      
     *      super.f(obj) && this != null &&
     * 
     *      // L'attributo badWords non può essere null
     *      badWords != null &&
     * 
     *      // Ogni valore in badWords non può null
     *      for all word in badWords ==> word != null
     * 
     */

    private static final int REPORT_PROPERTY_DIMENSION = 2;
    private Set<String> badWords;

    public ProtectedSocialNetwork() throws Exception {
        super();

        // Inizializzo badWords
        badWords = new TreeSet<String>();

        // Ottengo parole cattive da JSON
        Object parser = new JSONParser().parse(new FileReader("resources/not_allowed_words.json"));
        
        
        // Typecasting parser --> JSONObject
        JSONObject parserJSON = (JSONObject) parser;
        
        Long dimensionObtained = (Long) parserJSON.get("dim");

        int i;

        for (i = 0; i < dimensionObtained.intValue(); i++) {
            String wordObtained = (String) parserJSON.get(String.valueOf(i));
            badWords.add(wordObtained);
        }

        // Ora badWords contiene tutte le parole non ammesse
    }

    // TESTATO
    // Estendo l'operazione di aggiunta di un post, controllando che non ci siano parole offensive
    @Override
    public void addPost(Post postToAdd) throws BadWordsException {
        
        if (postToAdd == null) throw new NullPointerException("Il parametro postToAdd non può essere null");

        for (String badWord : this.badWords) {
            if (postToAdd.getText().contains(badWord)) {
                throw new BadWordsException("Questo post non può essere aggiunto perché possiede contenuto offensivo");
            }
        }

        try {
            super.addPost(postToAdd);
        } catch (UserNotInSocialNetworkException | TextTooLongException | EmptyPostException e) {
            System.out.println(e);
        }
    }

    // TESTATO
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

     * EFFECTS: aggiunge al json posts_to_control.json "idToReport" : [ "degree" : dangerDegree, "motivation" : motivation]
     */
    public void addDangerousReport(String username, UUID idToReport, Integer dangerDegree, String motivation) throws UserNotInSocialNetworkException,
     NoPostInNetworkException, FileNotFoundException, IllegalFollowException {

        if (username == null) throw new NullPointerException("Il parametro username deve essere esplicitato");
        if (idToReport == null) throw new NullPointerException("Il parametro postID deve essere esplicitato");        
        if (dangerDegree == null) throw new NullPointerException("Il parametro dangerDegree deve essere esplicitato");
        if (motivation == null) throw new NullPointerException("Il parametro motivation deve essere esplicitato");

        if (!super.isUser(username)) throw new UserNotInSocialNetworkException(username);

        if (super.containsPostWithID(idToReport) == null) throw new NoPostInNetworkException();

        if (super.containsPostWithID(idToReport) == username) throw new IllegalFollowException(username);

        // Scrivo la segnalazione in posts_to_control.json
        // creo un oggetto JSON
        JSONObject jsonObj = new JSONObject();
        
        // Aggiungo il postSegnalato con il grado di pericolo e la motivazione
        Map<String, String> newReport = new LinkedHashMap<>(REPORT_PROPERTY_DIMENSION);

        newReport.put("degree", String.valueOf(dangerDegree));
        newReport.put("motivation", motivation);

        jsonObj.put(idToReport.toString(), newReport);

        PrintWriter pw = new PrintWriter("resources/posts_to_control.json");
        
        pw.write(jsonObj.toJSONString());
        pw.flush();
        pw.close();
    }

}
