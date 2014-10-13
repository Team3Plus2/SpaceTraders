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
public class Shield implements Serializable {
    
    public static void Load() {
        ShieldType.Load();
    }
    
    public static Shield Random() {
        ArrayList<ShieldType> rList = (ArrayList<ShieldType>) ShieldType.getList(ShieldType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Shield(rList.get(index));
    }
    
    private ShieldType shieldType;
    
    /**
     * @param shieldType an enum that determines the type of shield
     */
    public Shield(ShieldType shieldType) {
        this.shieldType = shieldType;
    }
    
    /**
     * try to absorb the given amount of damage
     * 
     * @param amount amount of damage to try to absorb
     * @return negative the amount the shield absorbed if the shield was destroyed, otherwise just the maount absorbed
     */
    public int absorbDamage(int amount) {
        if(amount >= shieldType.getStrength())
            return -shieldType.getStrength();
        return amount;
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
    
    public int getStrength() {
        return strength;
    }
}

