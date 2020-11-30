package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando si tenta di menzionare in un post, l'autore del post stesso.
*/

public class IllegalTagException extends PostException {

    public IllegalTagException() {
        super();
    }

    public IllegalTagException(String username) {
        super(username + " non può menzionare se stesso nel post.");
    }
}
