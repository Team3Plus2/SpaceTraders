package spacetrader.cosmos.system;

import java.util.Random;
import spacetrader.cosmos.Universe;

/**
 * This is a solar system, it stores planets and some system wide social information
 *
 * @author Alex
 */
public class SolarSystem {
    
    private static final int DEFAULT_MAX_PLANETS = 10;

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
        planets = new Planet[rand.nextInt(DEFAULT_MAX_PLANETS)];
        
        for(int i = 0; i < planets.length; i ++) {
            planets[i] = new Planet(rand);
        }
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
    
    @Override
    public String toString() {
        return name;
    }
}
