package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;
import java.util.HashMap;



/**
 * Array is unbounded
 * 
 * Uses a embedded HashMap
 * 
 * @author Alex
 */
public class SparseSpace {
    private HashMap<Integer, HashMap<Integer, SolarSystem>> spaceMap;

    public SparseSpace() {
        spaceMap = new HashMap<>();
    }

    /**
     * Inserts a solar system at the given coordinates (null for deep space)
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param system The solar system to insert
     */
    public void insert(int x, int y, SolarSystem system) {
        if(spaceMap.containsKey(x)) {
            spaceMap.get(x).put(y, system);
        } else {
            HashMap<Integer, SolarSystem> ymap = new HashMap<Integer, SolarSystem>();
            ymap.put(y, system);
            spaceMap.put(x, ymap);
        }
    }
    
    /**
     * Returns the solar system at the given coordinate
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return null if outer space, otherwise, the solar system at the given coordinates
     */
    public SolarSystem get(int x, int y) {
        HashMap<Integer, SolarSystem> ymap = spaceMap.get(x);
        if(ymap == null)
            return null;
        return ymap.get(y);
    }
}