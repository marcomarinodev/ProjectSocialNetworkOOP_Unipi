package src.microblogproject;

/**
 * Eccezione che indica il tentativo di un utente di pubblicare un post contentente
 * testo offensivo.
 */

public class BadWordsException extends Exception {
    public BadWordsException() {
        super();
    }

    public BadWordsException(String error) {
        super(error);
    }
}
