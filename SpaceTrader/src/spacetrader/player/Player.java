/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.player;

import java.util.Iterator;
import java.util.ArrayList;

import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.turns.TurnEvent;

/**
 * The main player and everything it owns.
 * 
 * @author Carey MacDonald
 * @author Aaron McAnally
 */
public class Player {
    private static final float FUEL_PER_PLANET_MOVEMENT = 10;
    
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
     * Move the player to the given planet and solar system
     * 
     * @param system target solar system
     * @param planet target planet
     * @return true if player has enough fuel to travel
     */
    public boolean movePlayer(SolarSystem system, Planet planet) {
        double distance = FUEL_PER_PLANET_MOVEMENT;
        if(system != currentSolarSystem)
            distance = Math.sqrt(Math.pow(system.getX() - currentSolarSystem.getX(), 2) + Math.pow(system.getY() - currentSolarSystem.getY(), 2));
        if(!ship.moveDistance(distance))
            return false;
        currentSolarSystem = system;
        currentPlanet = planet;
        TurnEvent.NextTurn();
        return true;
    }
    
    /**
     * Move the player to the given planet in the current system
     * 
     * @param planet target planet
     * @return true if player has enough fuel to travel to the planet
     */
    public boolean movePlayer(Planet planet) {
        return movePlayer(currentSolarSystem, planet);
    }
    
    public String getName() {
        return name;
    }
    
    public SolarSystem getCurrentSolarSystem() {
        return currentSolarSystem;
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
        Iterator<SolarSystem> iter = universe.iterateFrom(x - travelRadius, y - travelRadius, x + travelRadius, y + travelRadius);
        for(SolarSystem i = iter.next();iter.hasNext(); i = iter.next()) {
            if(i != null)
                systems.add(i);
        }
        return systems;
    }
}
