package spacetrader.player;

import java.io.Serializable;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

import java.util.Random;
import java.util.ArrayList;


/**
 * A weapon may be stored on a ship.
 * 
 * @author Aaron McAnally
 */
public class Weapon implements Serializable {
    
    public static void Load() {
        LaserType.Load();
    }
    
    public static Weapon Random() {
        ArrayList<LaserType> rList = (ArrayList<LaserType>) LaserType.getList(LaserType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Weapon(rList.get(index));
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
    
    public int getStrength() {
        return laserType.getStrength();
    }

}

/**
 * LaserTypes:
 * LaserType(1) = pulse laser
 * LaserType(2) = beam laser
 * LaserType(3) = military laser
 */
class LaserType extends LoadedType implements Serializable {
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
    
    public int getStrength() {
        return strength;
    }
}
