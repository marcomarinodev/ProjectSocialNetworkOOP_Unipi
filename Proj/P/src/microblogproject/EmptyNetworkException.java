package src.microblogproject;

/*
    Eccezione checked che viene lanciata quando si tenta di fare un'azione coinvolgendo uno o più utenti, ma
    la rete sociale è vuota.
*/

public class EmptyNetworkException extends Exception {
    public EmptyNetworkException() {
        super();
    }
}
