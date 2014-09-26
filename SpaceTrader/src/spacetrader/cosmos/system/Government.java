package spacetrader.cosmos.system;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.FromXML;
import spacetrader.xml.XMLReader;

/**
 * Different types of government for solar systems
 * 
 * @author Alex
 */
public class Government {
    
    private static final String governmentsFile = "objects/Resources.xml";
    private static ArrayList<Government> governments;
    
    
    public static void LoadGovernments() {
        XMLReader reader = new XMLReader(Government.class, governmentsFile);
        governments = reader.read();
    }
    
    /**
     * 
     * Gets a government with the given name
     * 
     * @param name the name of the government type
     * @return null if the given government type is not defined, otherwise a government object of the given type
     */
    public static Government get(String name) {
        for(Government a : governments) {
            if(a.name.equals(name))
                return a;
        }
        return null;
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
    private String name;
    
    @FromXML
    TechLevel lowestTechnology;//if null, then the government requires no technology
    
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
            selectedGov = governments.get(rand.nextInt(governments.size()));
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
        return random(new Random(), TechLevel.get(0));
    }
}
