package spacetrader.cosmos.system;

import java.util.Random;

/**
 *
 * @author Alex
 */
public enum TechLevel {
    PRE_AGRICULTURE,
    AGRICULTURE,
    MEDIEVAL,
    EARLY_INDUSTRIAL,
    INDUSTRIAL,
    POST_INDUSTRIAL,
    HI_TECH;
    
    public static TechLevel random(Random rand) {
        TechLevel[] values = TechLevel.values();
        return values[rand.nextInt(values.length)];
    }
    
    public static TechLevel random() {
        return random(new Random());
    }
}
