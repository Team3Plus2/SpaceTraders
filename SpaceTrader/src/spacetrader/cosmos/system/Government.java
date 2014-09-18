package spacetrader.cosmos.system;

import java.util.Random;

/**
 * Different types of government for solar systems
 * 
 * @author Alex
 */
public enum Government {
    ANARCHY(null),
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
    THEOCRACY(null);
    
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
        Government[] values = Government.values();
        Government selectedGov = null;
        do {
            selectedGov = values[rand.nextInt(values.length)];
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
        return random(new Random(), TechLevel.HI_TECH);
    }
}
