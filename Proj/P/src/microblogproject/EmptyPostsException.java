package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando si tenta di fare un'azione coinvolgendo uno o più post, ma
    non è presente alcun post nella rete sociale.
*/

public class EmptyPostsException extends Exception {
    public EmptyPostsException() {
        super();
    }
    
    public EmptyPostsException(String error) {
        super(error);
    }
}
