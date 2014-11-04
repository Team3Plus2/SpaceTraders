package spacetrader.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

/**
 * A shield is added protection to a ship.
 * 
 * @author Aaron McAnally
 */
public class Shield extends AbstractUpgrade implements Serializable {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 95L;
    
    /**
     * Loads the shield type.
     */
    public static void load() {
        ShieldType.load();
    }
    
    /**
     * @return a random shield type
     */
    public static Shield random() {
        ArrayList<ShieldType> rList = (ArrayList<ShieldType>) ShieldType.getList(ShieldType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Shield(rList.get(index));
    }
    
    /**
     * The shield's type.
     */
    private ShieldType shieldType;
    
    /**
     * @param shieldType2 an enum that determines the type of shield
     */
    public Shield(ShieldType shieldType2) {
        this.shieldType = shieldType2;
    }
    
    /**
     * try to absorb the given amount of damage.
     * 
     * @param amount amount of damage to try to absorb
     * @return negative the amount the shield absorbed if the shield was destroyed, otherwise just the maount absorbed
     */
    public int absorbDamage(int amount) {
        if (amount >= shieldType.getStrength()) {
            return -shieldType.getStrength();
        }
        return amount;
    }
    
    /**
     * Used to differentiate between Upgrade types.
     * 
     * @return "Shield"
     */
    @Override
    public String getClassName() {
        return "Shield";
    }
    
    @Override
    public int getPrice() {
        return shieldType.getPrice();
    }
    
    @Override
    public int getTechLevel() {
        return shieldType.getTechLevel();
    }
    
    /**
     * @return the list of all possible shield types
     */
    public static ArrayList<Shield> getShieldTypes() {
        ArrayList<Shield> shields = new ArrayList<Shield>();
        ArrayList<ShieldType> types = ShieldType.getShieldTypes();
        for (ShieldType type: types) {
            shields.add(new Shield(type));
        }
        return shields;
    }
    
    @Override
    public String toString() {
        return "Shield - Strength: " + this.shieldType.getStrength();
    }
    
}


/**
 * Basic ShieldTypes.
 * ShieldType(1): energy shield
 * ShieldType(2): reflective shield
 */
class ShieldType extends LoadedType {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 101L;

    /**
     * The shield XML file location.
     */
    private static final String SHIELD_FILE_LOCATION = "objects/Shields.xml";

    /**
     * Loads the shield type.
     */
    public static void load() {
        ShieldType.load(ShieldType.class, SHIELD_FILE_LOCATION, null);
    }
    
    /**
     * The strength of the shield.
     */
    @FromXML
    private int strength;
    /**
     * The price of the shield.
     */
    @FromXML
    private int price;
    /**
     * The tech level of the shield.
     */
    @FromXML
    private int techLevel;
    
    /**
     * need by XML loader.
     */
    public ShieldType() {

    }
    
    /**
     * @param strength2 the strength of the shield
     * @param price2 the price of the shield
     * @param techLevel2 the tech level of the shield
     */
    ShieldType(int strength2, int price2, int techLevel2) {
        this.strength = strength2;
        this.price = price2;
        this.techLevel = techLevel2;
    }
    
    /**
     * @return the strength of the shield
     */
    public int getStrength() {
        return strength;
    }
    
    /**
     * @return the price of the shield
     */
    public int getPrice() {
        return price;
    }
    
    /**
     * @return the tech level of the shield
     */
    public int getTechLevel() {
        return techLevel;
    }
    
    /**
     * @return the list of all shield types
     */
    public static ArrayList<ShieldType> getShieldTypes() {
        return ShieldType.getList(ShieldType.class);
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

