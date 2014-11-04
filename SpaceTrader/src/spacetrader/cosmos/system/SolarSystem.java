package spacetrader.cosmos.system;

import java.io.Serializable;
import java.util.Random;
import spacetrader.cosmos.Universe;
import spacetrader.turns.TurnEvent;
import spacetrader.turns.TurnListener;
import spacetrader.player.Player;


/**
 * This is a solar system, it stores planets and some system wide social information.
 *
 * @author Alex
 */
public class SolarSystem implements TurnListener, Serializable {
    
    /**
     * the default maximum number of planets in a system.
     */
    private static final int DEFAULT_MAX_PLANETS = 10;

    /**
     * the system's x coordinate.
     */
    private int x;
    
    /**
     * the system's y coordinate.
     */
    private int y;
    
    /**
     * the system's name.
     */
    private String name;
    
    /**
     * the system's planets.
     */
    private Planet[] planets;
    
    /**
     * the system's internally associated random object.
     */
    private Random rand;
    
    /**
     * the system's sun type.
     */
    private SunType sun;
    
    /**
     * the system's tech level.
     */
    private TechLevel techLevel;
    
    /**
     * the system's government.
     */
    private Government government;
    
    /**
     * the system's relative wealth.
     * -1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods
     */
    private float relativeWealth;
    
    /**
     * the system's relation with the player.
     * 0- normal, -1.0f- attack on sight, 1.0f will not search
     */
    private float relation;
    
    /**
     * initializes the system, used by constructors.
     * @param seededRand random object to generate the system
     */
    private void init(Random seededRand) {
        this.rand = seededRand;
        name = Universe.generateName(seededRand);
        sun = SunType.random(seededRand);
        techLevel = TechLevel.random(seededRand);
        government = Government.random(seededRand, techLevel);
        relativeWealth = seededRand.nextFloat() * 2.0f - 1.0f;
        planets = new Planet[seededRand.nextInt(DEFAULT_MAX_PLANETS) + 5];
        relation = 0;
        
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet(seededRand);
        }
        
        TurnEvent.registerListener(this);
    }
    
    /**
     * initialize the solar system with an internally generated random object.
     */
    public SolarSystem() {
        rand = new Random();
        init(rand);
    }    
    
    /**
     * initialize a solar system with a seeded random object.
     * @param seededRand seeded random object
     */
    public SolarSystem(Random seededRand) {
        init(seededRand);
    }
    
    /**
     * get the system's sun type.
     * @return the system's sun type
     */
    public SunType sunType() {
        return sun;
    }
    
    /**
     * get the system's tech level.
     * @return the system's tech level
     */
    public TechLevel techLevel() {
        return techLevel;
    }
    
    /**
     * get the system's government.
     * @return the system's government
     */
    public Government government() {
        return government;
    } 
    
    /**
     * check to see if this system's police should attack the player on sight.
     * @return true if should attack
     */
    public boolean shouldAttack() {
        return relation == -1.0f;
    }
    
    /**
     * check to see if this system's police should search the player.
     * @return true if should search
     */
    public boolean shouldSearch() {
        return relation != 1.0f;
    }
    
    /**
     * get the system's wealth.
     * @return the system's wealth
     */
    public float wealth() {
        return relativeWealth;
    }
    
    /**
     * get the system's planets.
     * @return the system's planets
     */
    public Planet[] planets() {
        return planets.clone();
    }
    
    /**
     * get the system's name.
     * @return the system's name
     */
    public String name() {
        return name;
    }
    
    /**
     * sets the system's x coordinate.
     * @param newX the system's new x coordinate
     */
    public void setX(int newX) {
        this.x = newX;
    }
    
    /**
     * sets the system's y coordinate.
     * @param newY the system's new y coordinate
     */
    public void setY(int newY) {
        this.y = newY;
    }
    
    /**
     * get the system's x coordinate.
     * @return the system's x coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * get the system's y coordinate.
     * @return the system's y coordinate
     */
    public int getY() {
        return y;
    }
    
    @Override
    public void handleNextTurn(Player player) {
        relativeWealth += rand.nextFloat() * 0.2f - 0.1f;
    }
    
    @Override
    public String toString() {
        String forreturn = name + ":\n-----";
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < planets.length; i++) {
            buff.append(planets[i]);
        }
        return forreturn;
    }
}
