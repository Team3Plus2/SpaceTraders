package spacetrader.cosmos.system;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;
import spacetrader.cosmos.Universe;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.Shipyard;
import spacetrader.turns.TurnEvent;
import spacetrader.turns.TurnListener;
import spacetrader.player.Player;

/**
 * This is a planet, it stores planet specific resources and social information
 * 
 * @author Alex
 */
public class Planet implements TurnListener, Serializable {
    
    private Random rand;
    private String name;
    private Resource resources;
    private float relativeWealth;//-1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods
    private MarketPlace market;
    private Shipyard shipyard;
    
    //needed only for display
    //location x: degree of orbit, y: average radius distance from sun
    private Point location;
    private Point orbitEllipse;

    private void init(Random rand) {
        this.rand = rand;
        name = Universe.GenerateName(rand);
        resources = Resource.random();
        relativeWealth = rand.nextFloat() * 2.0f - 1.0f;
        Random r = new Random();
        int angle = r.nextInt(144000);
        int radius = (r.nextInt(400)) + 100;
        location = new Point(angle, radius);
        orbitEllipse = new Point(r.nextInt(10), r.nextInt(10));
        
        TurnEvent.RegisterListener(this);
    }
    
    public Planet() {
        rand = new Random();
        init(rand);
    } 
    
    public Planet(Random rand) {
        init(rand);
    } 
    
    public Resource Resources() {
        return resources;
    }
    
    public float Wealth() {
        return relativeWealth;
    }
    
    public String Name() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public Point getOrbitEllipse() {
        return orbitEllipse;
    }

    public void setOrbitEllipse(Point orbitEllipse) {
        this.orbitEllipse = orbitEllipse;
    }
    
    public MarketPlace getMarket() {
        return market;
    }

    public void setMarket(MarketPlace market) {
        this.market = market;
    }
    
    public Shipyard getShipyard() {
        return shipyard;
    }
    
    public void setShipyard(Shipyard shipyard) {
        this.shipyard = shipyard;
    }
    
    @Override
    public void handleNextTurn(Player player) {
        relativeWealth += rand.nextFloat() * 0.2f - 0.1f;
    }
}
