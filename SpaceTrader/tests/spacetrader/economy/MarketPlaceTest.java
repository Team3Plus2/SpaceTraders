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
        float tempPrice = tg.getCurrentPriceEach();
        boolean result = instance.buy(p, tg, 1);
        assertEquals(expResult, result);
        assertEquals(p.getMoney(), 20000 - tempPrice, 0);
        
    }

    /**
     * Test of sell method, of class MarketPlace.
     */
    @Test
    public void testSell() {
        testBuyOne();
        TradeGood tg = p.getCargoList().get(0);
        int i = 0;
        while (tg.getAmount() == 0) {
            i++;
            tg = p.getCargoList().get(i);
        }
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
        while (tg.getAmount() == 0) {
            i++;
            tg = instance.getListOfGoods().get(i);
        }
        boolean expResult = true;
        int tempamount = tg.getAmount();
        float tempPrice = tg.getCurrentPriceEach();
        boolean result = instance.buy(p, tg, tg.getAmount());
         if(p.getCargoList().get(i).getAmount() > 0) {
            assertEquals(expResult, result);
            assertEquals(p.getMoney(), 20000 - tempPrice*tempamount, 0);
        } else {
            assertEquals(!expResult, result);
            assertEquals(p.getMoney(), 20000, 0);
        }
    }

    /**
     * Test of sell method, of class MarketPlace.
     */
    @Test
    public void testSellMultiple() {
        boolean success = false;
        while(!success) {
            testBuyMultiple();
            TradeGood tg = p.getCargoList().get(0);
            if(tg != null && tg.getAmount() > 0) {
                boolean expResult = true;
                boolean result = instance.sell(p, tg, tg.getAmount());
                assertEquals(expResult, result);
                assertEquals(p.getMoney(), 20000, 0);
                success = true;
            }
        }
    }
    
}
