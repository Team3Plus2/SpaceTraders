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
import spacetrader.player.Player;

/**
 *
 * @author Kartikay Kini
 */
public class MarketPlaceTest {
    
    public MarketPlaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setupGoodsWithLocale method, of class MarketPlace.
     */
    @Test
    public void testSetupGoodsWithLocale() {
        System.out.println("setupGoodsWithLocale");
        TechLevel localTech = null;
        MarketPlace instance = null;
        instance.setupGoodsWithLocale(localTech);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buy method, of class MarketPlace.
     */
    @Test
    public void testBuy() {
        System.out.println("buy");
        Player p = null;
        TradeGood tg = null;
        int amount = 0;
        MarketPlace instance = null;
        boolean expResult = false;
        boolean result = instance.buy(p, tg, amount);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of amountOfGood method, of class MarketPlace.
     */
    @Test
    public void testAmountOfGood() {
        System.out.println("amountOfGood");
        TradeGood tg = null;
        MarketPlace instance = null;
        int expResult = 0;
        int result = instance.amountOfGood(tg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of priceOfGood method, of class MarketPlace.
     */
    @Test
    public void testPriceOfGood() {
        System.out.println("priceOfGood");
        TradeGood tg = null;
        MarketPlace instance = null;
        float expResult = 0.0F;
        float result = instance.priceOfGood(tg);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfGoods method, of class MarketPlace.
     */
    @Test
    public void testGetListOfGoods() {
        System.out.println("getListOfGoods");
        MarketPlace instance = null;
        ArrayList<TradeGood> expResult = null;
        ArrayList<TradeGood> result = instance.getListOfGoods();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleNextTurn method, of class MarketPlace.
     */
    @Test
    public void testHandleNextTurn() {
        System.out.println("handleNextTurn");
        Player player = null;
        MarketPlace instance = null;
        instance.handleNextTurn(player);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
