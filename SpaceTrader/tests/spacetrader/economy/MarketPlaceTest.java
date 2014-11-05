/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.economy;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import spacetrader.cosmos.system.Resource;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.player.Player;

/**
 *
 * @author Kartikay Kini
 */
public class MarketPlaceTest {
    
    public MarketPlaceTest() {
    }

    /**
     * Test of buy method, of class MarketPlace.
     */
    @Test
    public void testBuyOne() {
        System.out.println("buy");
        Player p = new Player("test", 15, 0, 0, 0, 0);
        p.setMoney(20000);
        MarketPlace instance = new MarketPlace(TechLevel.random(), Resource.random());
        TradeGood tg = instance.getListOfGoods().get(0);
        boolean expResult = true;
        boolean result = instance.buy(p, tg, 1);
        assertEquals(expResult, result);
    }

    /**
     * Test of sell method, of class MarketPlace.
     */
    @Test
    public void testSell() {
        System.out.println("sell");
        Player p = null;
        TradeGood tg = null;
        int amount = 0;
        MarketPlace instance = null;
        boolean expResult = false;
        boolean result = instance.sell(p, tg, amount);
        assertEquals(expResult, false);
    }
    
}
