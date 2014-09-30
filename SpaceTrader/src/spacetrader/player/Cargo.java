/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

import java.util.ArrayList;
import spacetrader.economy.TradeGood;

/**
 * An object that allows for easier access to a ship's cargo hold.
 * 
 * @author Aaron McAnally
 */
public class Cargo {
    private int maxCargo;
    private ArrayList<TradeGood> goods;
    
    public Cargo(int maxCargo) {
        this.maxCargo = maxCargo;
        goods = new ArrayList<TradeGood>();
        goods.add(new TradeGood("water"));
        goods.add(new TradeGood("furs"));
        goods.add(new TradeGood("food"));
        goods.add(new TradeGood("ore"));
        goods.add(new TradeGood("games"));
        goods.add(new TradeGood("firearms"));
        goods.add(new TradeGood("medicine"));
        goods.add(new TradeGood("machines"));
        goods.add(new TradeGood("narcotics"));
        goods.add(new TradeGood("robots"));
        
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
        int numFilled = 0;
        for (TradeGood good: goods) {
            numFilled += good.getAmount();
        }
        return numFilled;
    }
    
    /**
     * @return the number of empty cargo hold slots remaining
     */
    public int getNumEmpty() {
        return maxCargo - getNumFilled();
    }
    
    /**
     * Adds a set of goods of a particular type to the cargo hold if there is room
     * 
     * @param good - the good to be added to the cargo hold
     * @return true if successful; false if failed - not enough room in cargo hold
     */
    public boolean addTradeGood(TradeGood good) {
        if (good.getAmount() <= getNumEmpty()) {
            for (int i = 0; i < 10; i++) {
                if (goods.get(i).getName().equals(good.getName())) {
                    goods.get(i).setAmount(goods.get(i).getAmount() + good.getAmount());
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeTradeGood(TradeGood good) {
        boolean success = false;
        for (int i = 0; i < 10; i++) {
            if (goods.get(i).getName().equals(good.getName())) {
                 if (goods.get(i).getAmount() >= good.getAmount()) {
                     goods.get(i).setAmount(goods.get(i).getAmount() - good.getAmount());
                     success = true;
                 }
            }
        }
        return success;
    }
    
    /**
     * @return the number of water goods on the ship
     */
    public int getNumWater() {
        return goods.get(0).getAmount();
    }
    
    /**
     * @return the number of furs goods on the ship
     */
    public int getNumFurs() {
        return goods.get(1).getAmount();
    }
    
    /**
     * @return the number of furs goods on the ship
     */
    public int getNumFood() {
        return goods.get(2).getAmount();
    }
    
    /**
     * @return the number of ore goods on the ship
     */
    public int getNumOre() {
        return goods.get(3).getAmount();
    }
    
    /**
     * @return the number of games goods on the ship
     */
    public int getNumGames() {
        return goods.get(4).getAmount();
    }
    
    /**
     * @return the number of firearms goods on the ship
     */
    public int getNumFirearms() {
        return goods.get(5).getAmount();
    }

    /**
     * @return the number of medicine goods on the ship
     */
    public int getNumMedicine() {
        return goods.get(6).getAmount();
    }

    /**
     * @return the number of machines goods on the ship
     */
    public int getNumMachines() {
        return goods.get(7).getAmount();
    }

    /**
     * @return the number of narcotics goods on the ship
     */
    public int getNumNarcotics() {
        return goods.get(8).getAmount();
    }
    
    /**
     * @return the number of robots goods on the ship
     */
    public int getNumRobots() {
        return goods.get(9).getAmount();
    }
}
