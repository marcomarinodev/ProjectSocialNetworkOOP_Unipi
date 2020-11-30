package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando si tenta di compiere un'azione su un Post non
    presente nella rete sociale
*/

public class NoPostInNetworkException extends PostException {
    public NoPostInNetworkException() {
        super();
    }

    public NoPostInNetworkException(Post errorPost) {
        super("ID_POST: " + errorPost.getID());
    }
}
