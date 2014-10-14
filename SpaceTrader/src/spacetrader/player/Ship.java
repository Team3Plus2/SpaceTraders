/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

import java.util.Random;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
    private int damageToShields;
    
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
        damageToShields = 0;
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
     * @return the ships weapons
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }
    
    /**
     * @return the ships gadgets
     */
    public ArrayList<Gadget> getGadgets() {
        return gadgets;
    }
    
    public ArrayList<TradeGood> getCargoList() {
        return cargo.getCargoList();
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
    
    /**
     * Fire at the given ship
     * 
     * @param other ship to attack on
     * @param modifier damage modifier
     * @param targets the parts on the ship to shoot at. if null, targets at random
     * @return true if the enemy ship was destroyed, else false
     */
    public boolean attack(Ship other, int modifier, ArrayList targets) {
        if(modifier <= 0)
            modifier = 1;
        Random rand = new Random();
        int modifierSeed = rand.nextInt(modifier);
        
        return other.recieveDamage(getPower() + (modifierSeed/5), targets);
    }
    
    /**
     * Damages the ship the given amount
     * 
     * @param amount amount of damage to apply to the ship
     * @param targets damage the given targets once the shields are destroyed, if null, targets at random
     * @return true if the ship was destroyed, false if it survived
     */
    public boolean recieveDamage(int amount, ArrayList targets) {
        damageToShields += amount;
        
        if(!shields.isEmpty()) {
            Iterator<Shield> iter = shields.iterator();
            do {
                Shield a = iter.next();
                int absorbed = a.absorbDamage(damageToShields);
                if(absorbed < 0) {
                    damageToShields += absorbed;//add since the shield's destructioned is denoted through a negative absorbtion return
                    iter.remove();
                }
            } while(iter.hasNext());
        }
        
        if(shields.isEmpty() && damageToShields > 0) {
            if(targets != null) {
                for(Object a : targets) {
                    if(a instanceof Weapon) {
                        if(weapons.size() > 0) {
                            Weapon weap = (Weapon)a;
                            Iterator<Weapon> weapIter = weapons.iterator();
                            for(Weapon b = weapIter.next(); weapIter.hasNext(); b = weapIter.next()) {
                                if(b.getLaserType().equals(weap.getLaserType())) {
                                    weapIter.remove();
                                    damageToShields--;
                                }
                            }
                        }
                    } else if (a instanceof Gadget) {
                        if(gadgets.size() > 0) {
                            Gadget gadg = (Gadget)a;
                            Iterator<Gadget> gadgIter = gadgets.iterator();
                            for(Gadget b = gadgIter.next(); gadgIter.hasNext(); b = gadgIter.next()) {
                                if(b.getType().equals(gadg.getType())) {
                                    gadgIter.remove();
                                    damageToShields--;
                                }
                            }
                        }
                    }
                }
            }
            
            if(damageToShields > 0) {
                Random rand = new Random();
                int i = 0;
                for(; i < damageToShields; i++) {
                    boolean removed = false;
                    int type = rand.nextInt(1);
                    if(type == 0) {
                        if(!weapons.isEmpty()) {
                            weapons.remove(0);
                            removed = true;
                        } else if(!gadgets.isEmpty()) {
                            gadgets.remove(0);
                            removed = true;
                        }
                    } else {
                        if(!gadgets.isEmpty()) {
                            gadgets.remove(0);
                            removed = true;
                        } else if(!weapons.isEmpty()) {
                            weapons.remove(0);
                            removed = true;
                        }
                    }
                    if(!removed)
                        return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Damages shields
     * 
     * This is also when any destroyed shields are actually removed from the arraylist
     * @return the number of shields destroyed
     */
    /*public int damageShields() {
        Iterator<Shield> iter = shields.iterator();
        int removeCount = 0;
        for(Shield a = iter.next() ;iter.hasNext(); a = iter.next()) {
            int absorbed = a.absorbDamage(damageToShields);
            if(absorbed < 0) {
                damageToShields += absorbed;//add since the shield's destructioned is denoted through a negative absorbtion return
                iter.remove();
                removeCount++;
            } else {
                damageToShields -= absorbed;
            }
        }
        return removeCount;
    }*/
    
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
    
    /**
     * Calculates the ships power. A.K.A.
     * Gets the sum of the strength of the ships weapons.
     * @return the ships power
     */
    public int getPower() {
        int power = 0;
        for(Weapon a : weapons) {
            power += a.getStrength();
        }
        return power;
    }

    public int getMaxCargo() {
        return maxCargo;
    }

    public int getMaxMercenaries() {
        return maxMercenaries;
    }

    public int getMaxWeapons() {
        return maxWeapons;
    }

    public int getMaxShields() {
        return maxShields;
    }

    public int getMaxGadgets() {
        return maxGadgets;
    }
    
    
}
