package spacetrader.cosmos.system;

import java.io.Serializable;
import java.util.Random;
import spacetrader.xml.LoadedType;

/**
 *
 * @author Alex
 */
public class TechLevel extends LoadedType {
    
    /**
     * An id for serialization.
     */
    static final long serialVersionUID = (long) 49L;
    
    /**
     * the file to load tech levels from.
     */
    private static final String TECH_LEVEL_FILE = "objects/TechLevels.xml";

    /**
     * load the tech levels from xml.
     */
    public static void load() {
        TechLevel.load(TechLevel.class, TECH_LEVEL_FILE, null);
    }
    
    /**
     * gets the default TechLevel.
     * @return the default TechLevel
     */
    public static TechLevel defaultValue() {
        return (TechLevel) TechLevel.defaultValue(TechLevel.class);
    }

    /**
     * Gets a pre-seeded random tech level.
     * @param rand pre-seeded random object
     * @return a random tech level
     */
    public static TechLevel random(Random rand) {
        return (TechLevel) TechLevel.get(rand.nextInt(TechLevel.size(TechLevel.class)), TechLevel.class);
    }
    
    /**
     * Gets a random tech level.
     * @return a random tech level
     */
    public static TechLevel random() {
        return random(new Random());
    }
    
}
