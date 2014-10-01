/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

import spacetrader.economy.TradeGood;
import java.util.ArrayList;

/**
 * Represents the generic ship that the player owns.
 * 
 * @author Aaron McAnally
 */
public class Ship {
    
    /**
     * An enum for ship types with basic stats:
     *      maxCargo, maxFuel, maxWeapons, maxShields, maxGadgets, maxMercenaries
     */
    public enum ShipType {
        FLEA(0, 20, 0, 0, 0, 0),
        GNAT(15, 14, 1, 0, 1, 0),
        FIREFLY(20, 17, 1, 1, 1, 0),
        MOSQUITO(15, 13, 2, 1, 1, 0),
        BUMBLEBEE(20, 15, 1, 2, 2, 1),
        BEETLE(50, 14, 0, 1, 1, 3),
        HORNET(20, 16, 3, 2, 1, 2),
        GRASSHOPPER(30, 15, 2, 1, 3, 3),
        TERMITE(60, 13, 1, 3, 2, 3),
        WASP(35, 14, 3, 2, 2, 3);
        
        private final int maxCargo, maxWeapons, maxShields, maxGadgets, maxMercenaries;
        private final float maxFuel;
        private ShipType(int maxCargo, float maxFuel, int maxWeapons, int maxShields, int maxGadgets, int maxMercenaries) {
            this.maxCargo = maxCargo;
            this.maxFuel = maxFuel;
            this.maxWeapons = maxWeapons;
            this.maxShields = maxShields;
            this.maxGadgets = maxGadgets;
            this.maxMercenaries = maxMercenaries;
        }
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
        fuel = ship.maxFuel;
        cargo = new Cargo(maxCargo);
        weapons = new ArrayList<Weapon>(ship.maxWeapons);
        shields = new ArrayList<Shield>(ship.maxShields);
        gadgets = new ArrayList<Gadget>(ship.maxGadgets);
        mercenaries = new ArrayList<Mercenary>(ship.maxMercenaries);
        maxCargo = ship.maxCargo;
        maxWeapons = ship.maxWeapons;
        maxShields = ship.maxShields;
        maxGadgets = ship.maxGadgets;
        maxMercenaries = ship.maxMercenaries;
    }
    
    /**
     * Creates a ship of type GNAT (the starter ship for the player)
     */
    public Ship() {
        this(ShipType.GNAT);
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

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }
}
