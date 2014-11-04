package spacetrader.cosmos;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.turns.TurnEvent;
import spacetrader.xml.FromXML;
import spacetrader.cosmos.SparseSpace.SparseIterator;
import spacetrader.player.Player;

/**
 *
 * The universe class stores stores all solar systems and provides all needed API for the frontend
 * aspects of the app to generate and display the universe.
 * 
 * 
 * @author Alex
 */
public class Universe implements Iterable<SolarSystem>, Serializable {
    
    /**
     * THE MEANING OF LIFE, THE UNIVERSE AND EVERYTHING!.
     */
    private static final int MEANING = 42; //Also applies to life and everything
    /**
     * Default initial width of this universe.
     */
    private static final int DEFAULT_INIT_WIDTH = 100;
    /**
     * Default initial spread of this universe.
     */
    private static final float DEFAULT_INIT_SPREAD = 0.15f;
    /**
     * Default maximum number of name syllables.
     */
    private static final int DEFAULT_MAX_NAME_SYLLABLES = 3;
    /**
     * Default chance of y being a vowel.
     */
    private static final float DEFAULT_Y_CHANCE = 0.1f;
    /**
     * Default initial vowel chance.
     */
    private static final float DEFAULT_INITIAL_VOWEL_CHANCE = 0.5f;
    /**
     * Default double letter chance.
     */
    private static final float DEFAULT_DOUBLE_LETER_CHANCE = 0.2f;
    
    /**
     * SparseSpace that backs this Universe.
     */
    private SparseSpace space;
    /**
     * Stores all the currently generated points, used to make generation more efficient.
     */
    private HashSet<Point> generated; //stores all the currently generated points, used to make generation more effecient
    /**
     * Spread of the Universe.
     */
    @FromXML
    private float spread;
    /**
     * Random number generator.
     */
    @FromXML
    private Random rand;
    
    /**
     * Generate a random name.
     * 
     * @param rand random object to use
     * @return a random name
     */
    public static String generateName(Random rand) {
        int syllables = rand.nextInt(DEFAULT_MAX_NAME_SYLLABLES) + 1;
        String name = "";
        
        if (rand.nextFloat() < DEFAULT_INITIAL_VOWEL_CHANCE) {
            name += randomVowel(rand);
        }
        
        for (int i = 0; i < syllables; i++) {
            name += randomLetter(rand);
            name += randomVowel(rand);
            //name += randomLetter(rand);
            
            if (rand.nextFloat() < DEFAULT_DOUBLE_LETER_CHANCE) {
                name += randomLetter(rand);
            }
        }
        
        String firstChar = "" + name.charAt(0);
        name = firstChar.toUpperCase() + name.substring(1);
        
        return name;
    }
    
    /**
     * Returns xMin of this Universe.
     * 
     * @return xMin
     */
    public int xMin() {
        return space.xMin();
    }
    
    /**
     * Returns xMax of this Universe.
     * 
     * @return xMax
     */
    public int xMax() {
        return space.xMax();
    }
    
    /**
     * Returns yMin of this Universe.
     * 
     * @return yMin
     */
    public int yMin() {
        return space.yMin();
    }
    
    /**
     * Returns yMax of this Universe.
     * 
     * @return yMax
     */
    public int yMax() {
        return space.yMax();
    }
    
    /**
     * Helper method for the generateName function.
     * 
     * @param rand Random object for random number generation
     * @return a random vowel
     */
    private static char randomVowel(Random rand) {
        int vowelSelector = rand.nextInt(5);
        float useY = rand.nextFloat(); // we don't want y to be used as often as the rest of the vowels
        if (useY < DEFAULT_Y_CHANCE) {
            return 'y';
        } else {
            switch (vowelSelector) {
                case 0:
                    return 'a';
                case 1:
                    return 'e';
                case 2:
                    return 'i';
                case 3:
                    return 'o';
                case 4:
                    return 'u';
                default:
                    return 'a';
            }
        }
    }
    
    /**
     * Helper method for the generateName function.
     * 
     * @param rand Random object for random number generation
     * @return a random letter
     */
    private static char randomLetter(Random rand) {
        int letterSelector = rand.nextInt(25); //we don't use z
        int a = (int) 'a';
        a += letterSelector;
        char cA = (char) a;
        return cA;
    }
    
    /**
     * Generate initial universe along with construction.
     * 
     * @param width initial Width/ height of the universe
     * @param spread2 sparseness of solarsystem occurrence
     */
    private void init(int width, float spread2) {
        generated = new HashSet<>();
        rand = new Random();
        space = new SparseSpace();
        this.spread = spread2 * MEANING / 42f;
        generateInPosXDirection(0, 0, width / 2, width / 2);
        generateinNegXDirection(0, -1, width / 2, width / 2);
    }
    
    /**
     * Constructor for Universe.
     * 
     * @param width the width we want the universe to be
     * @param spread2 the spread we want this universe to have
     */
    public Universe(int width, float spread2) {
        init(width, spread2);
    }
    
    /**
     * Constructor for a default Universe.
     */
    public Universe() {
        init(DEFAULT_INIT_WIDTH, DEFAULT_INIT_SPREAD);
    }
    
    /**
     * Generates an area of the given width around the given coordinates.
     * 
     * @param x0 The x0 coordinate
     * @param y0 The y0 coordinate
     * @param x1 The x1 coordinate
     * @param y1 The y1 coordinate
     */
    public void generateFrom(int x0, int y0, int x1, int y1) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for (int i = x0; i < x1; i++) {
            for (int j = y0; j < y1; j++) {
                if (!isPointGenerated(i, j)) {
                    systems.add(generatePoint(i, j));
                }
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param height The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateInPosXDirection(int x, int y, int height, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for (int i = x; i <= x + length; i++) {
            for (int j = y - height / 2; j <= y + height / 2; j++) {
                if (!isPointGenerated(i, j)) {
                    systems.add(generatePoint(i, j));
                }
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinPosYDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for (int i = y; i <= y + length; i++) {
            for (int j = x - width / 2; j <= x + width / 2; j++) {
                if (!isPointGenerated(i, j)) {
                    systems.add(generatePoint(i, j));
                }
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinNegXDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for (int i = y; i >= y - length; i--) {
            for (int j = x - width / 2; j <= x + width / 2; j++) {
                if (!isPointGenerated(i, j)) {
                    systems.add(generatePoint(i, j));
                }
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinNegYDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for (int i = y; i >= y - length; i--) {
            for (int j = x - width / 2; j <= x + width / 2; j++) {
                if (!isPointGenerated(i, j)) {
                    systems.add(generatePoint(i, j));
                }
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates a SolarSystem at the given coordinates.
     * 
     * @param x the x coordinate we want the SolarSystem to be at
     * @param y the y coordinate we want the SolarSystem to be at
     * @return the created SolarSystem
     */
    private SolarSystem generatePoint(int x, int y) {
        SolarSystem sys = new SolarSystem(rand);
        if (canPlaceSystemAt(x, y) && rand.nextFloat() < spread) {
            space.insert(x, y, sys);
        }
        generated.add(new Point(x, y));
        return sys;
    }
    
    /**
     * Checks if a SolarSystem exists at the given coordinates.
     * 
     * @param x the x coordinate we are checking for a SolarSystem
     * @param y the y coordinate we are checking for a SolarSystem
     * @return true if there is SolarSystem at the given coordinates.
     */
    private boolean isPointGenerated(int x, int y) {
        return generated.contains(new Point(x, y));
    }
    
    /**
     * Checks if a SolarSystem can be placed at.
     * 
     * @param x the x coordinate we are checking
     * @param y the y coordinate we are checking
     * @return true if a SolarSystem can be placed at the given coordinates
     */
    private boolean canPlaceSystemAt(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) { 
                if (space.get(x + i, y + j) != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks if a SolarSystem can generated in the given radius of the given coordinates.
     * 
     * @param x the x coordinate we are checking
     * @param y the y coordinate we are checking
     * @param width the radius we are checking
     * @return true if a SolarSystem can be placed with the radius of the given coordinates
     */
    public boolean canGenerateAround(int x, int y, int width) {
        for (int i = x - width / 2; i <= x + width / 2; i++) {
            for (int j = y - width / 2; j <= y + width / 2; j++) {
                if (space.get(i, j) != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Gets the SolarSystem at the given coordinates.
     * 
     * @param x the x coordinate we are getting
     * @param y the y coordinate we are getting
     * @return the SolarSystem at the given coordinates.
     */
    public SolarSystem getSolarSystem(int x, int y) {
        return space.get(x, y);
    }
    
    /**
     * Gets the SolarSystem closest to the given coordinates within the given radius.
     * 
     * @param x the x coordinate we are getting
     * @param y the y coordinate we are getting
     * @param rad the radius we are checking
     * @return the SolarSystem closest to the given coordinates within the given radius.
     */
    public SolarSystem getClosestSolarSystem(int x, int y, int rad) {
        for (int r = 0; r < rad; r++) { //radius loop
            for (int i = x-r; i < x+r; i++) { //x loop
                for (int j = y-r; j < y+r; j++) { //y loop
                    if (space.get(i,j) != null) {
                        return space.get(i,j);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Moves to the next turn.
     * 
     * @param player the Player we are moving to the next turn.
     */
    public void nextTurn(Player player) {
        TurnEvent.nextTurn(player);
    }
    
    /**
     * Returns an iterator that will go from the bottom leftmost generated solarsystem
     * to the upper rightmost solar system or, more concisely put, will iterate row by row
     * across the box defined by the corners (xMin, yMin), (xMax, yMax).
     * 
     * This can be used to draw a picture of the entire generated universe
     * 
     * @return an iterator that will behave as described above
     */
    @Override
    public SparseIterator iterator() {
        return space.iterator();
    }
    
    /**
     * 
     * Iterate from x0 and y0 to x1 and y1.
     * 
     * Note: x0 is less than x1 and y0 is less than y1
     * 
     * @param x0 lower left x coordinate
     * @param y0 lower left y coordinate
     * @param x1 upper right x coordinate
     * @param y1 upper right y coordinate
     * @return Iterator which will iterate row by row from the bottom left point to the upper right point (as a box defined by the points)
     */
    public SparseIterator iterateFrom(int x0, int y0, int x1, int y1) {
        return space.iterateFrom(x0, y0, x1, y1);
    }
}

