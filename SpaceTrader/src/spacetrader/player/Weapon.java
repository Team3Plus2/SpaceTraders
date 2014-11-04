package spacetrader.player;

import java.io.Serializable;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

import java.util.Random;
import java.util.ArrayList;


/**
 * A weapon may be stored on a ship.
 * 
 *  Doesn't ovverride .equals since it should actually just use the Loaded Type super class check
 * 
 * @author Aaron McAnally
 */
public class Weapon extends AbstractUpgrade implements Serializable {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 98L;
    
    /**
     * Loads the LaserType for the weapon.
     */
    public static void load() {
        LaserType.load();
    }
    
    /**
     * @return Gets a random weapon
     */
    public static Weapon random() {
        ArrayList<LaserType> rList = (ArrayList<LaserType>) LaserType.getList(LaserType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Weapon(rList.get(index));
    }
    
    /**
     * The laser type of the weapon.
     */
    private LaserType laserType;
    
    /**
     * @param laserType2 an enum that determines the type of weapon
     */
    public Weapon(LaserType laserType2) {
        this.laserType = laserType2;
    }
    
    /**
     * Access to the LaserType inner class.
     * 
     * @return the laserType for the weapon
     */
    public LaserType getLaserType() {
        return laserType;
    }
    
    /**
     * @return the list of all weapon types
     */
    public static ArrayList<Weapon> getWeaponTypes() {
        ArrayList<Weapon> weapons = new ArrayList<Weapon>();
        ArrayList<LaserType> types = LaserType.getLaserTypes();
        for (LaserType type: types) {
            weapons.add(new Weapon(type));
        }
        return weapons;
    }
    
    /**
     * @return the weapon's strength
     */
    public int getStrength() {
        return laserType.getStrength();
    }
    
    /**
     * Used to differentiate between Upgrade types.
     * 
     * @return "Weapon"
     */
    @Override
    public String getClassName() {
        return "Weapon";
    }
    
    @Override
    public int getPrice() {
        return laserType.getPrice();
    }
    
    @Override
    public int getTechLevel() {
        return laserType.getTechLevel();
    }
    
    @Override
    public String toString() {
        return "Weapon - Strength: " + this.getStrength();
    }

}

/**
 * LaserTypes.
 * LaserType(1) = pulse laser
 * LaserType(2) = beam laser
 * LaserType(3) = military laser
 */
class LaserType extends LoadedType {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 99L;
    
    /**
     * Weapon XML file location.
     */
    private static final String WEAPON_FILE_LOCATION = "objects/Weapons.xml";
    
    /**
     * Loads the laser type.
     */
    public static void load() {
        LaserType.load(LaserType.class, WEAPON_FILE_LOCATION, null);
    }
    
    /**
     * The laser strength.
     */
    @FromXML
    private int strength;
    /**
     * The laser price.
     */
    @FromXML
    private int price;
    /**
     * The laser tech level.
     */
    @FromXML
    private int techLevel;

    /**
     * Required by XMLReader.
     */
    public LaserType() {
        
    }
    
    /**
     * @param strength2 Laser strength
     * @param price2 Laser price
     * @param techLevel2 Laser tech level
     */
    LaserType(int strength2, int price2, int techLevel2) {
        this.strength = strength2;
        this.price = price2;
        this.techLevel = techLevel2;
    }
    
    /**
     * @return the strength of the laser
     */
    public int getStrength() {
        return strength;
    }
    
    /**
     * @return the price of the laser
     */
    public int getPrice() {
        return price;
    }
    
    /**
     * @return the tech level of the laser
     */
    public int getTechLevel() {
        return techLevel;
    }
    
    /**
     * @return the list of all laser types
     */
    public static ArrayList<LaserType> getLaserTypes() {
        return ShieldType.getList(LaserType.class);
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode(); //blah
    }
}
