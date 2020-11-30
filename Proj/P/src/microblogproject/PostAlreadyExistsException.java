package src.microblogproject;

/*
    Eccezione unchecked che viene lanciata quando si vuole aggiungere un post
    già presente nella rete sociale.
*/

public class PostAlreadyExistsException extends RuntimeException {

    public PostAlreadyExistsException() {
        super();
    }

    public PostAlreadyExistsException(String error) {
        super(error);
    }

}
