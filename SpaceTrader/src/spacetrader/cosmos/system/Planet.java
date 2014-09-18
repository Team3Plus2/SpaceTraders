package spacetrader.cosmos.system;

import java.util.Random;
import spacetrader.cosmos.Universe;

/**
 * This is a planet, it stores planet specific resources and social information
 * 
 * @author Alex
 */
public class Planet {
    
    private Random rand;
    private String name;
    private Resources resources;
    private float relativeWealth;//-1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods

    private void init(Random rand) {
        this.rand = rand;
        name = Universe.GenerateName(rand);
        resources = Resources.random();
        relativeWealth = rand.nextFloat() * 2.0f - 1.0f;
    }
    
    public Planet() {
        rand = new Random();
        init(rand);
    } 
    
    public Planet(Random rand) {
        init(rand);
    } 
    
    public Resources Resources() {
        return resources;
    }
    
    public float Wealth() {
        return relativeWealth;
    }
    
    public String Name() {
        return name;
    }
}
