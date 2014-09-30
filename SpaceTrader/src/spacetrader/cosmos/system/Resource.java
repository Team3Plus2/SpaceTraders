package spacetrader.cosmos.system;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.LoadableType;
import spacetrader.xml.FromXML;
import spacetrader.xml.TypeLoader;
import spacetrader.xml.XMLReader;

/**
 * 
 * Resources for a planet
 * 
 * @author Alex
 */
public class Resource extends TypeLoader {
    
    private static float DEFAULT_RESOURCE_CHANCE = 0.25f;
    
    public static void Load() {
        Resource def = new Resource("NO_SPECIAL_RESOURCES");
        Resource.Load(Resource.class, "objects/Resources.xml", def);
    }
    
    public static Resource Default() {
        return (Resource)Resource.Default(Resource.class);
    }
    
    public static Resource random(Random rand, float resourceChance) {
        float useResource = rand.nextFloat();
        if(useResource > resourceChance) {
            return (Resource)Resource.get(rand.nextInt(Resource.size(Resource.class) - 1) + 1, Resource.class);
        }
        return (Resource)Resource.Default(Resource.class);
    }
    
    public static Resource random(float resourceChance) {
        return random(new Random(), resourceChance);
    }
    
    public static Resource random() {
        return random(new Random(), DEFAULT_RESOURCE_CHANCE);
    }
    
    /**
     * Needed for XMLReader
     */
    public Resource() {
        
    }
    
    public Resource(String name) {
        super(name);
    }
    
}
