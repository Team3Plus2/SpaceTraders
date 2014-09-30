/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.player;

import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;

/**
 * The main player and everything it owns.
 * 
 * @author Carey MacDonald
 * @author Aaron McAnally
 */
public class Player {
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
}
