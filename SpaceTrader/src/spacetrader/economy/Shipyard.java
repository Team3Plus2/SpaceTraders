/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.economy;

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
public class Shipyard {
    
    private ArrayList<ShipType> shipTypes;
    
    /**
     * Creates a shipyard of ship types
     */
    public Shipyard() {
        shipTypes = ShipType.getShipTypes();
    }
    
    /**
     * Gets the list of ships sold in this shipyard
     * 
     * @param techLevel techLevel of the system
     * @return list of available ship types
     */
    public ArrayList<ShipType> getListAvailable(TechLevel techLevel) {
        ArrayList<ShipType> types = new ArrayList<ShipType>();
        for (ShipType type: shipTypes) {
            if (type.getTechLevel() <= TechLevel.getIndex(techLevel)) {
                types.add(type);
            }
        }
        return types;
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
        if (p.getMoney() >= type.getPrice() && cargo.getNumFilled() <= type.getMaxCargo()) {
            cargo.setMax(type.getMaxCargo());
            Ship ship = new Ship(type);
            ship.setCargo(cargo);
            p.setShip(ship);
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
