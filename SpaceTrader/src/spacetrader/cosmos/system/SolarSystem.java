package spacetrader.cosmos.system;

import java.io.Serializable;
import java.util.Random;
import spacetrader.cosmos.Universe;
import spacetrader.turns.TurnEvent;
import spacetrader.turns.TurnListener;

/**
 * This is a solar system, it stores planets and some system wide social information
 *
 * @author Alex
 */
public class SolarSystem implements TurnListener, Serializable {
    
    private static final int DEFAULT_MAX_PLANETS = 10;

    private int x;
    private int y;
    private String name;
    private Planet[] planets;
    private Random rand;
    private SunType sun;
    private TechLevel techLevel;
    private Government government;
    private float relativeWealth;//-1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods
    
    private void init(Random rand) {
        this.rand = rand;
        name = Universe.GenerateName(rand);
        sun = SunType.random(rand);
        techLevel = TechLevel.random(rand);
        government = Government.random(rand, techLevel);
        relativeWealth = rand.nextFloat() * 2.0f - 1.0f;
        planets = new Planet[rand.nextInt(DEFAULT_MAX_PLANETS) + 5];
        
        for(int i = 0; i < planets.length; i ++) {
            planets[i] = new Planet(rand);
        }
        
        TurnEvent.RegisterListener(this);
    }
    
    public SolarSystem() {
        rand = new Random();
        init(rand);
    }    
    
    public SolarSystem(Random rand) {
        init(rand);
    }
    
    public SunType SunType() {
        return sun;
    }
    
    public TechLevel TechLevel() {
        return techLevel;
    }
    
    public Government Government() {
        return government;
    } 
    
    public float Wealth() {
        return relativeWealth;
    }
    
    public Planet[] Planets() {
        return planets;
    }
    
    public String Name() {
        return name;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public void handleNextTurn() {
        relativeWealth += rand.nextFloat() * 0.2f - 0.1f;
    }
    
    @Override
    public String toString() {
        String forreturn = name + ":\n-----";
        for(int i = 0; i < planets.length; i++) {
            forreturn = forreturn + planets[i];
        }
        return forreturn;
    }
}
