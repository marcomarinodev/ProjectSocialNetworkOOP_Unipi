package src.microblogproject;

import java.util.*;

public interface PostInterface {
    /**
     * OVERVIEW: Tipo di dato immutabile ed identificatbile attraverso un codice 128 bit universale e univoco
     *          
     * 
     * TYPICAL ELEMENT:
     *  
     *      <id, author, text, timestamp>, dove:
     *          - id: identificatore immutabile, universale e unico a 128-bit per il post
     *          - author: rappresenta l'utente che a fatto il post
     *          - text: contenuto formato testo del post
     *          - timestamp: data e ora in cui il post è stato fatto
     * 
     * 
     */

     /**
      * EFFECTS: ritorna l'id di this
      */
    public UUID getID();

    /**
      * EFFECTS: ritorna l'autore di this
      */
    public String getAuthor();

    /**
      * EFFECTS: ritorna il testo di this
      */
    public String getText();

    /**
     * EFFECTS: ritorna data e ora in cui il post viene pubblicato in formato
     * dateTimePattern di tipo String
     */
    public String getDateTimeToString();

    /**
      * EFFECTS: ritorna this in formato testo
      */
    public String toString();

}
