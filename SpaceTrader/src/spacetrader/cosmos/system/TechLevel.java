package spacetrader.cosmos.system;

import java.io.Serializable;
import java.util.Random;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

/**
 *
 * @author Alex
 */
public class TechLevel extends LoadedType implements Serializable {
    
    private static final String techLevelFile = "objects/TechLevels.xml";

    public static void Load() {
        TechLevel.load(TechLevel.class, "objects/TechLevels.xml", null);
    }
    
    public static TechLevel Default() {
        return (TechLevel)TechLevel.defaultValue(TechLevel.class);
    }
    
    /*PRE_AGRICULTURE,
    AGRICULTURE,
    MEDIEVAL,
    EARLY_INDUSTRIAL,
    INDUSTRIAL,
    POST_INDUSTRIAL,
    HI_TECH;*/
    
    public static TechLevel random(Random rand) {
        return (TechLevel)TechLevel.get(rand.nextInt(TechLevel.size(TechLevel.class)), TechLevel.class);
    }
    
    public static TechLevel random() {
        return random(new Random());
    }
    
}
