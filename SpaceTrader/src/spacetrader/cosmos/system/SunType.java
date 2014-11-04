package spacetrader.cosmos.system;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Random;

import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 *
 * @author Alex
 */
public class SunType extends LoadedType implements Serializable {
    
    /**
     * the location of the xml file to load the sun types from.
     */
    private static final String SUN_TYPE_FILE_LOCATION = "objects/SunTypes.xml";
    
    /**
     * load all the sun types.
     */
    public static void load() {
        SunType.load(SunType.class, SUN_TYPE_FILE_LOCATION, null);
    }
    
    /**
     * Get the default sun value.
     * @return The default sun value
     */
    public static SunType defaultValue() {
        return (SunType) SunType.defaultValue(SunType.class);
    }
    
    /**
     * Gets a random sun type.
     * @param rand random pre-seeded object to use
     * @return a random sun type
     */
    public static SunType random(Random rand) {
        ArrayList<SunType> types = SunType.getList(SunType.class);
        float totalChance = 0.0f;
        
        for (SunType a : types) {
            totalChance += a.chance;
        }
        
        float diceRoll = rand.nextFloat() * totalChance;
        totalChance = 0;
        
        for (SunType a : types) {
            totalChance += a.chance;
            if (diceRoll < totalChance) {
                return a;
            }
        }
        
        return types.get(types.size() - 1);
    }
    
    /**
     * Get a non-pre-seeded random sun type.
     * @return a random sun type
     */
    public static SunType random() {
        return random(new Random());
    }
    
    /**
     * Chance of getting this sun type.
     */
    @FromXML
    private float chance;
    
    /**
     * the red color value.
     */
    @FromXML (required = false)
    private float r;
            
    /**
     * The green color value.
     */
    @FromXML (required = false)
    private float g;
    
    /**
     * The blue color value.
     */
    @FromXML (required = false)
    private float b;
    
    /**
     * The image to use for this sun type.
     */
    @FromXML (required = false)
    private String image;
    
    /**
     * True if this type uses color for display.
     * @return true if this type uses color for display
     */
    public boolean usesColor() {
        return image == null;
    }
    
    /**
     * True if this type uses an image for display.
     * @return true if this type uses an image for display
     */
    public boolean usesImage() {
        return image != null;
    }
    
    /**
     * get the red color value.
     * @return the red color value
     */
    public float getR() {
        return r;
    }
    
    /**
     * get the green color value.
     * @return the green color value
     */
    public float getG() {
        return g;
    }
    
    /**
     * get the blue color value.
     * @return the blue color value
     */
    public float getB() {
        return b;
    }
    
    /**
     * get the image for this sun.
     * @return the image for this sun type, or null if no image is used
     */
    public String getImage() {
        return image;
    }
    
}
