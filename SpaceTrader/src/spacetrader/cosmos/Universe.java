package spacetrader.cosmos;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.turns.TurnListener;
import spacetrader.turns.TurnEvent;
import spacetrader.xml.FromXML;
import spacetrader.cosmos.SparseSpace.SparseIterator;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.encounter.Encounter;
import spacetrader.player.Player;

/**
 *
 * The universe class stores stores all solar systems and provides all needed API for the frontend
 * aspects of the app to generate and display the universe.
 * 
 * 
 * @author Alex
 */
public class Universe implements Iterable<SolarSystem>, TurnListener, Serializable {
    
    private static final int MEANING = 42;//Also applies to life and everything
    private static final int DEFAULT_INIT_WIDTH = 100;
    private static final float DEFAULT_INIT_SPREAD = 0.15f;
    private static final int DEFAULT_MAX_NAME_SYLLABLES = 3;
    private static final float DEFAULT_Y_CHANCE = 0.1f;
    private static final float DEFAULT_INITIAL_VOWEL_CHANCE = 0.5f;    
    private static final float DEFAULT_DOUBLE_LETER_CHANCE = 0.2f;
    
    private SparseSpace Space;
    private HashSet<Point> generated;//stores all the currently generated points, used to make generation more effecient
    @FromXML
    private float spread;
    @FromXML
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
    
    public int xMin() {
        return Space.xMin();
    }
    
    public int xMax() {
        return Space.xMax();
    }
    
    public int yMin() {
        return Space.yMin();
    }
    
    public int yMax() {
        return Space.yMax();
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
        generated = new HashSet<>();
        rand = new Random();
        Space = new SparseSpace();
        this.spread = spread;
        TurnEvent.RegisterListener(this);
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
     * Generates an area of the given width around the given coordinates
     * 
     * @param x0 The x0 coordinate
     * @param y0 The y0 coordinate
     * @param x1 The x1 coordinate
     * @param y1 The y1 coordinate
     */
    public void generateFrom(int x0, int y0, int x1, int y1) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for(int i = x0; i < x1; i++) {
            for(int j = y0; j < y1; j++) {
               if(!isPointGenerated(i,j))
                    systems.add(generatePoint(i,j));
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param height The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateInPosXDirection(int x, int y, int height, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for(int i = x; i <= x + length; i++) {
            for(int j = y - height/2; j <= y + height/2; j++) {
                if(!isPointGenerated(i,j))
                    systems.add(generatePoint(i,j));
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinPosYDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for(int i = y; i <= y + length; i++) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(!isPointGenerated(i,j))
                    systems.add(generatePoint(i,j));
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinNegXDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for(int i = y; i >= y - length; i--) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(!isPointGenerated(i,j))
                    systems.add(generatePoint(i,j));
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    /**
     * Generates an area of the given height past the given x
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of the area to generate
     * @param length The length of the area to generate
     */
    public void generateinNegYDirection(int x, int y, int width, int length) {
        ArrayList<SolarSystem> systems = new ArrayList<>();
        for(int i = y; i >= y - length; i--) {
            for(int j = x - width/2; j <= x + width/2; j++) {
                if(!isPointGenerated(i,j))
                    systems.add(generatePoint(i,j));
            }
        }
        UniverseGenerationEvent.universeGenerated(systems);
    }
    
    private SolarSystem generatePoint(int x, int y) {
        SolarSystem sys = new SolarSystem(rand);
        if(canPlaceSystemAt(x,y) && rand.nextFloat() < spread)
            Space.insert(x, y, sys);
        generated.add(new Point(x,y));
        return sys;
    }
    
    private boolean isPointGenerated(int x, int y) {
        return generated.contains(new Point(x,y));
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
    
    public SolarSystem getClosestSolarSystem(int x, int y, int rad) {
        SolarSystem found = null;
        if(Space.get(x, y) != null) {
            found = Space.get(x, y);
        } else {
            int radius = 1;
            while (found == null && radius < rad) {
                if (Space.get(x + radius, y + radius) != null) {
                    found = Space.get(x + radius, y + radius);
                }
                else if (Space.get(x - radius, y + radius) != null) {
                    found = Space.get(x - radius, y + radius);
                }
                else if (Space.get(x + radius, y - radius) != null) {
                    found = Space.get(x + radius, y - radius);
                }
                else if (Space.get(x - radius, y - radius) != null) {
                    found = Space.get(x - radius, y - radius);
                }
                else if (Space.get(x, y - radius) != null) {
                    found = Space.get(x, y - radius);
                }
                else if (Space.get(x, y + radius) != null) {
                    found = Space.get(x, y + radius);
                }
                else if (Space.get(x + radius, y) != null) {
                    found = Space.get(x + radius, y);
                }
                else if (Space.get(x - radius, y) != null) {
                    found = Space.get(x - radius, y);
                }
                radius++;
            }
        }
        return found;
    }
    
    public void NextTurn(Player player) {
        TurnEvent.NextTurn(player);
    }
    
    @Override
    public void handleNextTurn(Player player) {
        Random rand = new Random();
        
        /*
        if(rand.nextFloat() < 0.1) {
            Encounter other = new Encounter(player.getCurrentSolarSystem(), player);
            System.out.println(other.getGreeting());
            
            //always check enemy agressivness first
            if(other.willAttack()) {
                System.out.println("Other ship: Hand over all your goods or die!");
                if(confirmationInterface()) {
                    other.loot(player);
                } else {
                    fight(other, player);
                }

            } else if(other.willRequestTrade()) {
                System.out.println("Other ship: Do you want to buy some goods?");
                if(confirmationInterface()) {
                    commandLineBuyInterface(other.getMarketPlace(), player);
                }
            } else if(other.willRequestSearch()) {
                System.out.println("Other ship: by the authority of the " + player.getCurrentSolarSystem().Name() + 
                        " system, I request permission to search your ship for illegal goods");
                if(confirmationInterface()) {
                    if(other.search(player)) {
                        System.out.println("Other ship: I have confiscated illegal goods in your hold, " +
                                 "don't let this happen again");
                    } else {
                        System.out.println("Other ship: You're all clear, my apologies for disturbing you");
                    }
                } else {
                    System.out.println("Then we will search you by force!");
                    fight(other, player);
                }
            } else {
                System.out.println("Enter a to attack, t to request trade, or any other key to continue on your way");
                Scanner in = new Scanner(System.in);
                String reaction = in.nextLine();
                if(reaction.equals("a")) {
                    fight(other, player);
                } else if(reaction.equals("t")) {
                    commandLineBuyInterface(other.getMarketPlace(), player);
                }
            }
        }
        */
    }
    
    public boolean confirmationInterface() {
        System.out.println("(Y or N)\n");
        Scanner in = new Scanner(System.in);
        String reaction = in.nextLine();
        return reaction.toUpperCase().equals("Y");
    }
    
    public void commandLineBuyInterface(MarketPlace market, Player player) {
        System.out.println("Goods:");
        int count = 1;
        for(TradeGood good : market.getListOfGoods()) {
            System.out.println("\t" + count + ": " + good.getName() + " : " + good.getAmount() + " : " + good.getCurrentPriceEach());
        }
        System.out.println("-1: Leave");
        
        Scanner in = new Scanner(System.in);
        int order = 0;
        while(order != -1) {
            System.out.println("What would you like?\n(enter an index of a good listed above)");
            order = in.nextInt();
            if(order <= market.getListOfGoods().size() && order > 0) {
                System.out.println("How many would you like?");
                int amount = in.nextInt();
                if(market.getListOfGoods().get(order).getAmount() >= amount) {
                    market.buy(player, market.getListOfGoods().get(order), amount);
                } else {
                    System.out.println("You have entered an invalid amount");
                }
            } else if(order != -1) {
                System.out.println("You have entered an invalid good");
            }
        }
    }
    
    public void fight(Encounter other, Player player) {
        MarketPlace loot = combatInterface(other, player);
        if(loot == null) {
            player.die();
        } else {
            System.out.println("Loot your defeated enemy!");
            commandLineBuyInterface(loot, player);
        }
    }
    
    public MarketPlace combatInterface(Encounter other, Player player) {
        Scanner in = new Scanner(System.in);
        String reaction = "";
        int result = 0;
        while(result == 0) {
            result = other.roundOfCombat(player, null);
            if(result == -2) {
                System.out.println("Other ship: Hold your fire! I surrender!\n(c to continue fire, any other key to accept surrender)");
                reaction = in.nextLine();
                if(reaction.equals("c")) {
                    result = 0;
                }
            }
        }

        if(result == 1) {
            System.out.println("You are dead");
        } else if(result == -1) {
            System.out.println("You destroy the enemy ship");
            return other.getSalvageExchange();
        } else if(result == -2) {
            System.out.println("The enemy surrenders to you");
            return other.getLootingExchange();
        }
        
        return null;
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
    public SparseIterator iterator() {
        return Space.iterator();
    }
    
    /**
     * 
     * Iterate from x0 and y0 to x1 and y1
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
        return Space.iterateFrom(x0, y0, x1, y1);
    }
}

