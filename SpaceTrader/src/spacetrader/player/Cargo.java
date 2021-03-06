/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

import java.io.Serializable;
import java.util.ArrayList;
import spacetrader.economy.TradeGood;

/**
 * An object that allows for easier access to a ship's cargo hold.
 * 
 * @author Aaron McAnally
 */
public class Cargo implements Serializable {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 90L;
    
    /**
     * The maximum size for this Cargo.
     */
    private int maxCargo;
    /**
     * The list of goods in this Cargo.
     */
    private ArrayList<TradeGood> goods;
    
    /**
     * Constructor for Cargo.
     * 
     * @param maxCargo2 the maximum size this cargo will be able to hold.
     */
    public Cargo(int maxCargo2) {
        this.maxCargo = maxCargo2;
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
     * the number of filled slots is not over the new capacity.
     * 
     * @param maxCargo2 the new total number of cargo holds on the ship
     * @return true if successful; 
     *       false if there are more slots filled than the new max size can hold
     */
    public boolean setMax(int maxCargo2) {
        if (getNumFilled() <= maxCargo2) {
            this.maxCargo = maxCargo2;
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
     * Adds a set of goods of a particular type to the cargo hold if there is room.
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
     * Removes a trade good from the cargo hold if the proper amount exists
     * or remove all if the passed trade good's amount is -1.
     * @param good the trade good to be removed
     * @return true if successful; false if improper amount of good type in cargo hold
     */
    public boolean removeTradeGood(TradeGood good) {
        boolean success = false;
        for (int i = 0; i < 10; i++) {
            if (goods.get(i).getName().equals(good.getName()) && goods.get(i).getAmount() != 0) {
                if (good.getAmount() < 0) {
                    goods.get(i).setAmount(0);
                    success = true;
                } else if (goods.get(i).getAmount() >= good.getAmount()) {
                    goods.get(i).setAmount(goods.get(i).getAmount() - good.getAmount());
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
     * Meant for use by the SolarSystemViewController in Marketplace view.
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
