package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando vi è un errore qualsiasi riguardo un utente.
*/

public class UserException extends Exception {
    public UserException() { 
        super();
    }

    public UserException(String error) {
        super(error);
    }
}
