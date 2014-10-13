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
 * Represents the generic ship that the player owns.
 * 
 * @author Aaron McAnally
 */
public class Ship implements Serializable {
    
    public static void Load() {
        ShipType.Load();
    }
    
    private float fuel;
    private int maxCargo, maxMercenaries, maxWeapons, maxShields, maxGadgets;
    private ArrayList<Weapon> weapons;
    private ArrayList<Shield> shields;
    private ArrayList<Gadget> gadgets;
    private ArrayList<Mercenary> mercenaries;
    private Cargo cargo;
    
    /**
     * Creates a new ship with basic stats.
     * 
     * @param ship the ShipType enum that stores the basic stats of the ship.
     */
    public Ship(ShipType ship) {
        fuel = ship.getMaxFuel();
        weapons = new ArrayList<Weapon>(ship.getMaxWeapons());
        shields = new ArrayList<Shield>(ship.getMaxShields());
        gadgets = new ArrayList<Gadget>(ship.getMaxGadgets());
        mercenaries = new ArrayList<Mercenary>(ship.getMaxMercenaries());
        maxCargo = ship.getMaxCargo();
        maxWeapons = ship.getMaxWeapons();
        maxShields = ship.getMaxShields();
        maxGadgets = ship.getMaxGadgets();
        maxMercenaries = ship.getMaxMercenaries();
        cargo = new Cargo(maxCargo);
    }
    
    /**
     * Creates a ship of type GNAT (the starter ship for the player)
     */
    public Ship() {
        this(ShipType.Default());
    }
    /**
     * Puts a weapon in an open weapon slot if one is available.
     * 
     * @param weapon the weapon to be added
     * @return false if there is no more room for weapons
     */
    public boolean addWeapon(Weapon weapon) {
        if (weapons.size() < maxWeapons) {
            return weapons.add(weapon);
        } else {
            return false;
        }
    }
    
    /**
     * Removes a weapon from the given weapon slot.
     * 
     * @param slot the slot in which the weapon is located
     * @return the removed weapon; null if slot was already empty
     */
    public Weapon removeWeapon(int slot) {
        return weapons.remove(slot);
    }
    
    /**
     * Puts a shield in an open shield slot if one is available.
     * 
     * @param shield the shield to be added
     * @return false if there is no more room for shields
     */
    public boolean addShield(Shield shield) {
        if (shields.size() < maxShields) {
            return shields.add(shield);
        } else {
            return false;
        }
    }
    
    /**
     * Removes a shield from the given shield slot.
     * 
     * @param slot the slot in which the shield is located
     * @return the removed shield; null if slot was already empty
     */
    public Shield removeShield(int slot) {
        return shields.remove(slot);
    }
    
    /**
     * Puts a gadget in an open gadget slot if one is available.
     * 
     * @param gadget the gadget to be added
     * @return false if there is no more room for gadgets
     */
    public boolean addGadget(Gadget gadget) {
        if (gadgets.size() < maxGadgets) {
            return gadgets.add(gadget);
        } else {
            return false;
        }
    }
    
    /**
     * Removes a gadget from the given gadget slot.
     * 
     * @param slot the slot in which the gadget is located
     * @return the removed gadget; null if slot was already empty
     */
    public Gadget removeGadget(int slot) {
        return gadgets.remove(slot);
    }
    
    /**
     * Hires a mercenary in an open mercenary slot if one is available.
     * 
     * @param mercenary the mercenary to be added
     * @return false if there is no more room for mercenaries
     */
    public boolean hireMercenary(Mercenary mercenary) {
        if (mercenaries.size() < maxMercenaries) {
            return mercenaries.add(mercenary);
        } else {
            return false;
        }
    }
    
    /**
     * Removes a mercenary from the given mercenary slot.
     * 
     * @param slot the slot in which the mercenary is located
     * @return the fired mercenary; null if slot was already empty
     */
    public Mercenary fireMercenary(int slot) {
        return mercenaries.remove(slot);
    }
    
    /**
     * @return the cargo hold object of the ship
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Moves the ship the given distance if the player has enough fuel
     * @param dist
     * @return true if the movement occured, false if ship doesn't have enough fuel
     */
    public boolean moveDistance(double dist) {
        if(dist <= fuel) {
            fuel -= dist;
            return true;
        }
        return false;
    }
    
    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
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
        return cargo.addTradeGood(good);
    }
    
    /**
     * Removes a trade good from the cargo hold if the proper amount exists.
     * 
     * @param good the trade good to be removed
     * @return true if successful; false if improper amount of good type in cargo hold
     */
    public boolean removeTradeGood(TradeGood good) {
        return cargo.removeTradeGood(good);
    }
}
