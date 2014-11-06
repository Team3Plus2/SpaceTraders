package spacetrader.cosmos.system;

import java.util.Random;
import spacetrader.xml.LoadedType;

/**
 * 
 * Resources for a planet.
 * 
 * @author Alex
 */
public class Resource extends LoadedType {
    
    /**
     * An id for serialization.
     */
    static final long serialVersionUID = (long) 45L;
    
    /**
     * the default chance that a resource, rather than NO_SPECIAL_RESOURCES is randomly selected.
     */
    private static final float DEFAULT_RESOURCE_CHANCE = 0.25f;
    
    /**
     * Load all the resources from xml.
     */
    public static void load() {
        Resource def = new Resource("NO_SPECIAL_RESOURCES");
        Resource.load(Resource.class, "objects/Resources.xml", def);
    }
    
    /**
     * gets the default resource type.
     * @return the default resource type.
     */
    public static Resource defaultValue() {
        return (Resource) Resource.defaultValue(Resource.class);
    }
    
    /**
     * gets a random resource.
     * @param rand seeded random object
     * @param resourceChance chance that a resource is obtained
     * @return a random resource
     */
    public static Resource random(Random rand, float resourceChance) {
        Resource resource = (Resource) Resource.defaultValue(Resource.class);
        float useResource = rand.nextFloat();
        if (useResource > resourceChance) {
            resource = (Resource) Resource.get(rand.nextInt(Resource.size(Resource.class) - 1) + 1, Resource.class);
        }
        return resource;
    }
    
    /**
     * gets a non-externally seeded random resource.
     * @param resourceChance chance of getting a resource
     * @return a random resource
     */
    public static Resource random(float resourceChance) {
        return random(new Random(), resourceChance);
    }
    
    /**
     * gets a non-externally seeded random resource using the default resource chance.
     * @return a random resource
     */
    public static Resource random() {
        return random(new Random(), DEFAULT_RESOURCE_CHANCE);
    }
    
    /**
     * Needed for XMLReader.
     */
    public Resource() {
        
    }
    
    /**
     * initializes a new resource with the given name.
     * @param name resource name 
     */
    public Resource(String name) {
        super(name);
    }
    
}
