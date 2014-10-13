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
        goods = TradeGood.getTradeGoodTypes();
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
                    goods.get(i).setPrice(good.getCurrentPriceEach());
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Remove the given trade good from the cargohold
     * @param good good to remove, if amount is -1, will remove all
     * @return true if a good is removed
     */
    public boolean removeTradeGood(TradeGood good) {
        boolean success = false;
        for (int i = 0; i < 10; i++) {
            if (goods.get(i).getName().equals(good.getName())) {
                if (goods.get(i).getAmount() >= good.getAmount()) {
                    goods.get(i).setAmount(goods.get(i).getAmount() - good.getAmount());
                    success = true;
                } else if(good.getAmount() == -1) {
                    goods.get(i).setAmount(0);
                    success = true;
                }
            }
        }
        return success;
    }
    
    /**
     * @return the cargo ArrayList of trade goods
     */
    public ArrayList<TradeGood> getCargoList() {
        return goods;
    }
    
    /**
     * Meant for use by the SolarSystemViewController in Marketplace view
     * 
     * @return ArrayList of trade goods with amount greater than 0
     */
    public ArrayList<TradeGood> getNonEmptyCargoList() {
        ArrayList<TradeGood> cargo = new ArrayList<TradeGood>();
        for (TradeGood good: goods) {
            if (good.getAmount() > 0) {
                cargo.add(good);
            }
        }
        return cargo;
    }
}
