package src.microblogproject;

import src.microblogproject.Post;
/*
    Eccezione checked che viene lanciata quando il testo di un Post supera la lunghezza di 140 caratteri.
*/
public class TextTooLongException extends PostException {
    public TextTooLongException() {
        super();
    }

    public TextTooLongException(Post ps) {
        super("Il testo di " + ps.getID() + " è troppo lungo.");
    }
}
