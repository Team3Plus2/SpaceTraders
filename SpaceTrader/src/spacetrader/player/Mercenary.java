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
    
    public static void Load() {
        MercenaryType.Load();
    }
    
    public Mercenary(MercenaryType mercenaryType) {
        this.mercenaryType = mercenaryType;
    }
}

/*
 *PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
 */
class MercenaryType extends LoadedType implements Serializable {

    private static final String MercenaryFileLocation = "objects/Mercenaries.xml";

    public static void Load() {
        MercenaryType.load(MercenaryType.class, MercenaryFileLocation, null);
    }

    public MercenaryType() {
        
    }
}
