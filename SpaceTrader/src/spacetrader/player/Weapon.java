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
public class Weapon extends Upgrade implements Serializable {
    
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
    
    public int getStrength() {
        return laserType.getStrength();
    }
    
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
    private int strength, price, techLevel;

    /**
     * Required by XMLReader
     */
    public LaserType() {
        
    }
    
    LaserType(int strength, int price, int techLevel) {
        this.strength = strength;
        this.price = price;
        this.techLevel = techLevel;
    }
    
    public int getStrength() {
        return strength;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getTechLevel() {
        return techLevel;
    }
    
    /**
     * @return the list of all laser types
     */
    public static ArrayList<LaserType> getLaserTypes() {
        return ShieldType.getList(LaserType.class);
    }
}
