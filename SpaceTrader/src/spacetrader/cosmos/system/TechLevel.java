package spacetrader.cosmos.system;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.xml.XMLReader;
import spacetrader.xml.FromXML;

/**
 *
 * @author Alex
 */
public class TechLevel implements Comparable {
    
    private static final String techLevelFile = "objects/TechLevels.xml";
    private static ArrayList<TechLevel> techLevels;
    
    public static void LoadTechLevels() {
        XMLReader reader = new XMLReader(TechLevel.class, techLevelFile);
        techLevels = reader.read();
    }
    
    /**
     * 
     * Gets a TechLevel with the given name
     * 
     * @param name the name of the Tech Level type
     * @return null if the given resource type is not defined, otherwise a tech level object of the given type
     */
    public static TechLevel get(String name) {
        for(TechLevel a : techLevels) {
            if(a.name.equals(name))
                return a;
        }
        return null;
    }
    
    /**
     * 
     * Gets a TechLevel with the given level
     * 
     * @param level the level of the tech to get
     * @return null if the given resource type is not defined, otherwise a tech level object of the given type
     */
    public static TechLevel get(int level) {
        if(level >= techLevels.size())
            return null;
        return techLevels.get(level);
    }
    
    @FromXML
    private String name;
    
    /*PRE_AGRICULTURE,
    AGRICULTURE,
    MEDIEVAL,
    EARLY_INDUSTRIAL,
    INDUSTRIAL,
    POST_INDUSTRIAL,
    HI_TECH;*/
    
    public static TechLevel random(Random rand) {
        return techLevels.get(rand.nextInt(techLevels.size()));
    }
    
    public static TechLevel random() {
        return random(new Random());
    }
    
    @Override
    public int compareTo(Object other) {
        if(other instanceof TechLevel) {
            TechLevel techOther = (TechLevel)other;
            if(techOther.name.equals(this.name))
                return 0;
            for(TechLevel a : techLevels) {
                if(a.name.equals(techOther.name))
                    return -1;
                if(a.name.equals(this.name))
                    return 1;
            }
        }
        return 0;
    }
}
