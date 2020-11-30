package src.microblogproject;
/**
  *  Eccezione checked che viene lanciata quando il testo di un post è vuoto.
*/
public class EmptyPostException extends Exception {    
    public EmptyPostException() {
        super();
    }

    public EmptyPostException(String error) {
        super(error);
    }
}
