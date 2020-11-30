package src.microblogproject;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String args[]) throws Exception {        // BATTERIA DI TEST
        SocialNetwork SN = new ProtectedSocialNetwork();

        // TEST SULLA POPOLAZIONE DEL SOCIAL NETWORK E LE POSSIBILI ECCEZIONI
        try {
            SN.addUser("Mib000");
            SN.addUser("George");
            SN.addUser("Fabian");
            SN.addUser("Joseph");
            SN.addUser("Andrea");
            SN.addUser("TimCook");
            SN.addUser("UtenteDaEliminare");

            // Qui ci dovrebbe essere un errore. Mib000 è già un utente.
            //    System.out.println("{*} USerAlreadyExistsException {*}");
            //    SN.addUser("Mib000");
            SN.addUser("123Rossi"); // Username non accettabile
        } catch (UserAlreadyExistsException | UsernameNotAllowedException e) {
            System.out.println(e);
        }

        // TEST SULLA RIMOZIONE DI UTENTI
        try {
            SN.removeUser("UtenteDaEliminare");

            // Eccezione 
        //    SN.removeUser("Marco");
        } catch (UserNotInSocialNetworkException e) {
            System.out.println(e);
        }

        // TEST SULL'INSERIMENTO DEI POST E LE POSSIBILI ECCEZIONI
        // POST DA METTERE NEL SOCIAL NETWORK
        Post post1 = new Post("Mib000", "What a wonderful day!");
        Post post2 = new Post("Mib000", "Another day is gone.");
        Post post3 = new Post("Mib000", "Update is coming!");
        Post post4 = new Post("George", "This is my first post");
        Post post5 = new Post("Fabian", "What do you think about USA elections ?");
        Post post6 = new Post("Joseph", "First day at Microsoft. What a day!");
        Post post7 = new Post("Joseph", "VSCode editor is open source");
        Post post8 = new Post("Andrea", "New user here!");
        Post post9 = new Post("TimCook", "Apple M1 is the best processor on market");
        Post post0 = new Post("TimCook", "Questo post verrà rimosso");
        Post post10 = new Post("Andrea", "F**k Donald Trump");

        // POST CHE GENERANO ERRORI DURANTE L'INSERIMENTO
        Post postE = new Post("XXX", "Post utente non registrato");
        Post postU = new Post("TimCook", "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        Post postF = new Post("TimCook", "");

        // POST CHE NON SONO IN SOCIAL NETWORK
        Post _post1 = new Post("TimCook", " (bozza) Questo è un messaggio che vorrei trasmettere a tutti");
        

        try {
            SN.addPost(post1);
            SN.addPost(post2);
            SN.addPost(post3);
            SN.addPost(post4);
            SN.addPost(post5);
            SN.addPost(post6);
            SN.addPost(post7);
            SN.addPost(post8);
            SN.addPost(post9);
            SN.addPost(post0);
            
            // Error posts
            SN.addPost(postE); // Utente non registrato
            // SN.addPost(postU); // Testo troppo lungo
            // SN.addPost(postF); // Testo vuoto
        } catch(UserNotInSocialNetworkException | TextTooLongException | EmptyPostException e) {
            System.out.println(e);
        } 
        
        try {
            SN.addPost(postU); // Testo troppo lungo
        } catch(TextTooLongException e) {
            System.out.println(e);
        }

        try {
            SN.addPost(postF); // Testo vuoto
        } catch(EmptyPostException e) {
            System.out.println(e);
        }

        // Aggiungo i follower ai post
        try {
            // LIKES POST0
            SN.addFollower("Andrea", post0.getID());

            // LIKES POST1
            SN.addFollower("George", post1.getID());
            SN.addFollower("Fabian", post1.getID());
            SN.addFollower("Joseph", post1.getID());
            SN.addFollower("TimCook", post1.getID());
            SN.addFollower("Andrea", post1.getID()); // Questo verrà rimosso nel test dopo

            // LIKES POST2
            SN.addFollower("Fabian", post2.getID());
            SN.addFollower("Joseph", post2.getID());

            // LIKES POST3
            SN.addFollower("George", post3.getID());
            SN.addFollower("TimCook", post3.getID());

            // LIKES POST4
            SN.addFollower("Fabian", post4.getID());
            SN.addFollower("Joseph", post4.getID());

            // LIKES POST5
            SN.addFollower("George", post5.getID());

            // LIKES POST6
            SN.addFollower("Fabian", post6.getID());

            // LIKES POST9
            SN.addFollower("Mib", post9.getID());
            SN.addFollower("Joseph", post9.getID());
            SN.addFollower("George", post9.getID());

            // Caso in cui l'utente non esiste 
            SN.addFollower("XXX", post8.getID());
            // Caso in cui il post non esiste
            // SN.addFollower("Mib000", _post1.getID());
            // Caso in cui un utente prova a mettere like ad un suo post
            // SN.addFollower("Joseph", post6.getID());

        } catch (UserNotInSocialNetworkException | NoPostInNetworkException | IllegalFollowException e) {
            System.out.println(e);
        }

        // SN.printNetwork(); // STAMPO LA RETE SOCIALE PRIMA DI RIMUOVERE UN UTENTE

        // TEST RIMOZIONE FOLLOWER DA POST
        try {
            SN.removeFollowerFromPost("Andrea", post1.getID());

            // Provo a rimuovere un follower di un post che non è presente
            // nella lista dei followers di quel post.
            // SN.removeFollowerFromPost("TimCook", post2.getID());
            // Le altre eccezioni sono gestite come addFollower(String username, UUID postID)
        } catch(UserNotInSocialNetworkException | NoPostInNetworkException | IllegalFollowException | NoFollowerInPostException e) {
            System.out.println(e);
        }

        
        // TEST: - Quando rimuovo un follow B da un post di A e quel like era l'unico
        // allora B non segue più A.
        // Funziona infatti Andrea aveva un unico like ad un post di Mib000. Dopo averlo tolto
        // risulta che Andrea non segue più Mib000
        System.out.println("--------------------");
        // SN.printNetwork(); // STAMPO LA RETE SOCIALE DOPO LA RIMOZIONE DI UN UTENTE PER VEDERNE GLI EFFETTI

        // TEST: - Provo a rimuovere un post, ovviamente da esso verranno rimossi tutti i likes
        // e quindi come prima, qualora ci sia un unico like da B ad A ==> B non sarà più un seguace di A.
        
        try {
            SN.deletePost(post0);

            // Caso in cui metto un post inesistente nella rete
            SN.deletePost(_post1);
        } catch(NoPostInNetworkException e) {
            System.out.println(e);
        }

        // SN.printAllPosts();
        // SN.printNetwork();

        // TEST - VERIFICA INSERIMENTO TAG NEI POST
        try {
            // TAGS LECITI
            SN.addTagOnPost("TimCook", post2.getID());
            SN.addTagOnPost("Mib000", post4.getID());
            SN.addTagOnPost("Mib000", post5.getID());
            SN.addTagOnPost("Andrea", post6.getID());
            SN.addTagOnPost("George", post6.getID());
            SN.addTagOnPost("Mib000", post7.getID());
            SN.addTagOnPost("TimCook", post7.getID());
            SN.addTagOnPost("Mib000", post9.getID());

            // AUTO TAG (NON LECITO)
            SN.addTagOnPost("Mib000", post1.getID());
            // USER NOT IN SOCIAL
            SN.addTagOnPost("XX", post1.getID());
            // POST NOT IN SOCIAL
            SN.addTagOnPost("Mib000", post0.getID());
        } catch(UserNotInSocialNetworkException | NoPostInNetworkException | IllegalTagException e) {
            System.out.println(e);
        }

        // TEST GUESSFOLLOWERS

        // SN.printAllPosts();
        List<Post> ps = new LinkedList<Post>();
        ps.add(post2);
        ps.add(post4);
        ps.add(post9);
        // Catturo una NoPostInNetworkException
        // ps.add(post0);

        try {
            Map<String, Set<String>> derivativeSN = SN.guessFollowers(ps);

            System.out.println(derivativeSN);

        } catch(NoPostInNetworkException e) {
            System.out.println(e);
        }

        // TEST INFLUENCERS
        SN.printNetwork();
        System.out.println(SN.influencers());
    
        System.out.println("TOP INFLUENCERS: " + SN.influencers());

        // TEST GETMENTIONEDUSERS()
        System.out.println("MENTIONED USERS: " + SN.getMentionedUsers());

        // TEST GETMENTIONEDUSERS(LISTA POST)
        System.out.println("MENTIONED USERS IN POSTS: " + SN.getMentionedUsers(ps));
    
        // TEST WRITTENBY()
        for (Post pst : SN.writtenBy("Mib000")) {
            System.out.println("POSTS OF Mib000: " + pst.getID());
        }

        // TEST WRITTENBY(LISTA POST, USERNAME)
        for (Post pst : SN.writtenBy(ps, "George")) {
            System.out.println("POSTS OF George in ps: " + pst.getID());
        }

        // TEST CONTAINING
        List<String> words = new LinkedList<String>();
        words.add("day"); 
        words.add("Update");
        words.add("MiCrOsOfT");

        for (Post pst : SN.containing(words)) {
            System.out.println("POSTS WITH WORDS: " + pst.getAuthor());
        }

        // TEST IMPLEMENTAZIONE PROTECTEDSOCIALNETWORK
        Post badPost = new Post("Joseph", "TimCook sei uno stupido");

        // TEST INSERIMENTO PAROLE CATTIVE IN UN POST
        // Qui dovrebbe non inserire il post e stampare a schermo badWords
        try {
            ((ProtectedSocialNetwork) SN).addPost(badPost);
        } catch (BadWordsException e) {
            System.out.println(e);
        }
        
        // TEST REPORT DEI POST
        try {
            ((ProtectedSocialNetwork) SN).addDangerousReport("Mib000", post10.getID(), 10, "Politicamente scorretto");
        } catch (UserNotInSocialNetworkException | NoPostInNetworkException | FileNotFoundException e) {
            System.out.println(e);
        }

        SN.printAllPosts();
    }
}
