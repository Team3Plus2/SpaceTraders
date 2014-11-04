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
    
    /**
     * Loads the ship type.
     */
    public static void load() {
        ShipType.load();
    }
    
    /**
     * the ship's max fuel.
     */
    private float maxFuel;
    /**
     * the ship's current fuel.
     */
    private float fuel;
    /**
     * the ship's price.
     */
    private float price;
    /**
     * the ship's fuel cost.
     */
    private float fuelCost;
    /**
     * the ship's max mercenaries.
     */
    private int maxMercenaries;
    /**
     * the ship's max weapons.
     */
    private int maxWeapons;
    /**
     * the ship's max shields.
     */
    private int maxShields;
    /**
     * the ship's max gadgets.
     */
    private int maxGadgets;
    /**
     * the ship's weapons.
     */
    private ArrayList<Weapon> weapons;
    /**
     * the ship's shields.
     */
    private ArrayList<Shield> shields;
    /**
     * the ship's gadgets.
     */
    private ArrayList<Gadget> gadgets;
    /**
     * the ship's mercenaries.
     */
    private ArrayList<Mercenary> mercenaries;
    /**
     * an arraylist of objects destroyed the last time this ship was damaged.
     */
    private ArrayList destroyed;
    /**
     * the ship's cargo.
     */
    private Cargo cargo;
    /**
     * the ship's damage to shields.
     */
    private int damageToShields;
    /**
     * damage this ship dealt in last attack.
     */
    private int damageFromLastAttack;
    /**
     * the ship's name.
     */
    private String name;
    
    /**
     * Creates a new ship with basic stats.
     * 
     * @param type the ShipType enum that stores the basic stats of the ship.
     */
    public Ship(ShipType type) {
        name = type.getName();
        maxFuel = fuel = type.getMaxFuel();
        weapons = new ArrayList<Weapon>(type.getMaxWeapons());
        shields = new ArrayList<Shield>(type.getMaxShields());
        gadgets = new ArrayList<Gadget>(type.getMaxGadgets());
        mercenaries = new ArrayList<Mercenary>(type.getMaxMercenaries());
        maxWeapons = type.getMaxWeapons();
        maxShields = type.getMaxShields();
        maxGadgets = type.getMaxGadgets();
        maxMercenaries = type.getMaxMercenaries();
        cargo = new Cargo(type.getMaxCargo());
        damageToShields = 0;
        price = type.getPrice();
        fuelCost = type.getFuelCost();
    }
    
    /**
     * Creates a ship of type GNAT (the starter ship for the player).
     */
    public Ship() {
        this(ShipType.defaultValue());
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
            if (gadget.getGadgetName().equals("CARGO")) {
                getCargo().setMax(getCargo().getMax() + 5);
            } //else if (gadget.getGadgetName().equals("NAVIGATION")) {
//                
//            } else if (gadget.getGadgetName().equals("AUTOREPAIR")) {
//                
//            } else if (gadget.getGadgetName().equals("TARGETING")) {
//                
//            } else if (gadget.getGadgetName().equals("CLOAKING")) {
//                
//            }
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
    
    /**
     * @return items in the cargo hold
     */
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
     * @param cargo2 the new cargo for the ship
     */
    public void setCargo(Cargo cargo2) {
        this.cargo = cargo2;
    }

    /**
     * Moves the ship the given distance if the player has enough fuel.
     * @param dist the move distance
     * @return true if the movement occurred, false if ship doesn't have enough fuel
     */
    public boolean moveDistance(double dist) {
        if (dist <= fuel) {
            fuel -= dist;
            return true;
        }
        return false;
    }
    
    /**
     * Fire at the given ship.
     * 
     * @param other ship to attack on
     * @param modifier damage modifier
     * @param targets the parts on the ship to shoot at. if null, targets at random
     * @return true if the enemy ship was destroyed, else false
     */
    public boolean attack(Ship other, int modifier, ArrayList targets) {
        int newModifier = modifier;
        if (newModifier <= 0) {
            newModifier = 1;
        }
        Random rand = new Random();
        int modifierSeed = rand.nextInt(newModifier);
        damageFromLastAttack = getPower() + (modifierSeed / 5); //damage from this attack now
        return other.recieveDamage(damageFromLastAttack, targets);
    }
    
    /**
     * Damages the ship the given amount.
     * 
     * @param amount amount of damage to apply to the ship
     * @param targets damage the given targets once the shields are destroyed, if null, targets at random
     * @return true if the ship was destroyed, false if it survived
     */
    public boolean recieveDamage(int amount, ArrayList targets) {
        destroyed = new ArrayList();
        damageToShields += amount;
        
        //damage shields
        if (!shields.isEmpty()) {
            Iterator<Shield> iter = shields.iterator();
            do {
                Shield a = iter.next();
                int absorbed = a.absorbDamage(damageToShields);
                if (absorbed < 0) {
                    damageToShields += absorbed; //add since the shield's destructioned is denoted through a negative absorbtion return
                    destroyed.add(a);
                    iter.remove();
                }
            } while (iter.hasNext());
        }
        
        if (shields.isEmpty() && damageToShields > 0) {
            //damage any targets
            if (targets != null) {
                damageShipComponents(targets);
            }
            
            if (damageToShields > 0 && damageShipComponentsAtRandom()) {
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Damage weapons and gadgets in the given arraylist.
     * @param damage damage to give weapons and gadgets
     * @param targets systems to target
     */
    private void damageShipComponents(ArrayList targets) {
        for (Object a : targets) {
            if (a instanceof Weapon) {
                if (weapons.size() > 0) {
                    Weapon weap = (Weapon) a;
                    Iterator<Weapon> weapIter = weapons.iterator();
                    for (Weapon b = weapIter.next(); weapIter.hasNext(); b = weapIter.next()) {
                        if (b.getLaserType().equals(weap.getLaserType())) {
                            destroyed.add(b);
                            weapIter.remove();
                            damageToShields--;
                        }
                    }
                }
            } else if (a instanceof Gadget && gadgets.size() > 0) {
                Gadget gadg = (Gadget) a;
                Iterator<Gadget> gadgIter = gadgets.iterator();
                for (Gadget b = gadgIter.next(); gadgIter.hasNext(); b = gadgIter.next()) {
                    if (b.getType().equals(gadg.getType())) {
                        destroyed.add(b);
                        gadgIter.remove();
                        damageToShields--;
                    }
                }
            }
        }
    }
    
    /**
     * Damage weapons and gadgets on the ship at random.
     * @return true if systems absorbed damage, false if damage reached ship
     */
    private boolean damageShipComponentsAtRandom() {
        Random rand = new Random();
        int i = 0;
        for (; i < damageToShields; i++) {
            boolean removed = false;
            int type = rand.nextInt(1);
            if (type == 0) {
                if (!weapons.isEmpty()) {
                    destroyed.add(weapons.remove(0));
                    removed = true;
                } else if (!gadgets.isEmpty()) {
                    destroyed.add(gadgets.remove(0));
                    removed = true;
                }
            } else {
                if (!gadgets.isEmpty()) {
                    destroyed.add(gadgets.remove(0));
                    removed = true;
                } else if (!weapons.isEmpty()) {
                    destroyed.add(weapons.remove(0));
                    removed = true;
                }
            }
            if (!removed) {
                return true;
            }
        }
        damageToShields -= i;
        return false;
    }
    
    /**
     * @return the items destroyed in the last round of combat
     */
    public ArrayList getDestroyed() {
        return destroyed;
    }
    
    /**
     * @return returns the pending damage to the shields
     */
    public int getDamageToShields() {
        return damageToShields;
    }
    
    /**
     * @return the damage this ship dealt in the last attack
     */
    public int getDamageDealt() {
        return damageFromLastAttack;
    }
    
    /**
     * @return the ships current fuel supply
     */
    public float getFuel() {
        return fuel;
    }

    /**
     * @param fuel2 the new fuel amount for the ship
     */
    public void setFuel(float fuel2) {
        this.fuel = fuel2;
    }
    
    /**
     * Calculates the ships power. A.K.A.
     * Gets the sum of the strength of the ships weapons.
     * @return the ships power
     */
    public int getPower() {
        int power = 0;
        for (Weapon a : weapons) {
            power += a.getStrength();
        }
        return power;
    }

    /**
     * @return the ship's max mercenaries
     */
    public int getMaxMercenaries() {
        return maxMercenaries;
    }

    /**
     * @return the ship's max weapons
     */
    public int getMaxWeapons() {
        return maxWeapons;
    }
    
    /**
     * @return the ship's number of weapons filled
     */
    public int getWeaponsFilled() {
        return weapons.size();
    }

    /**
     * @return the ship's max shields
     */
    public int getMaxShields() {
        return maxShields;
    }
    
    /**
     * @return the ship's number of shields filled
     */
    public int getShieldsFilled() {
        return shields.size();
    }

    /**
     * @return the ship's max gadgets
     */
    public int getMaxGadgets() {
        return maxGadgets;
    }
    
    /**
     * @return the ship's number of gadgets filled
     */
    public int getGadgetsFilled() {
        return gadgets.size();
    }
    
    /**
     * @return the ship's price
     */
    public float getPrice() {
        return price;
    }
    
    /**
     * @return the ship's cost for fuel
     */
    public float getFuelCost() {
        return fuelCost;
    }
    
    /**
     * @return the ship's max fuel
     */
    public float getMaxFuel() {
        return maxFuel;
    }
    
    @Override
    public String toString() {
        return name + "\nGadgets: " + getGadgets().toString()
                + "\nWeapons: " + getWeapons().toString();
    }
    
    /**
     * @return the ship's name
     */
    public String getName() {
        return name;
    }
    
}
