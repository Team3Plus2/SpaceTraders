package spacetrader.player;

import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;


/**
 * A weapon may be stored on a ship.
 * 
 * @author Aaron McAnally
 */
public class Weapon {
    
    public static void Load() {
        LaserType.Load();
    }
    
    private LaserType laserType;
    
    /**
     * @param laserType an enum that determines the type of weapon
     */
    public Weapon(LaserType laserType) {
        this.laserType = laserType;
    }
    
    public LaserType getLaserType() {
        return laserType;
    }
}

/**
 * LaserTypes:
 * LaserType(1) = pulse laser
 * LaserType(2) = beam laser
 * LaserType(3) = military laser
 */
class LaserType extends LoadedType {
    private static String WeaponFileLocation = "objects/Weapons.xml";
    
    public static void Load() {
        LaserType.Load(LaserType.class, WeaponFileLocation, null);
    }
    
    @FromXML
    private int strength;

    /**
     * Required by XMLReader
     */
    public LaserType() {
        
    }
    
    LaserType(int strength) {
        this.strength = strength;
    }
}
