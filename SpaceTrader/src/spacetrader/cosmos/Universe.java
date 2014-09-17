package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Universe {
    
    private static final int MEANING = 42;//Also applies to life and everything
    private static final int DEFAULT_INIT_WIDTH = 50;
    private static final float DEFAULT_INIT_SPREAD = 0.15f;
    
    private SparseSpace Space;
    private float spread;
    private Random rand;
    
    /**
     * Generate initial universe along with construction
     * 
     * @param width initial Width/ height of the universe
     * @param spread sparseness of solarsystem occurrence
     * @return A generated universe object
     */
    private void init(int width, float spread) {
        rand = new Random();
        Space = new SparseSpace();
        this.spread = spread;
        generateAround(0,0, width);
    }
    
    public Universe(int width, float spread) {
        init(width, spread);
    }
    
    public Universe() {
        init(DEFAULT_INIT_WIDTH, DEFAULT_INIT_SPREAD);
    }
    
    /**
     * Generates an area of the given width around the given coordinates
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     */
    public void generateAround(int x, int y, int width) {
        for(int i = x - width; i < x + width; i++) {
            for(int j = y - width; j < y + width; j++) {
                if(canPlaceSystemAt(i,j) && rand.nextFloat() < spread)
                    Space.insert(i, j, new SolarSystem());
            }
        }
    }
    
    private boolean canPlaceSystemAt(int x, int y) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) { 
                if(Space.get(x+i, y+j) != null)
                    return false;
            }
        }
        return true;
    }
    
    public SolarSystem getSolarSystem(int x, int y) {
        return Space.get(x, y);
    }
    
}
