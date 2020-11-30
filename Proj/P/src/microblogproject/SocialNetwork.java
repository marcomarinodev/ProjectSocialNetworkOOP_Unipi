package src.microblogproject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialNetwork implements SocialNetworkInterface {

    /* ABSTRACTION FUNCTION:
     *  f(obj) : O -> A
     *      
     *      <socialNetwork, postFollowersTags, topInfluencers> :
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
     *  REPRESENTATION INVARIANT:
     *  f(obj): O -> Bool =
     * 
     *      this != null &&
     * 
     *      // ***** CONTROLLO SULLE MAPS
     *      // --- Nessuna map può essere uguale a null
     *      socialNetwork != null &&
     *      postFollowersTags != null && 
     *      topInfluencers != null &&
     * 
     *      // ***** CONTROLLI NELLE MAPS
     *      // --- Nè le chiavi, nè i valori di una map possono essere null
     *      !socialNetwork.keySet().contains(null) && !socialNetwork.values().contains(null) &&
     *      !postFollowersTags.keySet().contains(null) && !postFollowersTags.values().contains(null) &&
     *      !topInfluencers.keySet().conatins(null) && !topInflunecers.values().contains(null) &&
     *      
     *      // ***** CONTROLLI SU socialNetwork
     *      // --- I Set<String> come valori nella mappa socialNetwork non possono essere null
     *      (for all user as String in socialNetwork.keySet() ==> !socialNetwork.get(user).contains(null)) &&
     *      
     *      // --- Un utente non può seguire un suo post
     *      (for all user as String in socialNetwork.keySet() ==> !socialNetwork.get(user).contains(user)) &&
     * 
     *      // --- Ogni utente che viene seguito da un altro utente deve appartenere alla rete sociale
     *      (for all user as String in socialNetwork.keySet() ==> 
     *          for all following as String in socialNetwork.get(user) ==> socialNetwork.keySet.contains(following)) &&
     * 
     *      // ***** CONTROLLI SU postFollowersTags
     *      // --- Le liste presenti nei valori di postFollowersTags non possono essere null
     *      // --- e devono avere dimensione 2
     *      (for all p as Post in postFollowersTags.keySet() ==> postFollowersTags.get(p) != null && 
     *          postFollowersTags.get(p).size() == 2) &&
     *      
     *      // --- I Set nelle liste presenti nei valori di postFollowersTags non possono essere null
     *      // --- e nessun elemento dei Set può essere null
     *      (for all p as Post in postFollowersTags.keySet() ==> !postFollowersTags.get(p).contains(null) &&
     *          !postFollowersTags.get(p).get(0).contains(null) &&
     *          !postFollowersTags.get(p).get(1).contains(null)
     *      ) &&
     * 
     *      // --- Un utente non può nè seguire un suo post nè menzionare se stesso in un suo post
     *      (for all p as Post in postFollowersTags.keySet() ==> !postFollowersTags.get(p).get(0).contains(p.getAuthor()) &&
     *          !postFollowersTags.get(p).get(1).contains(p.getAuthor())
     *      ) &&
     * 
     *      // --- Un utente che mette like ad un post (quindi è un follower del post) e/o che è stato menzionato 
     *      // --- deve essere utente che appartiene alla rete sociale
     *      (for all p as Post in postFollowersTags.keySet() ==> 
     *          for all follower as String in postFollowersTags.get(p).get(0) ==>
     *              socialNetwork.keySet().contains(follower) && 
     *          for all tag as String in postFollowersTags.get(p).get(1) ==>
     *              socialNetwork.keySet().contains(tag)
     *      ) && 
     * 
     *      // --- Ogni post deve essere scritto da un utente appartenente alla rete sociale
     *      (for all p as Post in postFollowersTags.keySet() ==> socialNetwork.keySet().contains(p.getAuthor())) &&
     *      
     *      // --- Ogni post deve avere un messaggio che deve avere numero caratteri n: 0 < n <= 140
     *      (for all p as Post in postFollowersTags.keySet() ==> p.getText().length() > 0 && p.getText().length() <= 140) &&
     *      
     *      // --- Ogni post deve avere un univoco id (garantito dalla proprietà della classe UUID)
     *      (for all p as Post in postFollowersTags.keySet() ==> 
     *          for all q as Post in postFollowersTags.keySet() except p ==>
     *              q.getID() != p.getID()) &&     
     * 
     *      // ***** CONTROLLO SU topInfluencers
     *      // --- Ogni chiave della mappa deve essere un utente appartenente alla rete sociale
     *      (for all user as String in topInfluencers.keySet() ==> socialNetwork.keySet().contains(user)) &&
     *      
    */


    protected Map<String, Set<String>> socialNetwork; // <Utente A, Persone che segue A>

    // Questa mappa associa un Post ad un insieme di stringhe che rappresentano i seguaci
    // di quel post e di conseguenza dell'autore del post e un insieme di stringhe che rappresentano
    // i tags di quel post
    protected Map<Post, List<SortedSet<String>>> postFollowersTags;
    protected Map<String, Integer> topInfluencers; 

    public SocialNetwork() {
        socialNetwork = new TreeMap<String, Set<String>>();
        postFollowersTags = new HashMap<Post, List<SortedSet<String>>>();
        topInfluencers = new TreeMap<String, Integer>();
    }

    // Aggiunge un follower in un post presente nella lista ps
    // REQUIRES: ps != null AND socialNetwork != null AND username belongs to socialNetwork AND postID occurs exactly once in
    // every post in ps AND the username who wants to follow is not the author of the post.
    // THROWS: IllegalArgumentException()
    
    // TESTATO
    @Override
    public void addFollower(String username, UUID postID) throws  UserNotInSocialNetworkException,
     NoPostInNetworkException, IllegalFollowException {

        if (username == null) throw new NullPointerException("Il parametro username deve essere esplicitato");
        if (postID == null) throw new NullPointerException("Il parametro postID deve essere esplicitato");        

        if (!this.isUser(username)) throw new UserNotInSocialNetworkException(username);
        
        // 1 success - 2 postID not in ps - 3 username == author
        Boolean validationCode = false;
        String postAuthor = "";

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            // System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            if (currentPost.getKey().getID().equals(postID)) {
                if (!currentPost.getKey().getAuthor().equals(username)) {
                    postAuthor = currentPost.getKey().getAuthor(); // mi serve per aggiornare socialNetwork
                    currentPost.getValue().get(0).add(username); // In questo caso aggiungo username tra i followers

                    validationCode = true; // success
                    break;
                }
                throw new IllegalFollowException(username);
            }
        }

        if (!validationCode) throw new NoPostInNetworkException();
        
        // Aggiungo: <username, <"x",...+,"author">>
        // conosco author, conosco username

        // Se tra le persone che segue username non è presente postAuthor ==> aggiungi e incrementa il numero di followers
        if (!this.socialNetwork.get(username).contains(postAuthor)) {
            this.socialNetwork.get(username).add(postAuthor);
            this.topInfluencers.put(postAuthor, this.topInfluencers.get(postAuthor) + 1); 
        }

    }

    // TESTATO
    @Override
    public void removeFollowerFromPost(String username, UUID postID) throws UserNotInSocialNetworkException,
     NoPostInNetworkException, IllegalFollowException, NoFollowerInPostException {

        if (username == null) throw new NullPointerException("Il parametro username deve essere esplicitato");
        if (postID == null) throw new NullPointerException("Il parametro postID deve essere esplicitato");    

        if (!this.isUser(username)) throw new UserNotInSocialNetworkException(username);
        
        // 1 success - 2 postID not in ps - 3 username == author
        Boolean validationCode = false;
        String postAuthor = "";

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            
            if (currentPost.getKey().getID().equals(postID)) {
                if (!currentPost.getKey().getAuthor().equals(username)) {
                    postAuthor = currentPost.getKey().getAuthor(); 

                    // Controllo prima che il username abbia messo like a questo post
                    if (currentPost.getValue().get(0).contains(username)) {
                        currentPost.getValue().get(0).remove(username); 
                    } else {
                        throw new NoFollowerInPostException(username);
                    }

                    validationCode = true; // success
                    break;
                }
                throw new IllegalFollowException(username);
            }
        }

        if (!validationCode) throw new NoPostInNetworkException();
        
        // Controllo che in postAuthor ci sia almeno una occorrenza di username
        Boolean followerOccurs = false;

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            if (currentPost.getKey().getAuthor().equals(postAuthor)) {
                if (currentPost.getValue().get(0).contains(username)) followerOccurs = true;
                break;
            }
        }

        // Tolgo: <username, <"x",...+ \"author">>
        // conosco author, conosco username

        // Se il like di username era l'unico => username non segue più postAuthor
        if (!followerOccurs) {
            this.socialNetwork.get(username).remove(postAuthor);
            this.topInfluencers.put(postAuthor, this.topInfluencers.get(postAuthor) - 1); 
        }
        
        
    }

    // TESTATO
    // Funzione che elimina un post
    @Override
    public void deletePost(Post post) throws NoPostInNetworkException, UserNotInSocialNetworkException,
            IllegalFollowException, NoFollowerInPostException {

        if (post == null) throw new NullPointerException("Il parametro post non può essere null");

        if (!this.containsPost(post)) throw new NoPostInNetworkException();

        Set<String> followersToRemoveFromPost = new TreeSet<String>();

        for (String followerToRemove : this.postFollowersTags.get(post).get(0)) {
            followersToRemoveFromPost.add(followerToRemove);
        }

        for (String toRemove : followersToRemoveFromPost) {
            this.removeFollowerFromPost(toRemove, post.getID()); // per ogni followers del post bisogna fare removeFollowerFromPost
        }

        // ora elimino il post da followers
        this.postFollowersTags.remove(post);
    }

    // TESTATO
    @Override
    public void addTagOnPost(String tagUser, UUID postID) throws UserNotInSocialNetworkException, NoPostInNetworkException, IllegalTagException {
        
        if (tagUser == null) throw new NullPointerException("Il parametro tagUser non può essere null");
        
        if (postID == null) throw new NullPointerException("Il parametro postID non può essere null");

        if (!this.isUser(tagUser)) throw new UserNotInSocialNetworkException(tagUser);

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            if (currentPost.getKey().getID().equals(postID)) {
                if (!currentPost.getKey().getAuthor().equals(tagUser)) {
                    currentPost.getValue().get(1).add(tagUser);
                    return;
                } else throw new IllegalTagException(tagUser);
            }
        }

        throw new NoPostInNetworkException();
    }

    // TESTATO
    @Override
    public void addPost(Post postToAdd) throws UserNotInSocialNetworkException, PostAlreadyExistsException, BadWordsException,
            TextTooLongException, EmptyPostException {
        
        if (postToAdd == null) throw new NullPointerException("Il parametro postToAdd non può essere null");
        
        if (containsPostWithID(postToAdd.getID()) != null) throw new PostAlreadyExistsException("Il post è già presente nella rete");

        if (!this.isUser(postToAdd.getAuthor())) throw new UserNotInSocialNetworkException(postToAdd.getAuthor());
        
        checkText(postToAdd.getText());

        SortedSet<String> initLikesSetStrings = new TreeSet<String>();
        SortedSet<String> initTagsSetStrings = new TreeSet<String>();
        List<SortedSet<String>> initLikesFollowers = new ArrayList<SortedSet<String>>();

        initLikesFollowers.add(initLikesSetStrings); // Inizializzo la prima lista (likes) (0)
        initLikesFollowers.add(initTagsSetStrings); // Inizializzo la seconda lista (followers) (1)

        this.postFollowersTags.put(postToAdd, initLikesFollowers);
    }

    // TESTATO
    /**
     * REQUIRES: txt.length() <= 140 && txt.length() > 0
     * 
     * THROWS: txt.length() > 140 && txt.length() == 0 lancia rispettivamente TextTooLongException e EmptyPostException 
     * 
     * EFFECTS: fa in modo che il testo inserito nel post non sia nè vuoto nè oltre 140 caratteri
     */
    public void checkText(String txt) throws TextTooLongException, EmptyPostException {
        // Sono sicuro che username non è null perché lo controlla il costruttore
  
        if (txt.length() > 140) throw new TextTooLongException();
        if (txt.length() == 0) throw new EmptyPostException("Il post non può essere vuoto");
    }

    // TESTATO
    @Override
    public void addUser(String username) throws UserAlreadyExistsException, UsernameNotAllowedException {
        
        if (username == null) throw new NullPointerException("Il parametro username non può essere null.");

        if (!checkUsername(username)) throw new UsernameNotAllowedException("Il nome utente non è valido");

        if (this.isUser(username)) throw new UserAlreadyExistsException(username);

        // In questa maniera quando devo aggiungere un nuovo utente, alloco lo spazio
        // per inserire altri utenti che segue nella rete sociale.
        Set<String> followingUsers = new TreeSet<String>(); 

        this.socialNetwork.put(username, followingUsers);
        this.topInfluencers.put(username, 0);
    }

    // TESTATO
    /**
    * EFFECTS: ritorna vero se i caratteri di username rientrano in quelli del regex.
    *          Il primo carattere deve iniziare con una lettera dell'alfabeto [A,...,Z,a,...z].
    *          {dai 6 ai 30 caratteri per nome utente}.
    */
    public boolean checkUsername(String username) {
        // Sono sicuro che username non è null perché lo controlla il costruttore
        // Regex per controllare se l'username è valido
        String regex = "^[A-Za-z]\\w{5,29}$";
    
        // Il regex deve essere compilato
        Pattern p = Pattern.compile(regex);
    
        // Confronta la stringa con il regex
        Matcher m = p.matcher(username);
    
        return m.matches();
    }

    // TESTATO
    // Quando rimuovo un utente devo eliminare tutte le sue occorrenze:
    // sia in social network che in postFollowers che in influencers
    @Override
    public void removeUser(String username) throws UserNotInSocialNetworkException {
        
        if (username == null) throw new NullPointerException("Il parametro username non può essere null.");

        if (!this.isUser(username)) throw new UserNotInSocialNetworkException(username);

        // rimuovo l'utente dalla rete sociale
        this.socialNetwork.remove(username);
        // rimuovo l'utente dai following degli altri utenti della rete
        for (Map.Entry<String, Set<String>> currentUser : this.socialNetwork.entrySet()) {
            currentUser.getValue().remove(username);
        }

        // ora devo rimuovere i posts e i tags di username
        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            // Nel caso in cui l'autore del post sia quello da eliminare
            if (currentPost.getKey().getAuthor().equals(username)) {
                this.postFollowersTags.entrySet().remove(currentPost);
            } else {
                // Rimuovo l'occorrenza dell'utente da eliminare dalla lista dei likes e tags
                currentPost.getValue().get(0).remove(username);
                currentPost.getValue().get(1).remove(username);
            }
            
        }

        // Rimuovo dalla top list di utenti del social
        topInfluencers.remove(username);
    }

    // TESTATO
    /**
     * --> Controlla se l'utente username esiste nella rete sociale
     * 
     * REQUIRES: username != null
     * 
     * THROWS: username == null lancia NullPointerException
     * 
     * EFFECTS: Ritorna true se username è presente in socialNetwork, false altrimenti.
     */
    protected Boolean isUser(String username) {

        if (username == null) throw new NullPointerException("Il parametro username non può essere null");

        if (this.socialNetwork.containsKey(username) == false) {
            // System.out.println("L'autore " +  username +" non esiste");
            return false;
        }
        return true;
    }

    // TESTATO
    /**
     * --> Controlla che nella rete sociale ci sia il post indicato come parametro
     * 
     * REQUIRES: post != null
     * 
     * THROWS: post == null lancia NullPointerException
     * 
     * EFFECTS: Ritorna true se post è presente in postFollowersTags, false altrimenti.
     */
    protected Boolean containsPost(Post post) {
        
        if (post == null) throw new NullPointerException("Il parametro post non può essere null");

        if (!postFollowersTags.containsKey(post)) return false;
        return true;
    }

    // TESTATO
    /**
     * --> Controlla che nella rete sociale ci sia il post indicato dal suo ID preso nel parametro
     * 
     * REQUIRES: postID != null
     * 
     * THROWS: postID == null lancia NullPointerException
     * 
     * EFFECTS: 
     */
    protected String containsPostWithID(UUID postID) {
        if (postID == null) throw new NullPointerException("Il parametro postID non può essere null");

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            if (currentPost.getKey().getID().equals(postID)) return currentPost.getKey().getAuthor();
        }

        return null;
    }

    // TESTATO
    @Override
    public void printAllPosts() throws EmptyPostsException {
        if (postFollowersTags.isEmpty()) throw new EmptyPostsException();

        System.out.println("------- POSTS (FOR DEBUGGING PURPOSES) -------");

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost: this.postFollowersTags.entrySet()) {
            System.out.println("(P) CURRENT POST: ");
            System.out.println(currentPost.getKey().toString());
            if (currentPost.getValue() != null) {
                System.out.println("(*) POST'S FOLLOWERS:");
                for (String follower: currentPost.getValue().get(0)) {
                    System.out.println("    - Follower: " + follower);
                }
                System.out.println("(*) POST'S MENTIONED USERS:");
                for (String tag: currentPost.getValue().get(1)) {
                    System.out.println("    - Tag name: " + tag);
                }
            } else { // Questo branch non dovrebbe essere mai eseguito perché inizializziamo sempre i set likes e tags
                System.out.println("None");
            }
        }
    }

    // TESTATO
    @Override
    public void printNetwork() throws EmptyNetworkException, EmptyPostsException {
        if (socialNetwork.isEmpty()) throw new EmptyPostsException();

        System.out.println("------- SOCIAL NETWORK REPRESENTATION -------");

        System.out.println("(SN) N.USERS IN SOCIAL: " + this.socialNetwork.size());
        for (Map.Entry<String, Set<String>> currentUser : this.socialNetwork.entrySet()) {
            System.out.println("(*) CURRENT USER: " + currentUser.getKey());
            System.out.println("--> is following:");
            if (currentUser.getValue() != null) {
                for (String followingUser: currentUser.getValue()) {
                    System.out.println("    - " + followingUser);
                }
            } else { // Questo branch non dovrebbe essere mai eseguito perché inizializziamo sempre i followings
                System.out.println("None");
            }
        }
    } 

    // METODI RICHIESTI

    // TESTATO
    @Override
    public Map<String, Set<String>> guessFollowers(List<Post> ps) throws NoPostInNetworkException, UserNotInSocialNetworkException {

        if (ps == null) throw new NullPointerException("Il parametro ps non può essere null");
        if (ps.contains(null)) throw new NullPointerException("Il parametro ps non può avere valori null");

        Map<String, Set<String>> derivativeSN = new TreeMap<String, Set<String>>();

        for (Post currentPost : ps) {
            
            if (!this.containsPost(currentPost)) throw new NoPostInNetworkException(currentPost);
            if (!this.isUser(currentPost.getAuthor())) throw new UserNotInSocialNetworkException(currentPost.getAuthor());

            for (Map.Entry<Post, List<SortedSet<String>>> currentPostSN : this.postFollowersTags.entrySet()) {
                if (currentPostSN.getKey().equals(currentPost)) { // se abbiamo trovato un'occorrenza del post nel social
                    // in questo caso ne prendiamo i likes e per ogni like aggiungiamo l'autore nei following
                    for (String follower : currentPostSN.getValue().get(0)) {
                        derivativeSN.putIfAbsent(follower, new TreeSet<>());  // follower -> []
                        derivativeSN.get(follower).add(currentPost.getAuthor()); // follower -> autore
                    }
                    break;
                }
            }

            derivativeSN.putIfAbsent(currentPost.getAuthor(), new TreeSet<>());
            
        }

        return derivativeSN;
    }

    // TESTATO
    @Override
    public List<String> influencers() {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

        this.topInfluencers.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        
        return new ArrayList<String>(sortedMap.keySet());
    }

    public void printInf() {
        System.out.println(this.topInfluencers);
    }

    // TESTATO
    @Override
    public Set<String> getMentionedUsers() throws NoPostInNetworkException, UserNotInSocialNetworkException {
        Set<String> mentionedUsers = new HashSet<String>();
        List<Post> ps = new LinkedList<Post>();

        ps.addAll(this.postFollowersTags.keySet());

        mentionedUsers = getMentionedUsers(ps);

        return mentionedUsers;
    }

    // TESTATO
    @Override
    public Set<String> getMentionedUsers(List<Post> ps) throws NoPostInNetworkException, UserNotInSocialNetworkException {
        
        if (ps == null) throw new NullPointerException("Il parametro ps non può essere null");
        if (ps.contains(null)) throw new NullPointerException("Il parametro ps non può avere valori null");

        Set<String> mentionedUsers = new HashSet<String>();

        for (Post postParameter : ps) {

            if (!this.containsPost(postParameter)) throw new NoPostInNetworkException(postParameter);
            if (!this.isUser(postParameter.getAuthor())) throw new UserNotInSocialNetworkException(postParameter.getAuthor());

            for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
                // System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                if (currentPost.getKey().getID().equals(postParameter.getID())) {
                    mentionedUsers.addAll(currentPost.getValue().get(1));
                }
            }
        }

        return mentionedUsers;
    }

    // TESTATO
    @Override
    public List<Post> writtenBy(String username) throws UserNotInSocialNetworkException {
        
        if (username == null) throw new NullPointerException("Il parametro username non può essere null");
        if (!this.isUser(username)) throw new UserNotInSocialNetworkException();

        List<Post> postsOfUser = new LinkedList<Post>();

        for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
            if (currentPost.getKey().getAuthor().equals(username)) {
                postsOfUser.add(currentPost.getKey());
            }
        }

        return postsOfUser;
    }

    // TESTATO
    @Override
    public List<Post> writtenBy(List<Post> ps, String username) throws UserNotInSocialNetworkException, UserNotInSocialNetworkException {
        
        if (username == null) throw new NullPointerException("Il parametro username non può essere null");
        if (ps == null) throw new NullPointerException("Il parametro ps non può essere null");
        if (ps.contains(null)) throw new NullPointerException("Il parametro ps non può avere valori null"); 

        List<Post> postsOfUser = new LinkedList<Post>();

        for (Post postParameter : ps) {
            if (!this.isUser(username)) throw new UserNotInSocialNetworkException();
            if (!this.isUser(postParameter.getAuthor())) throw new UserNotInSocialNetworkException(postParameter.getAuthor());

            for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
                if (currentPost.getKey().getAuthor().equals(username) && currentPost.getKey().getID().equals(postParameter.getID())){
                    postsOfUser.add(currentPost.getKey());
                }
            } 
        }

        return postsOfUser;
    }

    // TESTATO
    @Override
    public List<Post> containing(List<String> words) {
        if (words == null) throw new NullPointerException("Il parametro words non può essere null");
        if (words.contains(null)) throw new NullPointerException("Il parametro words non può avere valori null");

        List<Post> postsOfUser = new LinkedList<Post>();
        
        for (String word: words) {
            for (Map.Entry<Post, List<SortedSet<String>>> currentPost : this.postFollowersTags.entrySet()) {
                if (currentPost.getKey().getText().indexOf(word) != -1) {
                    postsOfUser.add(currentPost.getKey());
                }
            } 
        }
        return postsOfUser;
    }
}