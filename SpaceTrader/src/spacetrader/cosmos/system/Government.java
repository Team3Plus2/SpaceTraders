package spacetrader.cosmos.system;

import java.util.Random;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

/**
 * Different types of government for solar systems.
 * 
 * Doesn't ovverride .equals since it should actually just use the Loaded Type super class check
 * 
 * @author Alex
 */
public class Government extends LoadedType {
    
    /**
     * the location of the xml file to load the governments from.
     */
    private static final String GOVERNMENTS_FILE = "objects/Governments.xml";
    
    /**
     * An id for serialization.
     */
    static final long serialVersionUID = (long) 43L;
    
    /**
     * load all the government types.
     */
    public static void load() {
        Government.load(Government.class, GOVERNMENTS_FILE, null);
    }
    
    /**
     * The lowest technology allowed to be used with this government.
     */
    @FromXML
    TechLevel lowestTechnology; //if null, then the government requires no technology
    
    /**
     * Required by XMLReader.
     */
    public Government() {
        
    }
    
    /**
     * Constructor that should be used in most situations.
     * @param requiredTechnology the lowest technology allowed to be used with this government
     */
    Government(TechLevel requiredTechnology) {
        lowestTechnology = requiredTechnology;
    }
    
    /**
     * 
     * Get a random government based on permitted government types with the given technology.
     * 
     * @param rand random number generator to use
     * @param technology technology level to consider
     * @return a random government allowed with the given level of technology
     */
    public static Government random(Random rand, TechLevel technology) {
        Government selectedGov = null;
        do {
            selectedGov = (Government) Government.get(rand.nextInt(Government.size(Government.class)), Government.class);
            if (selectedGov.lowestTechnology != null && technology.compareTo(selectedGov.lowestTechnology) < 0) {
                selectedGov = null;
            }
        } while (selectedGov == null);
        return selectedGov;
    }
    
    /**
     * Assumes TechLevel.HI_TECH and creates its own random variable.
     * 
     * @return a random government type
     */
    public static Government random() {
        return random(new Random(), null);
    }
}
