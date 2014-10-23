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
import spacetrader.player.Player;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;

/**
 * Exists on every planet. Can be used to buy new ships and refuel.
 * 
 * @author Aaron McAnally
 */
public class Shipyard implements Serializable {
    
    private ArrayList<ShipType> shipTypes;
    
    /**
     * Creates a shipyard of available ship types
     * 
     * @param techLevel techLevel of the system
     */
    public Shipyard(TechLevel techLevel) {
        shipTypes = new ArrayList<ShipType>();
        ArrayList<ShipType> types = ShipType.getShipTypes();
        for (ShipType type: types) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                shipTypes.add(type);
            }
        }
    }
    
    /**
     * Gets the list of ships sold in this shipyard
     * 
     * @return list of available ship types
     */
    public ArrayList<ShipType> getListAvailable() {
        return shipTypes;
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
