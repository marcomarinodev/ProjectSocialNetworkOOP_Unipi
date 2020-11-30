package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando si prova a creare un post con
    username non concesso.
*/

public class UsernameNotAllowedException extends UserException {
    public UsernameNotAllowedException() {
        super();
    }

    public UsernameNotAllowedException(String error) {
        super(error);
    }
}
