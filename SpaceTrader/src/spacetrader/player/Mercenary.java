package spacetrader.player;

import java.io.Serializable;
import spacetrader.xml.LoadedType;

/**
 * Mercenaries can be hired on the ship to add to player skills.
 * 
 * @author Aaron McAnally
 */
public class Mercenary implements Serializable {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 93L;
    
    /**
     * The type of this mercenary.
     */
    private MercenaryType mercenaryType;

    /**
     * Getter for mercenaryType.
     * 
     * @return mercenaryType
     */
    public MercenaryType getMercenaryType() {
        return mercenaryType;
    }
    
    /**
     * Loads the types of mercenaries.
     */
    public static void load() {
        MercenaryType.load();
    }
    
    /**
     * Constructor for Mercenary.
     * 
     * @param mercenaryType2 the type that this mercenary will be
     */
    public Mercenary(MercenaryType mercenaryType2) {
        this.mercenaryType = mercenaryType2;
    }
}

/**
 * PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR.
 */
class MercenaryType extends LoadedType {

    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 100L;
    
    /**
     * The location of the file that defines the types of mercenaries.
     */
    private static final String MERCENARY_FILE_LOCATION = "objects/Mercenaries.xml";

    /**
     * Loads the types of mercenaries.
     */
    public static void load() {
        MercenaryType.load(MercenaryType.class, MERCENARY_FILE_LOCATION, null);
    }

    /**
     * Constructor for MercenaryType to avoid conflicts with LoadedType.
     */
    public MercenaryType() {
        
    }
}
