package tietorakenne;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Kristian Leirimaa
 * @version 27.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;


    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
