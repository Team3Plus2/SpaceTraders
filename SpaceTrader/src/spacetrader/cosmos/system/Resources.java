package spacetrader.cosmos.system;

import java.util.Random;

/**
 *
 * @author Alex
 */
public enum Resources {
    NO_SPECIAL_RESOURCES,
    MINERAL_RICH,
    MINERAL_POOR,
    DESERT,
    LOTS_OF_WATER,
    RICH_SOIL,
    POOR_SOIL,
    RICH_FAUNA,
    LIFELESS,
    WIERD_MUSHROOMS,
    LOTS_OF_HERBS,
    ARTISTIC,
    WARLIKE;
    
    private static float DEFAULT_RESOURCE_CHANCE = 0.25f;
    
    public static Resources random(Random rand, float resourceChance) {
        float useResource = rand.nextFloat();
        if(useResource > resourceChance) {
            Resources[] values = Resources.values();
            return values[rand.nextInt(values.length - 1) + 1];
        }
        return NO_SPECIAL_RESOURCES;
    }
    
    public static Resources random(float resourceChance) {
        return random(new Random(), resourceChance);
    }
    
    public static Resources random() {
        return random(new Random(), DEFAULT_RESOURCE_CHANCE);
    }
}
