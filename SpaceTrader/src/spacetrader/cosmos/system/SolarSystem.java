package spacetrader.cosmos.system;

import java.util.Random;

/**
 *
 * @author Alex
 */
public class SolarSystem {
    
    private Random rand;
    private SunType sun;
    private Resources resources;
    private TechLevel techLevel;
    private Government government;
    private float relativeWealth;//-1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods
    
    public SolarSystem() {
        rand = new Random();
        sun = SunType.random();
        resources = Resources.random();
        techLevel = TechLevel.random();
        government = Government.random();
        relativeWealth = rand.nextFloat() * 2.0f - 1.0f;
    }
    
    public SunType SunType() {
        return sun;
    }
    
    public Resources Resources() {
        return resources;
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
    
    @Override
    public String toString() {
        return "S";
    }
}
