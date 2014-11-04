package spacetrader.player;

import java.io.Serializable;
import spacetrader.xml.LoadedType;

/**
 * Mercenaries can be hired on the ship to add to player skills.
 * 
 * @author Aaron McAnally
 */
public class Mercenary implements Serializable {
    
    private MercenaryType mercenaryType;
    
    public static void load() {
        MercenaryType.load();
    }
    
    public Mercenary(MercenaryType mercenaryType2) {
        this.mercenaryType = mercenaryType2;
    }
}

/*
 *PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
 */
class MercenaryType extends LoadedType implements Serializable {

    private static final String MERCENARY_FILE_LOCATION = "objects/Mercenaries.xml";

    public static void load() {
        MercenaryType.Load(MercenaryType.class, MERCENARY_FILE_LOCATION, null);
    }

    public MercenaryType() {
        
    }
}
