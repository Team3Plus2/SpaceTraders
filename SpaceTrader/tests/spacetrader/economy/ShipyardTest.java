/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.economy;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;
import spacetrader.player.Weapon;
import spacetrader.xml.LoadedType;

/**
 *
 * @author Admin
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
    public void buyShipMoneyTest() {
        Shipyard shipyard = shipyards[shipyards.length - 1];
        for (ShipType type: shipTypes) {
            assertFalse(shipyard.buyShip(brokePlayer, type));
            float before = richPlayer.getMoney();
            if (shipyard.buyShip(richPlayer, type)) {
                assertEquals(richPlayer.getMoney(), before - type.getPrice(), 1.0);
            }
        }
    }
    
    @Test
    public void buyShipTechLevelTest() {
        for (Shipyard shipyard : shipyards) {
            boolean[] canBuy = buyShips(richPlayer, shipyard);
            for (int j = 0; j < canBuy.length; j++) {
                boolean expected = shipTypes.get(j).getTechLevel()
                        <= TechLevel.getIndex(shipyard.getTechLevel());
                assertTrue("Shipyard tech level is " + TechLevel.getIndex(shipyard.getTechLevel()) + "; Ship Type tech level is " + shipTypes.get(j).getTechLevel()
                            + "; attempted purchace returned " + canBuy[j] + "; expected: " + expected,
                        canBuy[j] == expected);
            }
        }
    }
    
    /**
     * @param shipyard the shipyard being tested
     * @return an array that contains what is returned when the player tries to purchase a ship
     */
    private boolean[] buyShips(Player p, Shipyard shipyard) {
        boolean[] canBuy = new boolean[shipTypes.size()];
        for (int i = 0; i < shipTypes.size(); i++) {
            canBuy[i] = shipyard.buyShip(p, shipTypes.get(i));
            if (canBuy[i] && i > 0) {
                canBuy[i-1] = true;
            }
        }
        return canBuy;
    }
}
