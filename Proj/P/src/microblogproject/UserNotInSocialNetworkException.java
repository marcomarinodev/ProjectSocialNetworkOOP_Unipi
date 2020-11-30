package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando un utente non presente nella rete tenta di eseguire un'azione
    riservata ai soli utenti della rete sociale.
*/

public class UserNotInSocialNetworkException extends UserException {
    public UserNotInSocialNetworkException() {
        super();
    }

    public UserNotInSocialNetworkException(String ghostUser) { 
        super(ghostUser + " non è un utente.");
    } 
}
