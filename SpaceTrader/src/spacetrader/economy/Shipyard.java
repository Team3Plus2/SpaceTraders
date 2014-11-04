/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.economy;

import java.io.Serializable;
import java.util.ArrayList;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.player.Cargo;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;
import spacetrader.player.Upgrade;
import spacetrader.player.Weapon;

/**
 * Exists on every planet. Can be used to buy new ships and refuel.
 * 
 * @author Aaron McAnally
 */
public class Shipyard implements Serializable {
    
    /**
     * List of ShipTypes.
     */
    private final ArrayList<ShipType> shipTypes;
    /**
     * List of Weapons this Shipyard can sell.
     */
    private final ArrayList<Weapon> weapons;
    /**
     * List of Shields this Shipyard can sell.
     */
    private final ArrayList<Shield> shields;
    /**
     * List of Gadgets this Shipyard can sell.
     */
    private final ArrayList<Gadget> gadgets;
    
    /**
     * Creates a shipyard of available ship types.
     * 
     * @param techLevel techLevel of the system
     */
    public Shipyard(TechLevel techLevel) {
        shipTypes = new ArrayList<ShipType>();
        ArrayList<ShipType> ships = ShipType.getShipTypes();
        for (ShipType type: ships) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                shipTypes.add(type);
            }
        }
        weapons = new ArrayList<Weapon>();
        ArrayList<Weapon> weaponTypes = Weapon.getWeaponTypes();
        for (Weapon type: weaponTypes) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                weapons.add(type);
            }
        }
        shields = new ArrayList<Shield>();
        ArrayList<Shield> shieldTypes = Shield.getShieldTypes();
        for (Shield type: shieldTypes) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                shields.add(type);
            }
        }
        gadgets = new ArrayList<Gadget>();
        ArrayList<Gadget> gadgetTypes = Gadget.getGadgetTypes();
        for (Gadget type: gadgetTypes) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                gadgets.add(type);
            }
        }
    }
    
    /**
     * Gets the list of ships sold in this shipyard.
     * 
     * @return list of available ship types
     */
    public ArrayList<ShipType> getListShipsAvailable() {
        return shipTypes;
    }
    
    /**
     * Gets the list of upgrades sold in this shipyard.
     * 
     * @return list of available upgrade types
     */
    public ArrayList<Upgrade> getListUpgradesAvailable() {
        ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
        upgrades.addAll(weapons);
        upgrades.addAll(shields);
        upgrades.addAll(gadgets);
        return upgrades;
    }
    
    /**
     * Purchase a new ship at the shipyard.
     * 
     * @param p the player
     * @param type the ShipType being purchased
     * @return true if successful; false if not enough money or too much cargo owned
     */
    public boolean buyShip(Player p, ShipType type) {
        Cargo cargo = p.getShip().getCargo();
        if (p.getMoney() >= type.getPrice() && cargo.getNumFilled() <= type.getMaxCargo()
                && !p.getShip().getName().equals(type.getName())) {
            cargo.setMax(type.getMaxCargo());
            Ship ship = new Ship(type);
            ship.setCargo(cargo);
            p.setShip(ship);
            p.setMoney(p.getMoney() - type.getPrice());
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Adds a weapon to the ship if player has enough money and the ship has enough weapon slots.
     * 
     * @param p the player
     * @param weapon the weapon to be added
     * @return true if successful; false if not enough money or weapon slots
     */
    public boolean buyWeapon(Player p, Weapon weapon) {
        if (p.getMoney() >= weapon.getPrice()) {
            if (p.getShip().addWeapon(weapon)) {
                p.setMoney(p.getMoney() - weapon.getPrice());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a shield to the ship if player has enough money and the ship has enough shield slots.
     * 
     * @param p the player
     * @param shield the shield to be added
     * @return true if successful; false if not enough money or shield slots
     */
    public boolean buyShield(Player p, Shield shield) {
        if (p.getMoney() >= shield.getPrice()) {
            if (p.getShip().addShield(shield)) {
                p.setMoney(p.getMoney() - shield.getPrice());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a gadget to the ship if player has enough money and the ship has enough gadget slots.
     * 
     * @param p the player
     * @param gadget the gadget to be added
     * @return true if successful; false if not enough money or gadget slots
     */
    public boolean buyGadget(Player p, Gadget gadget) {
        if (p.getMoney() >= gadget.getPrice()) {
            if (p.getShip().addGadget(gadget)) {
                p.setMoney(p.getMoney() - gadget.getPrice());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Purchase fuel at the shipyard.
     * 
     * @param p the player
     * @param amount amount of fuel purchased
     * @return true if successful; false if not enough money or fuel tank size
     */
    public boolean buyFuel(Player p, int amount) {
        Ship ship = p.getShip();
        if (p.getMoney() >= ship.getFuelCost() && ship.getFuel() + amount <= ship.getMaxFuel()) {
            ship.setFuel(ship.getFuel() + amount);
            return true;
        } else {
            return false;
        }
    }
    
}
