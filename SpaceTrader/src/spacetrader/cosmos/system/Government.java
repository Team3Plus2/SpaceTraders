package spacetrader.cosmos.system;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.TypeLoader;
import spacetrader.xml.FromXML;
import spacetrader.xml.XMLReader;

/**
 * Different types of government for solar systems
 * 
 * @author Alex
 */
public class Government extends TypeLoader {
    
    private static final String governmentsFile = "objects/Governments.xml";
    
    
    public static void Load() {
        Government.Load(Government.class, governmentsFile, null);
    }
    
    /*ANARCHY(null),
    CAPITALIST_STATE(null),
    COMUNIST_STATE(null),
    CONFEDERACY(null),
    CORPORATE_STATE(TechLevel.INDUSTRIAL),
    CYBERNETIC_STATE(TechLevel.HI_TECH),
    DEMOCRACY(null),
    DICTATORSHIP(TechLevel.AGRICULTURE),
    FASCIST_STATE(null),
    FEUDAL_STATE(null),
    MILITARY_STATE(TechLevel.AGRICULTURE),
    MONARCHY(null),
    PACIFIST_STATE(null),
    SOCIALIST_STATE(null),
    TECHNOCRACY(TechLevel.HI_TECH),
    THEOCRACY(null);*/
    
    @FromXML
    TechLevel lowestTechnology;//if null, then the government requires no technology
    
    /**
     * Required by XMLReader
     */
    public Government() {
        
    }
    
    Government(TechLevel requiredTechnology) {
        lowestTechnology = requiredTechnology;
    }
    
    /**
     * 
     * Get a random government based on permitted government types with the given technology
     * 
     * @param rand random number generator to use
     * @param technology technology level to consider
     * @return a random government allowed with the given level of technology
     */
    public static Government random(Random rand, TechLevel technology) {
        Government selectedGov = null;
        do {
            selectedGov = (Government)Government.get(rand.nextInt(Government.size(Government.class)), Government.class);
            if(selectedGov.lowestTechnology != null && technology.compareTo(selectedGov.lowestTechnology) < 0)
                selectedGov = null;
        } while(selectedGov == null);
        return selectedGov;
    }
    
    /**
     * Assumes TechLevel.HI_TECH and creates its own random variable
     * 
     * @return a random government type
     */
    public static Government random() {
        return random(new Random(), null);
    }
}
