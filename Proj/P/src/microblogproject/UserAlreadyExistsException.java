package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando un utente già presente nella rete tenta di registrarsi
*/

public class UserAlreadyExistsException extends UserException {
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String username) {
        super("L'utente " + username + " esiste già nella rete.");
    }
}
