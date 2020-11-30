package src.microblogproject;

import java.util.*;

/**
 * OVERVIEW:
 * Social Network è un tipo di dato mutabile che caratterizza una rete sociale
 * 
 * TYPICAL ELEMENT:
 *  <socialNetwork, postFollowersTags, topInfluencers> :
 *      - socialNetwork : {user_0, ...} ==> { {a_0, ...}, {b_0, ...}, ...}
 *          socialNetwork(user) = {a : user segue a}
 * 
 *      - postFollowersTags : {post_0, ...} ==> { ({aa_0, ...}, {bb_0, ...}), ({aa_1, ...}, {bb_1, ..}), ...}
 *          postFollowersTags(p) = { (aa, bb) : aa ha messo like/segue al post p && bb è stato menzionato/taggato nel post p}
 * 
 *      - topInfluencers : {user_0, ...} ==> {a, b, c, ...}
 *          topInfluencers(user) = a : a è il numero di persone che hanno messo like ai post di user
 *       
 *          
 */

public interface SocialNetworkInterface {
    /**
     * 
     * METODI RICHIESTI
     * 
     */
    /**
     * --> Ritorna la rete sociale derivante dalla lista di post nel parametro
     * 
     * REQUIRES: ps != null && !ps.contains(null) && 
     *      (for all pst in ps ==> postFollowersTags.keySet().contains(pst)) &&
     *      (for all pst in ps ==> socialNetwork.keySet().contains(pst.getAuthor))
     * 
     * THROWS: ps == null || ps.contains(null) lancia NullPointerException(unchecked) || 
     *      (for all pst in ps ==> !postFollowersTags.keySet().contains(pst) lancia NoPostInNetworkException(unchecked)) ||
     *      (for all pst in ps ==> !socialNetwork.keySet().contains(pst.getAuthor) lancia UserNotInSocialNetworkException(unchecked))
     * 
     * EFFECTS: restituisce la rete sociale derivata dalla lista di post (parametro del metodo)
     *          <AutoreA post in ps, Autori di post a cui ha messo like AutoreA>
     * 
     * @param ps lista post da cui costruire la rete sociale
     */
    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws NoPostInNetworkException,
     UserNotInSocialNetworkException;

    /**
     * --> Classifica degli utenti in base al numero di likes per i loro posts [User,...,User]
     * 
     * EFFECTS: Ritorna la lista degli utenti nella rete sociale ordinata 
     *          in ordine di numero di followers decrescente 
     */
    public List<String> influencers();

    /** --> Restituisce l’insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
     * 
     * EFFECTS: Ritorna un insieme(privo di duplicati) di utenti che sono stati menzionati almeno una volta nella rete sociale 
     * @throws UserNotInSocialNetworkException
     * @throws NoPostInNetworkException
     */
    public Set<String> getMentionedUsers() throws NoPostInNetworkException, UserNotInSocialNetworkException;


    /**
     * --> Restituisce l’insieme degli utenti menzionati (inclusi) nella lista di post
     * 
     * REQUIRES: ps != null && !ps.contains(null) && 
     *      (for all pst in ps ==> postFollowersTags.keySet().contains(pst)) &&
     *      (for all pst in ps ==> socialNetwork.keySet().contains(pst.getAuthor)) 
     * 
     * THROWS: ps == null && ps.contains(null) lancia NullPointerException(unchecked) || 
     *      (for all pst in ps ==> !postFollowersTags.keySet().contains(pst) lancia NoPostInNetworkException(unchecked)) ||
     *      (for all pst in ps ==> !socialNetwork.keySet().contains(pst.getAuthor) lancia UserNotInSocialNetworkException(unchecked))
     * 
     * EFFECTS: Ritorna un insieme(privo di duplicati) di utenti che sono stati menzionati almeno una volta nei post dati
     *      nei parametri.
     * 
     * @param ps lista post da cui ottenere gli utenti menzionati
     */
    public Set<String> getMentionedUsers(List<Post> ps) throws NoPostInNetworkException,
     UserNotInSocialNetworkException;

    /**
     * --> Restituisce la lista dei post effettuati dall’utente nella rete sociale il cui nome è dato dal parametro username
     * 
     * REQUIRES: username != null && socialNetwork.keySet().contains(username)
     * 
     * THROWS: username == null lancia NullPointerException(unchecked) ||
     *         !socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException(unchecked)
     * 
     * EFFECTS: Ritorna la lista di post caricati da username
     * 
     * @param username autore dei post di ritorno
     */
    public List<Post> writtenBy(String username) throws UserNotInSocialNetworkException;


    /**
     * --> Restituisce la lista dei post effettuati dall’utente il cui nome è dato dal parametro username presenti nella lista ps
     * 
     * REQUIRES: ps != null && !ps.contains(null) && 
     *      (for all pst in ps ==> postFollowersTags.keySet().contains(pst)) &&
     *      (for all pst in ps ==> socialNetwork.keySet().contains(pst.getAuthor))
     *      username != null && socialNetwork.keySet().contains(username)
     * 
     * THROWS: ps == null || ps.contains(null) lancia NullPointerException(unchecked) || 
     *      (for all pst in ps ==> !postFollowersTags.keySet().contains(pst) lancia NoPostInNetworkException(unchecked)) ||
     *      (for all pst in ps ==> !socialNetwork.keySet().contains(pst.getAuthor) lancia UserNotInSocialNetworkException(unchecked)) ||
     *      username == null lancia NullPointerException(unchecked) || !socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException(unchecked)
     *       
     * EFFECTS: Restituisce la lista di post di ps che sono stati scritti da username
     */
    public List<Post> writtenBy(List<Post> ps, String username) throws UserNotInSocialNetworkException,
     UserNotInSocialNetworkException;

    /**
     * --> Restituisce la lista dei post presenti nella rete sociale che includono
     *     almeno una delle parole presenti nella lista delle parole argomento del metodo
     * 
     * REQUIRES: words != null && !words.contains(null)
     * 
     * THROWS: words == null || words.contains(null) lancia NullPointerException
     * 
     * EFFECTS: Ritorna la lista dei post in socialNetwork che hanno nel testo almeno una
     *          delle parole presenti nella lista words data in parametro
     */
    public List<Post> containing(List<String> words);

    /**
     * --> Dato username e id del post, questa funzione fa in modo che username metta like
     *     al post identificato con il postID.
     * 
     * REQUIRES: username != null && postID != null && socialNetwork.keySet().contains(username) &&
     *          validationCode != false && 
     *          if (currentPost.getKey().getID().equals(postID)) ==> 
     *          currentPost.getKey().getAuthor().equals(username) != true
     * 
     * THROWS: username == null || postID == null lanciano NullPointerException || 
     *          !socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException ||
     *          validationCode == false lancia NoPostInNetworkException(unchecked) || 
     *          if (currentPost.getKey().getID().equals(postID)) ==> 
     *          currentPost.getKey().getAuthor().equals(username) == true lancia IllegalFollowException(unchecked)
     * 
     * MODIFIES: socialNetwork, postFollowersTags, topInfluencers
     * 
     * EFFECTS: Aggiunge in socialNetwork.get(username) l'autore del post se e solo se è la prima volta che lo si inserisce
     *          inoltre, in questo caso, si incrementa di 1 anche il numero di followers associato a username.
     *          In ogni caso aggiunge a postFollowersTags.get(post with postID).get(0) (lista dei like)
     *          username (username ha messo like a post with postID).
     */
    public void addFollower(String username, UUID postID) throws UserNotInSocialNetworkException,
     NoPostInNetworkException, IllegalFollowException;

    /**
     * --> Dato username e id del post, questa funzione fa in modo che username possa togliere like
     *     al post identificato con il postID.
     * 
     * REQUIRES: username != null && postID != null && socialNetwork.keySet().contains(username) &&
     *          validationCode != false && 
     *          if (currentPost.getKey().getID().equals(postID)) ==> 
     *              (currentPost.getKey().getAuthor().equals(username) != true &&
     *              currentPost.getValue().get(0).contains(username) != false)
     * 
     * THROWS: username == null || postID == null lanciano NullPointerException || 
     *          !socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException ||
     *          validationCode == false lancia NoPostInNetworkException(unchecked) || 
     *          for all currentPost in postFollowersTags 
     *              if (currentPost.getKey().getID().equals(postID)) ==> 
     *                  (currentPost.getKey().getAuthor().equals(username) == true lancia IllegalFollowException(unchecked) ||
     *                  currentPost.getValue().get(0).contains(username) == false lancia NoFollowerInPostException)
     * 
     * MODIFIES: socialNetwork, postFollowersTags, topInfluencers
     * 
     * EFFECTS: Rimuove in socialNetwork.get(username) l'autore del post se e solo se è l'unico like che username aveva messo a postAuthor.
     *          In ogni caso rimuove da postFollowersTags.get(post with postID).get(0) (lista dei like) username.
     *          
     */
    public void removeFollowerFromPost(String username, UUID postID) throws UserNotInSocialNetworkException,
     NoPostInNetworkException, IllegalFollowException, NoFollowerInPostException;

    /**
     * --> Elimina il post indicato nel parametro dalla rete sociale
     * 
     * REQUIRES: post != null && postFollowersTags.keySet().contains(post)
     * 
     * THROWS: post == null lancia NullPointerException ||
     *         !postFollowersTags.keySet().contains(post) lancia NoPostInNetworkException
     * 
     * MODIFIES: socialNetwork, postFollowersTags, topInfluencers
     * 
     * EFFECTS: Elimina il post, dunque elimina ove necessario le relazioni fra due utenti
     *          legati solamente dal like di uno dei due ad un post dell'altro. (Vedi metodo removeFollowerFromPost())
     * @throws NoFollowerInPostException
     * @throws IllegalFollowException
     * @throws UserNotInSocialNetworkException
     */
    public void deletePost(Post post) throws NoPostInNetworkException, UserNotInSocialNetworkException, IllegalFollowException, NoFollowerInPostException;

    /**
     * --> Aggiunge una menzione in un post diretta ad un altro utente presente nella rete sociale
     * 
     * REQUIRES: tagUser != null && postID != null && 
     *           socialNetwork.keySet().contains(tagUser) &&
     *           for all currentPost in postFollowersTags 
     *              if (currentPost.getKey().getID().equals(postID)) ==>
     *                  (currentPost.getKey().getAuthor().equals(tagUser) != true) &&
     *           it has to return in some way through NullPointerException or IllegalTagException or UserNotInNetworkException
     *          
     * THROWS: tagUser == null || postID == null lancia NullPointerException(unchecked) || 
     *           !socialNetwork.keySet().contains(tagUser) lancia UserNotInSocialNetworkException(unchecked) ||
     *           for all currentPost in postFollowersTags 
     *              if (currentPost.getKey().getID().equals(postID)) ==>
     *                  (currentPost.getKey().getAuthor().equals(tagUser) == true) lancia IllegalTagException(unchecked) ||
     *           if for all currentPost in PostFollowersTags terminate and function still not finished
     *              lancia NoPostInNetworkException(unchecked)
     * 
     * MODIFIES: postFollowersTags        
     * 
     * EFFECTS: Entra in postFollowersTags.get(post with postID).get(1) (Set dei menzionati in post) e aggiunge tagUser
     */
    public void addTagOnPost(String tagUser, UUID postID) throws UserNotInSocialNetworkException,
     NoPostInNetworkException, IllegalTagException;

    /**
     * --> Aggiunge un post nella rete sociale
     * 
     * REQUIRES: postToAdd != null && postToAdd.getAuthor() != null && postToAdd.getText() != null &&
     *           socialNetwork.keySet().contains(postToAdd.getAuthor()) &&
     *           0 < postToAdd.getText().length <= 140
     * 
     * THROWS: postToAdd == null lancia NullPointerException(unchecked) ||
     *           !socialNetwork.keySet().contains(postToAdd.getAuthor()) lancia UserNotInSocialNetworkException(unchecked) ||
     *           postToAdd.getText().length > 140 lancia TextTooLongException(unchecked) ||
     *           postToAdd.getText().length == 0 lancia EmptyPostException(unchecked)
     * 
     * MODIFIES: postFollowersTags
     * 
     * EFFECTS: Ogni volta che si vuole aggiungere un post nella rete sociale alloca lo spazio per 
     *          i set dei likes e dei tags per post. Una volta fatto questo aggiunge il post in postFollowersTags
     *          inserendoci il postToAdd come chiave e come valore i due set.
     * @throws BadWordsException
     * @throws PostAlreadyExistsException
     * @throws EmptyPostException
     * @throws TextTooLongException
     */
    public void addPost(Post postToAdd) throws UserNotInSocialNetworkException, PostAlreadyExistsException, BadWordsException, TextTooLongException, EmptyPostException;

    /**
     * --> Aggiunge un utente nella rete sociale
     * 
     * REQUIRES: username != null && !socialNetwork.keySet().contains(username)
     * 
     * THROWS: username == null lancia NullPointerException(unchecked) ||
     *         socialNetwork.keySet().contains(username) lancia UserAlreadyExistsExcetpion(unchecked)
     * 
     * MODIFIES: socialNetwork, topInfluencers
     * 
     * EFFECTS: Aggiunge a socialNetwork <username, Set di String allocato> e
     *          inserisce username in topInfluencers con 0 follower.
     * @throws UsernameNotAllowedException
     *         
     */
    public void addUser(String username) throws UserAlreadyExistsException, UsernameNotAllowedException;

    /**
     * --> Rimuove un utente dalla rete sociale
     * 
     * REQUIRES: username != null && socialNetwork.keySet().contains(username)
     * 
     * THROWS: username == null lancia NullPointerException(unchecked) ||
     *         !socialNetwork.keySet().contains(username) lancia UserNotInSocialNetworkException
     * 
     * MODIFIES: socialNetwork, postFollowersTags, topInfluencers
     * 
     * EFFECTS: Elimina tutte le occorrenze dell'utente da eliminare:
     *          sia in social network che in postFollowers che in influencers.
     */
    public void removeUser(String username) throws UserNotInSocialNetworkException;

    /**
     * --> Scrive in console tutti i post pubblicati dagli utenti appartenenti alla rete sociale
     * 
     * REQUIRES: postFollowersTags.isEmpty() != true
     * 
     * THROWS: postFollowersTags.isEmpty() == true lancia EmptyPostsException
     * 
     * EFFECTS: Stampa in console tutte le occorrenze di post in postFollowersTags.
     */
    public void printAllPosts() throws EmptyPostsException;

    /**
     * --> Scrive in console tutti gli utenti del socialNetwork insiema alla lista di persone che segue l'utente che occorre
     * 
     * REQUIRES: socialNetwork.isEmpty() != true
     * 
     * THROWS: socialNetwork.isEmpty() == true lancia EmptyNetworkException
     * 
     * EFFECTS: Stampa in console tutte le occorrenze di utenti in socialNetwork e le loro rispettive liste
     *        di following.
     * @throws EmptyPostsException
     */
    public void printNetwork() throws EmptyNetworkException, EmptyPostsException;

}
