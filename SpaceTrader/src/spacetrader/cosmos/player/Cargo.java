/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.cosmos.player;

/**
 * An object that allows for easier access to a ship's cargo hold.
 * 
 * @author Aaron McAnally
 */
public class Cargo {
    private int maxCargo, slotsFilled;
    private int water, furs, food, ore, games, firearms,
                medicine, machines, narcotics, robots;
    
    public Cargo(int maxCargo) {
        this.maxCargo = maxCargo;
        slotsFilled = water = furs = food = ore = games = firearms
                = medicine = machines = narcotics = robots = 0;
    }
    
    /**
     * @return the total number of cargo holds on the ship
     */
    public int getMax() {
        return maxCargo;
    }
    
    /**
     * Changes the number of slots for cargo on the ship only if 
     * the number of filled slots is not over the new capacity
     * 
     * @param maxCargo the new total number of cargo holds on the ship
     * @return true if successful; 
     *       false if there are more slots filled than the new max size can hold
     */
    public boolean setMax(int maxCargo) {
        if (getNumFilled() <= maxCargo) {
            this.maxCargo = maxCargo;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of filled cargo hold slots
     */
    public int getNumFilled() {
        return slotsFilled;
    }
    
    /**
     * @return the number of empty cargo hold slots remaining
     */
    public int getNumEmpty() {
        return maxCargo - slotsFilled;
    }
    
    /**
     * @return the number of water goods on the ship
     */
    public int getNumWater() {
        return water;
    }
    
    /**
     * 
     * @param numWater - the new amount of water stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumWater(int numWater) {
        if (slotsFilled - water + numWater < maxCargo) {
            slotsFilled = slotsFilled - water + numWater;
            water = numWater;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of furs goods on the ship
     */
    public int getNumFurs() {
        return furs;
    }
    
    /**
     * 
     * @param numFurs - the new amount of furs stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumFurs(int numFurs) {
        if (slotsFilled - furs + numFurs < maxCargo) {
            slotsFilled = slotsFilled - furs + numFurs;
            furs = numFurs;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of furs goods on the ship
     */
    public int getNumFood() {
        return food;
    }
    
    /**
     * 
     * @param numFood - the new amount of food stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumFood(int numFood) {
        if (slotsFilled - food + numFood < maxCargo) {
            slotsFilled = slotsFilled - food + numFood;
            food = numFood;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of ore goods on the ship
     */
    public int getNumOre() {
        return ore;
    }
    
    /**
     * 
     * @param numOre - the new amount of ore stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumOre(int numOre) {
        if (slotsFilled - ore + numOre < maxCargo) {
            slotsFilled = slotsFilled - ore + numOre;
            ore = numOre;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of games goods on the ship
     */
    public int getNumGames() {
        return games;
    }
    
    /**
     * 
     * @param numGames - the new amount of games stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumGames(int numGames) {
        if (slotsFilled - games + numGames < maxCargo) {
            slotsFilled = slotsFilled - games + numGames;
            games = numGames;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of firearms goods on the ship
     */
    public int getNumFirearms() {
        return firearms;
    }
    
    /**
     * 
     * @param numFirearms - the new amount of firearms stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumFirearms(int numFirearms) {
        if (slotsFilled - firearms + numFirearms < maxCargo) {
            slotsFilled = slotsFilled - firearms + numFirearms;
            firearms = numFirearms;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of medicine goods on the ship
     */
    public int getNumMedicine() {
        return medicine;
    }
    
    /**
     * 
     * @param numMedicine - the new amount of medicine stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumMedicine(int numMedicine) {
        if (slotsFilled - medicine + numMedicine < maxCargo) {
            slotsFilled = slotsFilled - medicine + numMedicine;
            medicine = numMedicine;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of machines goods on the ship
     */
    public int getNumMachines() {
        return machines;
    }
    
    /**
     * 
     * @param numMachines - the new amount of machines stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumMachines(int numMachines) {
        if (slotsFilled - machines + numMachines < maxCargo) {
            slotsFilled = slotsFilled - machines + numMachines;
            machines = numMachines;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of narcotics goods on the ship
     */
    public int getNumNarcotics() {
        return water;
    }
    
    /**
     * 
     * @param numNarcotics - the new amount of narcotics stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumNarcotics(int numNarcotics) {
        if (slotsFilled - narcotics + numNarcotics < maxCargo) {
            slotsFilled = slotsFilled - narcotics + numNarcotics;
            narcotics = numNarcotics;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return the number of robots goods on the ship
     */
    public int getNumRobots() {
        return robots;
    }
    
    /**
     * 
     * @param numRobots - the new amount of robots stored on the ship
     * @return true if successful;
     *         false if failed: this new amount puts total cargo over capacity
     */
    public boolean setNumRobots(int numRobots) {
        if (slotsFilled - robots + numRobots < maxCargo) {
            slotsFilled = slotsFilled - robots + numRobots;
            robots = numRobots;
            return true;
        } else {
            return false;
        }
    }
}
