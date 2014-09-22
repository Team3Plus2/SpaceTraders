package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import spacetrader.turns.TurnListener;
import spacetrader.turns.TurnEvent;

/**
 *
 * The universe class stores stores all solar systems and provides all needed API for the frontend
 * aspects of the app to generate and display the universe.
 * 
 * 
 * @author Alex
 */
public class Universe implements Iterable<SolarSystem>, TurnListener{
    
    private static final int MEANING = 42;//Also applies to life and everything
    private static final int DEFAULT_INIT_WIDTH = 100;
    private static final float DEFAULT_INIT_SPREAD = 0.15f;
    private static final int DEFAULT_MAX_NAME_SYLLABLES = 3;
    private static final float DEFAULT_Y_CHANCE = 0.1f;
    private static final float DEFAULT_INITIAL_VOWEL_CHANCE = 0.5f;    
    private static final float DEFAULT_DOUBLE_LETER_CHANCE = 0.2f;
    
    private SparseSpace Space;
    private float spread;
    private Random rand;
    
    /**
     * Generate a random name
     * 
     * @param rand random object to use
     * @return a random name
     */
    public static String GenerateName(Random rand) {
        int syllables = rand.nextInt(DEFAULT_MAX_NAME_SYLLABLES) + 1;
        String name = "";
        
        if(rand.nextFloat() < DEFAULT_INITIAL_VOWEL_CHANCE) {
            name += randomVowel(rand);
        }
        
        for(int i = 0; i < syllables; i++) {
            name += randomLetter(rand);
            name += randomVowel(rand);
            //name += randomLetter(rand);
            
            if(rand.nextFloat() < DEFAULT_DOUBLE_LETER_CHANCE) {
                name += randomLetter(rand);
            }
        }
        
        String firstChar = "" + name.charAt(0);
        name = firstChar.toUpperCase() + name.substring(1);
        
        return name;
    }
    
    /**
     * Helper method for the GenerateName function
     * 
     * @return a random vowel
     */
    private static char randomVowel(Random rand) {
        int vowelSelector = rand.nextInt(5);
        float useY = rand.nextFloat();// we don't want y to be used as often as the rest of the vowels
        if(useY < DEFAULT_Y_CHANCE) {
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
     * Helper method for the GenerateName function
     * 
     * @return a random letter
     */
    private static char randomLetter(Random rand) {
        int letterSelector = rand.nextInt(25);//we don't use z
        int A = (int)'a';
        A += letterSelector;
        char cA = (char)A;
        return cA;
    }
    
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
        generateInPosXDirection(0,0, width/2, width/2);
        generateinNegXDirection(0,-1, width/2, width/2);
    }
    
    public Universe(int width, float spread) {
        init(width, spread);
    }
    
    public Universe() {
        init(DEFAULT_INIT_WIDTH, DEFAULT_INIT_SPREAD);
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param width The width of the area to generate
     */
    public void generateInPosXDirection(int x, int y, int height, int length) {
        for(int i = x; i <= x + length; i++) {
            for(int j = y - height/2; j <= y + height/2; j++) {
                if(canPlaceSystemAt(i,j) && rand.nextFloat() < spread)
                    Space.insert(i, j, new SolarSystem(rand));
            }
        }
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param width The width of the area to generate
     */
    public void generateinPosYDirection(int x, int y, int width, int length) {
        for(int i = y; i <= y + length; i++) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(canPlaceSystemAt(i,j) && rand.nextFloat() < spread)
                    Space.insert(i, j, new SolarSystem(rand));
            }
        }
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param width The width of the area to generate
     */
    public void generateinNegXDirection(int x, int y, int width, int length) {
        for(int i = y; i >= y - length; i--) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(canPlaceSystemAt(i,j) && rand.nextFloat() < spread)
                    Space.insert(i, j, new SolarSystem(rand));
            }
        }
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param width The width of the area to generate
     */
    public void generateinNegYDirection(int x, int y, int width, int length) {
        for(int i = y; i >= y - length; i--) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(canPlaceSystemAt(i,j) && rand.nextFloat() < spread)
                    Space.insert(i, j, new SolarSystem(rand));
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
    
    public boolean canGenerateAround(int x, int y, int width) {
        for(int i = x - width/2; i <= x + width/2; i++) {
            for(int j = y - width/2; j <= y + width/2; j++) {
                if(Space.get(i,j) != null)
                    return false;
            }
        }
        return true;
    }
    
    public SolarSystem getSolarSystem(int x, int y) {
        return Space.get(x, y);
    }
    
    @Override
    public void handleNextTurn() {
        System.out.println("Turn: " + TurnEvent.getTurn());
    }
    
    /**
     * Returns an iterator that will go from the bottom leftmost generated solarsystem
     * to the upper rightmost solar system or, more concisely put, will iterate row by row
     * across the box defined by the corners (xMin, yMin), (xMax, yMax)
     * 
     * This can be used to draw a picture of the entire generated universe
     * 
     * @return an iterator that will behave as described above
     */
    @Override
    public Iterator<SolarSystem> iterator() {
        return Space.iterator();
    }
    
    /**
     * 
     * Iterate from x0 and y0 to x1 and y1
     * 
     * @precondition x0 < x1 and y0 < y1
     * @param x0 lower left x coordinate
     * @param y0 lower left y coordinate
     * @param x1 upper right x coordinate
     * @param y1 upper right y coordinate
     * @return Iterator which will iterate row by row from the bottom left point to the upper right point (as a box defined by the points)
     */
    public Iterator<SolarSystem> iterateFrom(int x0, int y0, int x1, int y1) {
        return Space.iterateFrom(x0, y0, x1, y1);
    }
}

