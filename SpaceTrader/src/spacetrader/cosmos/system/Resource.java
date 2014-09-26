package spacetrader.cosmos.system;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadableType;
import spacetrader.xml.XMLReader;

/**
 * 
 * Resources for a planet
 *
 * @author Alex
 */
public class Resource extends LoadableType{
    
    private static float DEFAULT_RESOURCE_CHANCE = 0.25f;
    
    public static void initialize() {
        Resource.initialize(Resource.class, "objects/Resources.xml", null);
    }
    
    public static Resource random(Random rand, float resourceChance) {
        float useResource = rand.nextFloat();
        if(useResource > resourceChance) {
            return (Resource)Resource.get(rand.nextInt(Resource.size() - 1) + 1);
        }
        return (Resource)Resource.Default();
    }
    
    public static Resource random(float resourceChance) {
        return random(new Random(), resourceChance);
    }
    
    public static Resource random() {
        return random(new Random(), DEFAULT_RESOURCE_CHANCE);
    }
    
}
