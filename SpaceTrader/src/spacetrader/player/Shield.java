package spacetrader.player;

import java.io.Serializable;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

/**
 * A shield is added protection to a ship.
 * 
 * @author Aaron McAnally
 */
public class Shield implements Serializable {
    
    public static void Load() {
        ShieldType.Load();
    }
    
    private ShieldType shieldType;
    
    /**
     * @param shieldType an enum that determines the type of shield
     */
    public Shield(ShieldType shieldType) {
        this.shieldType = shieldType;
    }
}


/**
 * Basic ShieldTypes:
 * ShieldType(1): energy shield
 * ShieldType(2): reflective shield
 */
class ShieldType extends LoadedType implements Serializable {

    private static final String ShieldFileLocation = "objects/Shields.xml";

    public static void Load() {
        ShieldType.Load(ShieldType.class, ShieldFileLocation, null);
    }
    
    @FromXML
    private int strength;
    
    public ShieldType() {

    }
    
    ShieldType(int strength) {
        this.strength = strength;
    }
}

