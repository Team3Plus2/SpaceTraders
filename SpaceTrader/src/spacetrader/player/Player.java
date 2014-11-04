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
public class Player implements Serializable {
    /**
     * Fuel expended in between planets.
     */
    private static final float FUEL_PER_PLANET_MOVEMENT = 0.5f;
    
    /**
     * The player's name.
     */
    private String name;
    /**
     * The pilot skill of the player.
     */
    private int pilotSkill;
    /**
     * The fighter skill of the player.
     */
    private int fighterSkill;
    /**
     * The trader skill of the player.
     */
    private int traderSkill;
    /**
     * The engineer skill of the player.
     */
    private int engineerSkill;
    /**
     * The investor skill of the player.
     */
    private int investorSkill;
    /**
     * The current planet of the player.
     */
    private Planet currentPlanet;
    /**
     * The current solar system of the player.
     */
    private SolarSystem currentSolarSystem;
    /**
     * True if the player is dead.
     */
    private boolean dead;

    /**
     * The amount of money the player has.
     */
    private float money;
    /**
     * The player's ship.
     */
    private Ship ship;
    
    /**
     * Instantiates the player at game initialization.
     * 
     * @param name2 name
     * @param pilotSkill2 pilot skill
     * @param fighterSkill2 fighter skill
     * @param traderSkill2 trader skill
     * @param engineerSkill2 engineer skill
     * @param investorSkill2 investor skill
     */
    public Player(String name2, int pilotSkill2, int fighterSkill2, int traderSkill2,
                  int engineerSkill2, int investorSkill2) {
        this.name = name2;
        this.money = 1000;
        this.pilotSkill = pilotSkill2;
        this.fighterSkill = fighterSkill2;
        this.traderSkill = traderSkill2;
        this.engineerSkill = engineerSkill2;
        this.investorSkill = investorSkill2;
        this.ship = new Ship();
    }

    /**
     * Simulates a single round of attack on the given player.
     * @param other player to attack
     * @param targets parts of ship to target, if null, targets at random
     * @return true if the enemy ship is destroyed
     */
    public boolean attack(Player other, ArrayList targets) {
        int advantage = other.fighterSkill - this.fighterSkill; //damage modifier based on relitave skill
        return ship.attack(other.ship, advantage, targets);
    }
    
    /**
     * @return the damage to the ship's shields.
     */
    public int getDamageToShields() {
        return ship.getDamageToShields();
    }
    
    /**
     * @return the damage this ship dealt in the last attack.
     */
    public int getDamageDealt() {
        return ship.getDamageDealt();
    }
    
    /**
     * @return the most recently destroyed objects from the ship.
     */
    public ArrayList getDestroyed() {
        return ship.getDestroyed();
    }
    
    /**
     * Move the player to the given solar system.
     * 
     * @param system target solar system
     * @return true if player has enough fuel to travel and system != currentSolarSystem and planet != currentPlanet
     */
    public boolean move(SolarSystem system) {
        double distance = FUEL_PER_PLANET_MOVEMENT;
        if (system != currentSolarSystem) {
            distance = Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        }
        if (!ship.moveDistance(distance)) {
            return false;
        }
        currentSolarSystem = system;
        if (system.planets().length >= 1) {
            currentPlanet = system.planets()[0];
        } else {
            currentPlanet = null;
        }
        TurnEvent.nextTurn(this);
        return true;
    }
    
    /**
     * Move the player to the given planet and solar system.
     * 
     * @param system target solar system
     * @param planet target planet
     * @return true if player has enough fuel to travel and system != currentSolarSystem and planet != currentPlanet
     */
    public boolean move(SolarSystem system, Planet planet) {
//        double distance = FUEL_PER_PLANET_MOVEMENT;
        double distance = 0;
        if (system != currentSolarSystem) {
            distance = Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        }
        if (!ship.moveDistance(distance)) {
            return false;
        }
        currentSolarSystem = system;
        currentPlanet = planet;
        if (currentPlanet.getShipyard() == null) {
            currentPlanet.setShipyard(new Shipyard(currentSolarSystem.techLevel()));
        }
        if (currentPlanet.getMarket() == null) {
            currentPlanet.setMarket(new MarketPlace(currentSolarSystem.techLevel(), currentPlanet.resources()));
        }
        TurnEvent.nextTurn(this);
        return true;
    }
    
    /**
     * Move the player to the given planet in the current system.
     * 
     * @param planet target planet
     * @return true if player has enough fuel to travel to the planet
     */
    public boolean move(Planet planet) {
        return Player.this.move(currentSolarSystem, planet);
    }
    
    /**
     * Returns distance to solar system.
     * @param system the solar system to check distance of
     * @return the distance to the solar system
     */
    public float distanceToSolarSystem(SolarSystem system) {
        float distance;
        distance = (float) Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        return distance;
    }
    
    /**
     * @return The name of the player
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return The current solar system of the player.
     */
    public SolarSystem getCurrentSolarSystem() {
        return currentSolarSystem;
    }
    
    /**
     * @return The current planet of the player.
     */
    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    /**
     * Sets the current solar system to a new one.
     * 
     * @param currentSolarSystem2 the current solar system
     */
    public void setCurrentSolarSystem(SolarSystem currentSolarSystem2) {
        this.currentSolarSystem = currentSolarSystem2;
    }

    /**
     * Sets the name to a new name.
     * 
     * @param name2 the new name
     */
    public void setName(String name2) {
        this.name = name2;
    }
    
    /**
     * @return the money the player owns
     */
    public float getMoney() {
        return money;
    }
    
    /**
     * Sets the money to a new value.
     * 
     * @param money2 the new money value
     */
    public void setMoney(float money2) {
        this.money = money2;
    }

    /**
     * @return The pilot skill.
     */
    public int getPilotSkill() {
        return pilotSkill;
    }

    /**
     * Sets the pilot skill to a new value.
     * 
     * @param pilotSkill2 the new pilot skill
     */
    public void setPilotSkill(int pilotSkill2) {
        this.pilotSkill = pilotSkill2;
    }

    /**
     * @return The fighter skill.
     */
    public int getFighterSkill() {
        return fighterSkill;
    }

    /**
     * Sets the fighter skill to a new value.
     * 
     * @param fighterSkill2 the new fighter skill
     */
    public void setFighterSkill(int fighterSkill2) {
        this.fighterSkill = fighterSkill2;
    }

    /**
     * @return The trader skill.
     */
    public int getTraderSkill() {
        return traderSkill;
    }

    /**
     * Sets the trader skill to a new value.
     * 
     * @param traderSkill2 the new trader skill
     */
    public void setTraderSkill(int traderSkill2) {
        this.traderSkill = traderSkill2;
    }

    /**
     * @return The engineer skill.
     */
    public int getEngineerSkill() {
        return engineerSkill;
    }

    /**
     * Sets the engineer skill to a new value.
     * 
     * @param engineerSkill2 the new engineer skill
     */
    public void setEngineerSkill(int engineerSkill2) {
        this.engineerSkill = engineerSkill2;
    }

    /**
     * @return The investor skill.
     */
    public int getInvestorSkill() {
        return investorSkill;
    }

    /**
     * Sets the investor skill to a new value.
     * 
     * @param investorSkill2 the new investor skill
     */
    public void setInvestorSkill(int investorSkill2) {
        this.investorSkill = investorSkill2;
    }
    
    /**
     * @return The player's ship
     */
    public Ship getShip() {
        return ship;
    }
    
    /**
     * Sets the player's ship to a new one.
     * 
     * @param ship2 the new ship
     */
    public void setShip(Ship ship2) {
        this.ship = ship2;
    }
    
    /**
     * Gets an ArrayList of travelable solar systems.
     * 
     * @param universe the universe
     * @return an ArrayList of travelable solar systems
     */
    public ArrayList<SolarSystem> getTravelable(Universe universe) {
        int travelRadius = (int) ship.getFuel();
        ArrayList<SolarSystem> systems = new ArrayList<>();
        int x = currentSolarSystem.getX();
        int y = currentSolarSystem.getY();
        SparseIterator iter = universe.iterateFrom(x - travelRadius, y - travelRadius, x + travelRadius, y + travelRadius);
        for (SolarSystem i = iter.next(); iter.hasNext(); i = iter.next()) {
            if (i != null && ship.getFuel() - distanceToSolarSystem(i) >= 0) {
                systems.add(i);
            }
        }
        
        //can always travel to current system
        if (!systems.contains(currentSolarSystem)) {
            systems.add(currentSolarSystem);
        }
        
        return systems;
    }
    
    /**
     * Gets the players current travel radius based on the ship's fuel.
     * @return the player's travel radius (obtained from the ship)
     */
    public float getTravelRadius() {
        return ship.getFuel();
    }
    
    /**
     * Checks the players life readings.
     * @return true if the player is dead
     */
    public boolean isDead() {
        return dead;
    }
    
    /**
     * Kills the player.
     */
    public void die() {
        dead = true;
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
        return ship.getCargo().addTradeGood(good);
    }
    
    /**
     * Removes a trade good from the cargo hold if the proper amount exists.
     * 
     * @param good the trade good to be removed
     * @return true if successful; false if improper amount of good type in cargo hold
     */
    public boolean removeTradeGood(TradeGood good) {
        return ship.getCargo().removeTradeGood(good);
    }
        
    /**
     * gets all the trade goods of the given type owned by the player.
     * @param types types of tradegoods to get
     * @return true if any goods were removed
     */
    public boolean removeTradeGoodsByType(TradeGood[] types) {
        boolean removedSome = false;
        for (TradeGood a : types) {
            a.setAmount(-1);
            if (ship.getCargo().removeTradeGood(a)) {
                removedSome = true;
            }
        }
        return removedSome;
    }
    
    /**
     * Get a list of cargo held by the player (null if player has no ship).
     * 
     * @return player's current cargo
     */
    public ArrayList<TradeGood> getCargoList() {
        if (ship == null) {
            return null;
        }
        return ship.getCargoList();
    }
    
    @Override
    public String toString() {
        if (pilotSkill == 0 && fighterSkill == 0
                && traderSkill == 0 && engineerSkill == 0
                && investorSkill == 0) {
            return name;
        } else {
            return name + "\nMoney: $" + getMoney();
        }
    }
    
    
}
