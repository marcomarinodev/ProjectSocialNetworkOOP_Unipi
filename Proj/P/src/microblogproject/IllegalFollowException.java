package src.microblogproject;
/*
    Eccezione checked che viene lanciata quando un utente prova a seguire un suo stesso post
*/
public class IllegalFollowException extends PostException {
    public IllegalFollowException() {
        super();
    }

    public IllegalFollowException(String user) {
        super(user + " non può seguire questo utente");
    }
}
