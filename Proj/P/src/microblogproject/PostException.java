package src.microblogproject;
/*
    Eccezione checked che viene lanciata quando vi è un errore qualsiasi su un Post.
*/
public class PostException extends Exception {
    public PostException() { super(); }
    public PostException(String error) { super(error); }
}
