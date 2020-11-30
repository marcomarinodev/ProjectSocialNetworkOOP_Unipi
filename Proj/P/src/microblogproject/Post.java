package src.microblogproject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Post implements PostInterface {
    /**
     * ABSTRACTION FUNCTION:
     *      f(obj) : O -> A = <obj.id, obj.author, obj.text, obj.timestamp>
     * 
     * REPRESENTATION INVARIANT:
     *      f(obj) : O -> Bool =
     *          this.author != null 
     *          this.text != null
     */

     /**
      * Identificatore immutabile, universale e univoco a 128 bit generata 
      * pseudo randomicamente.
      */
    private final UUID id;
    
    /**
     * Autore del post
     */
    private final String author;

    /**
     * Testo del post
     */
    private final String text;

    /**
     * Data e ora nel momento in cui un post viene creato
     */
    private final LocalDateTime timestamp;

    /**
     * Formato in cui vediamo Data e ora della pubblicazione del post
     */
    private static final String dateTimePattern = "HH:mm:ss - dd-MM-yyyy";

    /**
     * --> Costruisce un oggetto di tipo Post assegnandone id randomico, autore, testo e data e ora attuali
     * 
     * REQUIRES: user != null && textMessage != null
     * 
     * THROWS: user == null || textMessage == null lancia NullPointerException
     */
    public Post(String user, String textMessage) {
        id = UUID.randomUUID(); // seleziono un id (128 bit)

        if (user == null) throw new NullPointerException("user non può essere null");
        if (textMessage == null) throw new NullPointerException("user non può essere null");

        author = user;

        text = textMessage;
        timestamp = LocalDateTime.now();
    }

    /**
      * EFFECTS: ritorna l'id di this
      */
    public UUID getID() {
        return id;
    }

    /**
      * EFFECTS: ritorna l'autore di this
      */
    public String getAuthor() {
        return author;
    }

    /**
      * EFFECTS: ritorna il testo di this
      */
    public String getText() {
        return text;
    }

    /**
     * EFFECTS: ritorna la data e l'ora in cui il post viene pubblicato
     */
    private LocalDateTime getDateTime() {
        return timestamp;
    }

    /**
     * EFFECTS: ritorna data e ora in cui il post viene pubblicato in formato
     * dateTimePattern di tipo String
     */
    public String getDateTimeToString() {
      return getDateTime().format(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    /**
      * EFFECTS: ritorna this in formato testo
      */
    public String toString() {
        return  "(*) POST ID: " + getID() + "\n" + 
                "(*) POST DATE: " + getDateTimeToString() + "\n" +
                "(*) AUTHOR: " + getAuthor() + 
                "(*) TEXT: " + getText() + "\n";
    }

}