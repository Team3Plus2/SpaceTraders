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
 * This is a planet, it stores planet specific resources and social information.
 * 
 * @author Alex
 */
public class Planet implements TurnListener, Serializable {
    
    /**
     * Random seed associated internally with this planet.
     */
    private Random rand;
    
    /**
     * name of this planet.
     */
    private String name;
    
    /**
     * resources of this planet.
     */
    private Resource resources;
    
    /**
     * relative wealth of this planet.
     * 
     * -1.0f pays less for goods, 0.0f normal, 1.0f pays more for goods
     */
    private float relativeWealth;
    
    /**
     * planet's market.
     */
    private MarketPlace market;
    
    /**
     * planet's shipyard.
     */
    private Shipyard shipyard;
    
    /**
     * needed only for display.
     * location x: degree of orbit, y: average radius distance from sun
     */
    private Point location;
    
    /**
     * needed only for display.
     * location x: degree of orbit, y: average radius distance from sun
     */
    private Point orbitEllipse;

    /**
     * initialize this planet, used by constructors.
     * @param seededRand seeded object to generate planet
     */
    private void init(Random seededRand) {
        this.rand = seededRand;
        name = Universe.generateName(seededRand);
        resources = Resource.random();
        relativeWealth = seededRand.nextFloat() * 2.0f - 1.0f;
        Random r = new Random();
        int angle = r.nextInt(144000);
        int radius = (r.nextInt(400)) + 100;
        location = new Point(angle, radius);
        orbitEllipse = new Point(r.nextInt(10), r.nextInt(10));
        
        TurnEvent.registerListener(this);
    }
    
    /**
     * basic constructor.
     */
    public Planet() {
        rand = new Random();
        init(rand);
    } 
    
    /**
     * seeded constructor.
     * @param seededRand seeded object to generate planet
     */
    public Planet(Random seededRand) {
        init(seededRand);
    } 
    
    /**
     * gets the resources of the planet.
     * @return the resources of the planet
     */
    public Resource resources() {
        return resources;
    }
    
    /**
     * gets the planet's relative wealth value.
     * @return the planet's relative wealth value
     */
    public float wealth() {
        return relativeWealth;
    }
    
    /**
     * gets the planet's name.
     * @return the planet's name
     */
    public String name() {
        return name;
    }

    /**
     * gets the location of the planet.
     * @return the planet's location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * set's the planet's location.
     * @param newLocation the location to give the planet
     */
    public void setLocation(Point newLocation) {
        this.location = newLocation;
    }
    
    @Override
    public String toString() {
        return name;
    }

    /**
     * gets the planet's orbit ellipse.
     * @return the planet's orbit ellipse
     */
    public Point getOrbitEllipse() {
        return orbitEllipse;
    }

    /**
     * sets the planet's orbit ellipse.
     * @param newOrbitEllipse the planet's new orbit ellipse
     */
    public void setOrbitEllipse(Point newOrbitEllipse) {
        this.orbitEllipse = newOrbitEllipse;
    }
    
    /**
     * gets the planet's market.
     * @return the planet's market
     */
    public MarketPlace getMarket() {
        return market;
    }
    
    /**
     * sets the planet's market.
     * @param newMarket the planet's new market
     */
    public void setMarket(MarketPlace newMarket) {
        this.market = newMarket;
    }
    
    /**
     * gets the planet's shipyard.
     * @return the planet's shipyard
     */
    public Shipyard getShipyard() {
        return shipyard;
    }
    
    /**
     * sets the planet's market.
     * @param newShipyard the planet's new market
     */
    public void setShipyard(Shipyard newShipyard) {
        this.shipyard = newShipyard;
    }
    
    @Override
    public void handleNextTurn(Player player) {
        relativeWealth += rand.nextFloat() * 0.2f - 0.1f;
    }
}
