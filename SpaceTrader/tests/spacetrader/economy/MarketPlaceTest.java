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
import spacetrader.xml.ObjectLoader;

/**
 *
 * @author Kartikay Kini
 */
public class MarketPlaceTest {
    
    MarketPlace instance;
    Player p;
    
    
    public MarketPlaceTest() {
        ObjectLoader.loadAllObjects();
    }

    /**
     * Test of buy method, of class MarketPlace.
     */
    @Test
    public void testBuyOne() {
        p = new Player("test", 15, 0, 0, 0, 0);
        p.setMoney(20000);
        instance = new MarketPlace(TechLevel.random(), Resource.random());
        TradeGood tg = instance.getListOfGoods().get(0);
        int i = 0;
        while (tg.getAmount() == 0) {
            i++;
            tg = instance.getListOfGoods().get(i);
        }
        boolean expResult = true;
        boolean result = instance.buy(p, tg, 1);
        assertEquals(expResult, result);
        assertEquals(p.getMoney(), 20000 - tg.getCurrentPriceEach(), 0);
    }

    /**
     * Test of sell method, of class MarketPlace.
     */
    @Test
    public void testSell() {
        testBuyOne();
        TradeGood tg = p.getCargoList().get(0);
        boolean expResult = true;
        boolean result = instance.sell(p, tg, 1);
        assertEquals(expResult, result);
        assertEquals(p.getMoney(), 20000, 0);
    }
    
     /**
     * Test of buy method, of class MarketPlace.
     */
    @Test
    public void testBuyMultiple() {
        p = new Player("test", 15, 0, 0, 0, 0);
        p.setMoney(20000);
        instance = new MarketPlace(TechLevel.random(), Resource.random());
        
        TradeGood tg = instance.getListOfGoods().get(0);
        int i = 0;
        System.out.println(tg.getAmount());
        while (tg.getAmount() == 0) {
            i++;
            tg = instance.getListOfGoods().get(i);
        }
        boolean expResult = true;
        System.out.println("Buying " + tg.getAmount() + " " + tg.getName() + "s.");
        int tempamount = tg.getAmount();
        float tempPrice = tg.getCurrentPriceEach();
        boolean result = instance.buy(p, tg, tg.getAmount());
        if(p.getShip().getCargo().getNumEmpty() >= tempamount) {
            assertEquals(expResult, result);
        } else {
            assertEquals(expResult, !result);
        }
        System.out.println("Buying " + tempamount + " " + tg.getName() + "s for " + tempPrice);
        assertEquals(p.getMoney(), 20000 - tempPrice*tempamount, 0);
    }

    /**
     * Test of sell method, of class MarketPlace.
     */
    @Test
    public void testSellMultiple() {
        testBuyMultiple();
        TradeGood tg = p.getCargoList().get(0);
        System.out.println("Buying " + tg.getAmount() + " " + tg.getName() + "s.");
        boolean expResult = true;
        boolean result = instance.sell(p, tg, tg.getAmount());
        assertEquals(expResult, result);
        assertEquals(p.getMoney(), 20000, 0);
    }
    
}
