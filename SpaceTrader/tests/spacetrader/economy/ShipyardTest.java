/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.economy;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.player.Cargo;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;
import spacetrader.player.Weapon;

/**
 * Tests the buyShip method of shipyard for the following:
 * - instantiation of shipyards of different tech levels was successful
 * - cargo is preserved after purchase
 * - the new ship is owned after purchase
 * - purchasing a ship that is already owned does not buy it
 * - a ship can only be purchased if the player owns enough money for it
 * - a ship can only be purchased if the tech level of the shipyard is high enough
 * 
 * @author Aaron McAnally
 */
public class ShipyardTest {
    
    private ArrayList<TechLevel> techLevels;
    private Shipyard[] shipyards;
    private Player brokePlayer;
    private Player richPlayer;
    private ArrayList<ShipType> shipTypes;
    
    public ShipyardTest() {
    }
    
    @Before
    public void setUp() {
        TechLevel.load();
        ShipType.load();
        Weapon.load();
        Shield.load();
        Gadget.load();
        TradeGood.load();
        techLevels = TechLevel.getList(TechLevel.class);
        shipyards = new Shipyard[techLevels.size()];
        for (int i = 0; i < shipyards.length; i++) {
            shipyards[i] = new Shipyard(techLevels.get(i));
        }
        brokePlayer = new Player("Player", 0, 0, 0, 0, 0);
        richPlayer = new Player("RichPlayer", 0, 0, 0, 0, 0);
        brokePlayer.setMoney(0);
        richPlayer.setMoney(100000000);
        shipTypes = ShipType.getShipTypes();
    }
    
    @Test
    public void shipyardsExist() {
        for (Shipyard shipyard : shipyards) {
            assertNotNull(shipyard);
        }
    }
    
    @Test
    public void cargoSavedAfterPurchaseTest() {
        ShipType grasshopper = null;
        for (ShipType type : shipTypes) {
            if (type.getName().equals("GRASSHOPPER")) {
                richPlayer.setShip(new Ship(type));
                grasshopper = type;
            }
        }
        for (int i = 0; i < 30; i++) {
            richPlayer.addTradeGood(TradeGood.randomSingleInstance());
        }
        for (Shipyard shipyard : shipyards) {
            for (ShipType type : shipTypes) {
                if (shipyard.buyShip(richPlayer, type)) {
                    assertTrue("Purchase of ship " + type.getName() + " was successful despite it having less cargo space ("
                                + type.getMaxCargo() + ") than player's ship " + richPlayer.getShip().getName() +
                                " cargo filled (" + richPlayer.getShip().getCargo().getNumFilled() + ")",
                                type.getMaxCargo() >= richPlayer.getShip().getCargo().getNumFilled());
                    assertTrue("Cargo was not preserved. Found: " + richPlayer.getShip().getCargo().getNumFilled()
                                + " Expected: 30",
                                richPlayer.getShip().getCargo().getNumFilled() == 30);
                } else if (type.getTechLevel() <= TechLevel.getIndex(shipyard.getTechLevel())
                            && !richPlayer.getShip().equals(new Ship(type))) {
                    assertFalse("Purchase of ship " + type.getName() + " was unsuccessful despite it having more cargo space ("
                                + type.getMaxCargo() + ") than player's ship " + richPlayer.getShip().getName() +
                                " cargo filled (" + richPlayer.getShip().getCargo().getNumFilled() + ")",
                                type.getMaxCargo() >= richPlayer.getShip().getCargo().getNumFilled());
                }
                Cargo cargo = richPlayer.getShip().getCargo();
                if (grasshopper != null) {
                    richPlayer.setShip(new Ship(grasshopper));
                    richPlayer.getShip().setCargo(cargo);
                }
            }
        }
        
    }
    
    @Test
    public void shipOwnedAfterPurchaseTest() {
        for (Shipyard shipyard : shipyards) {
            for (ShipType type : shipTypes) {
                if (shipyard.buyShip(richPlayer, type)) {
                    assertTrue("After successful purchase, player owns a ship of type " + richPlayer.getShip().getName()
                                    + " Expected: " + type.getName(),
                                    richPlayer.getShip().equals(new Ship(type)));
                }
            }
        }
    }
    
    @Test
    public void buyShipAlreadyOwnedTest() {
        for (ShipType owned : shipTypes) {
            richPlayer.setShip(new Ship(owned));
            for (Shipyard shipyard : shipyards) {
                for (ShipType type : shipTypes) {
                    boolean success = shipyard.buyShip(richPlayer, type);
                    if (owned.equals(type)) {
                        assertFalse("Player was able to buy ship " + type.getName()
                                        + " and already owns ship " + richPlayer.getShip().getName()
                                        + " in shipyard of tech level " + shipyard.getTechLevel(),
                                        success);
                    }
                    richPlayer.setShip(new Ship(owned));
                }
            }
        }
    }
    
    @Test
    public void buyShipMoneyTest() {
        for (Shipyard shipyard : shipyards) {
            for (ShipType type : shipTypes) {
                assertFalse("Broke player was able to purchase a ship of type " + type
                                + " from Shipyard of tech level " + shipyard.getTechLevel(),
                                shipyard.buyShip(brokePlayer, type));
                float before = richPlayer.getMoney();
                boolean success = shipyard.buyShip(richPlayer, type);
                float after = richPlayer.getMoney();
                if (success) {
                    assertEquals("Money deducted from player was improper amount. Expected: " + type.getPrice() + " Actual: " + (before - after),
                                    after, before - type.getPrice(), 1.0);
                } else if (type.getTechLevel() <= TechLevel.getIndex(shipyard.getTechLevel())
                            && !richPlayer.getShip().equals(new Ship(type))) {
                    fail("Rich Player was unable to purchase a ship of type " + type
                            + " from Shipyard of tech level " + shipyard.getTechLevel());
                } else {
                    assertEquals("Money was deducted even though the purchase of ship of type "
                                        + type.getName() + " was unsuccessful",
                                        before, after, 1.0);
                }
            }
        }
    }
    
    @Test
    public void buyShipTechLevelTest() {
        for (Shipyard shipyard : shipyards) {
            boolean[] canBuy = buyShipsIgnoreAlreadyOwned(richPlayer, shipyard);
            for (int j = 0; j < canBuy.length; j++) {
                boolean expected = shipTypes.get(j).getTechLevel()
                        <= TechLevel.getIndex(shipyard.getTechLevel());
                assertTrue("Shipyard tech level is " + TechLevel.getIndex(shipyard.getTechLevel())
                                + "; Ship Type tech level is " + shipTypes.get(j).getTechLevel()
                                + "; attempted purchace returned " + canBuy[j] + "; expected: " + expected,
                                canBuy[j] == expected);
            }
        }
    }
    
    /**
     * Returns boolean array telling which ships can be purchased successfully.
     * Ignores the fact that the new ship must be different from the one already owned by the player
     * 
     * @param shipyard the shipyard being tested
     * @return an array that contains what is returned when the player tries to purchase a ship
     */
    private boolean[] buyShipsIgnoreAlreadyOwned(Player p, Shipyard shipyard) {
        boolean[] canBuy = new boolean[shipTypes.size()];
        for (int i = 0; i < shipTypes.size(); i++) {
            if (p.getShip().getName().equals(shipTypes.get(i).getName()) && i != 0) {
                p.setShip(new Ship(shipTypes.get(0)));
            } else {
                p.setShip(new Ship(shipTypes.get(shipTypes.size() - 1)));
            }
            canBuy[i] = shipyard.buyShip(p, shipTypes.get(i));
        }
        return canBuy;
    }

}
