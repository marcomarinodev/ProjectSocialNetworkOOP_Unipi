package src.microblogproject;
/*
    Eccezione checked che viene lanciata quando un username non fa parte degli utenti
    che hanno messo like ad un post.
*/
public class NoFollowerInPostException extends PostException {
    public NoFollowerInPostException() {
        super();
    }

    public NoFollowerInPostException(String username) {
        super(username + " non è presente nei likes di questo post");
    } 
}
