package spacetrader.player;

import java.io.Serializable;
import java.util.ArrayList;

import spacetrader.cosmos.SparseSpace.SparseIterator;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.Shipyard;
import spacetrader.economy.TradeGood;
import spacetrader.turns.TurnEvent;

/**
 * The main player and everything it owns.
 * 
 * @author Carey MacDonald
 * @author Aaron McAnally
 */
public class Player implements Serializable{
    private static final float FUEL_PER_PLANET_MOVEMENT = 0.5f;
    
    private String name;
    private int pilotSkill, fighterSkill, traderSkill, engineerSkill, investorSkill;
    private Planet currentPlanet;
    private SolarSystem currentSolarSystem;

    private float money;
    private Ship ship;
    
    public Player(String name, int pilotSkill, int fighterSkill, int traderSkill,
                  int engineerSkill, int investorSkill) {
        this.name = name;
        this.money = 1000;
        this.pilotSkill = pilotSkill;
        this.fighterSkill = fighterSkill;
        this.traderSkill = traderSkill;
        this.engineerSkill = engineerSkill;
        this.investorSkill = investorSkill;
        this.ship = new Ship();
    }

    /**
     * Simulates a single round of attack on the given player
     * @param other player to attack
     * @param targets parts of ship to target, if null, targets at random
     * @return true if the enemy ship is destroyed
     */
    public boolean attack(Player other, ArrayList targets) {
        int advantage = other.fighterSkill - this.fighterSkill;//damage modifier based on relitave skill
        return ship.attack(other.ship, advantage, targets);
    }
    
    /**
     * Move the player to the given solar system
     * 
     * @param system target solar system
     * @return true if player has enough fuel to travel and system != currentSolarSystem and planet != currentPlanet
     */
    public boolean move(SolarSystem system) {
        double distance = FUEL_PER_PLANET_MOVEMENT;
        if(system != currentSolarSystem)
            distance = Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        if(!ship.moveDistance(distance))
            return false;
        currentSolarSystem = system;
        if(system.Planets().length >= 1)
            currentPlanet = system.Planets()[0];
        else
            currentPlanet = null;
        TurnEvent.NextTurn();
        return true;
    }
    
    /**
     * Move the player to the given planet and solar system
     * 
     * @param system target solar system
     * @param planet target planet
     * @return true if player has enough fuel to travel and system != currentSolarSystem and planet != currentPlanet
     */
    public boolean move(SolarSystem system, Planet planet) {
        double distance = FUEL_PER_PLANET_MOVEMENT;
        if(system != currentSolarSystem)
            distance = Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        if(!ship.moveDistance(distance))
            return false;
        currentSolarSystem = system;
        currentPlanet = planet;
        if(currentPlanet.getShipyard() == null) {
            currentPlanet.setShipyard(new Shipyard(currentSolarSystem.TechLevel()));
        }
        if (currentPlanet.getMarket() == null) {
            currentPlanet.setMarket(new MarketPlace(currentSolarSystem.TechLevel(), currentPlanet.Resources()));
        }
        TurnEvent.NextTurn();
        return true;
    }
    
    /**
     * Move the player to the given planet in the current system
     * 
     * @param planet target planet
     * @return true if player has enough fuel to travel to the planet
     */
    public boolean move(Planet planet) {
        return Player.this.move(currentSolarSystem, planet);
    }
    
    /**
     * Returns distance to solar system
     * @param system the solar system to check distance of
     * @return 
     */
    public float distanceToSolarSystem(SolarSystem system) {
        float distance;
        distance = (float) Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        return distance;
    }
    
    public String getName() {
        return name;
    }
    
    public SolarSystem getCurrentSolarSystem() {
        return currentSolarSystem;
    }
    
    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentSolarSystem(SolarSystem currentSolarSystem) {
        this.currentSolarSystem = currentSolarSystem;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public float getMoney() {
        return money;
    }
    
    public void setMoney(float money) {
        this.money = money;
    }

    public int getPilotSkill() {
        return pilotSkill;
    }

    public void setPilotSkill(int pilotSkill) {
        this.pilotSkill = pilotSkill;
    }

    public int getFighterSkill() {
        return fighterSkill;
    }

    public void setFighterSkill(int fighterSkill) {
        this.fighterSkill = fighterSkill;
    }

    public int getTraderSkill() {
        return traderSkill;
    }

    public void setTraderSkill(int traderSkill) {
        this.traderSkill = traderSkill;
    }

    public int getEngineerSkill() {
        return engineerSkill;
    }

    public void setEngineerSkill(int engineerSkill) {
        this.engineerSkill = engineerSkill;
    }

    public int getInvestorSkill() {
        return investorSkill;
    }

    public void setInvestorSkill(int investorSkill) {
        this.investorSkill = investorSkill;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public void setShip(Ship ship) {
        this.ship = ship;
    }
    
    public ArrayList<SolarSystem> getTravelable(Universe universe) {
        int travelRadius = (int)ship.getFuel();
        ArrayList<SolarSystem> systems = new ArrayList<>();
        int x = currentSolarSystem.getX();
        int y = currentSolarSystem.getY();
        SparseIterator iter = universe.iterateFrom(x - travelRadius, y - travelRadius, x + travelRadius, y + travelRadius);
        for(SolarSystem i = iter.next();iter.hasNext(); i = iter.next()) {
            if(i != null && ship.getFuel() - distanceToSolarSystem(i) >= 0)
                systems.add(i);
        }
        
        //can always travel to current system
        if(!systems.contains(currentSolarSystem))
            systems.add(currentSolarSystem);
        
        return systems;
    }
    
    /**
     * Gets the players current travel radius based on the ship's fuel
     * @return the player's travel radius (obtained from the ship)
     */
    public float getTravelRadius() {
        return ship.getFuel();
    }
    
    /*****************************************
     *  Cargo Accessor Methods
     *****************************************/
    
    /**
     * Adds a trade good to the cargo hold if space is available.
     * 
     * @param good the good to be added to the cargo hold
     * @return true if successful; false if no more room for cargo
     */
    public boolean addTradeGood(TradeGood good) {
        return ship.addTradeGood(good);
    }
    
    /**
     * Removes a trade good from the cargo hold if the proper amount exists.
     * 
     * @param good the trade good to be removed
     * @return true if successful; false if improper amount of good type in cargo hold
     */
    public boolean removeTradeGood(TradeGood good) {
        return ship.removeTradeGood(good);
    }
        
    /**
     * gets all the trade goods of the given type owned by the player
     * @param types types of tradegoods to get
     * @return true if any goods were removed
     */
    public boolean removeTradeGoodsByType(TradeGood[] types) {
        boolean removedSome = false;
        for(TradeGood a : types) {
            a.setAmount(-1);
            if(ship.getCargo().removeTradeGood(a))
                removedSome = true;
        }
        return removedSome;
    }
    
    /**
     * Get a list of cargo held by the player (null if player has no ship)
     * 
     * @return player's current cargo
     */
    public ArrayList<TradeGood> getCargoList() {
        if(ship == null)
            return null;
        return ship.getCargoList();
    }
    
    public String toString() {
        if(pilotSkill == 0 && fighterSkill == 0 &&
                traderSkill == 0 && engineerSkill == 0 &&
                investorSkill == 0) {
            return name;
        } else {
            return name + "\nMoney: $" + getMoney();
        }
    }
}
