package spacetrader.global;

import org.junit.Test;
import static org.junit.Assert.*;
import spacetrader.economy.TradeGood;

/**
 * JUnit Tests for Utility.
 * 
 * @author Carey MacDonald
 */
public class UtilityTest {

    /**
     * Test of currencyFormat method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCFNull() {
        Object o = null;
        String result = Utility.currencyFormat(o);
    }
    
    /**
     * Test of currencyFormat method with double.
     */
    @Test
    public void testCFDouble() {
        double d = 55.55;
        String expResult = "$55.55";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double, but negative.
     */
    @Test
    public void testCFDoubleNegative() {
        double d = -55.55;
        String expResult = "-$55.55";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double needed to be rounded up.
     */
    @Test
    public void testCFDoubleRoundedUp() {
        double d = 55.5555;
        String expResult = "$55.56";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double, but negative needed to be rounded up.
     */
    @Test
    public void testCFDoubleRoundedUpNegative() {
        double d = -55.5555;
        String expResult = "-$55.56";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double needed to be rounded down.
     */
    @Test
    public void testCFDoubleRoundedDown() {
        double d = 55.5544;
        String expResult = "$55.55";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double, but negative needed to be rounded down.
     */
    @Test
    public void testCFDoubleRoundedDownNegative() {
        double d = -55.5544;
        String expResult = "-$55.55";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double with zeros needing to be added.
     */
    @Test
    public void testCFDoubleWithZeros() {
        double d = 55;
        String expResult = "$55.00";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with double, but negative with zeros needing to be added.
     */
    @Test
    public void testCFDoubleWithZerosNegative() {
        double d = -55;
        String expResult = "-$55.00";
        String result = Utility.currencyFormat(d);
        assertEquals(expResult, result);
    }
    
        /**
     * Test of currencyFormat method with float.
     */
    @Test
    public void testCFFloat() {
        float f = 55.55f;
        String expResult = "$55.55";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float, but negative.
     */
    @Test
    public void testCFFloatNegative() {
        float f = -55.55f;
        String expResult = "-$55.55";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float needed to be rounded up.
     */
    @Test
    public void testCFFloatRoundedUp() {
        float f = 55.5555f;
        String expResult = "$55.56";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float, but negative needed to be rounded up.
     */
    @Test
    public void testCFFloatRoundedUpNegative() {
        float f = -55.5555f;
        String expResult = "-$55.56";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float needed to be rounded down.
     */
    @Test
    public void testCFFloatRoundedDown() {
        float f = 55.5544f;
        String expResult = "$55.55";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float, but negative needed to be rounded down.
     */
    @Test
    public void testCFFloatRoundedDownNegative() {
        float f = -55.5544f;
        String expResult = "-$55.55";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float with zeros needing to be added.
     */
    @Test
    public void testCFFloatWithZeros() {
        float f = 55;
        String expResult = "$55.00";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with float, but negative with zeros needing to be added.
     */
    @Test
    public void testCFFloatWithZerosNegative() {
        float f = -55;
        String expResult = "-$55.00";
        String result = Utility.currencyFormat(f);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with int with zeros needing to be added.
     */
    @Test
    public void testCFIntWithZeros() {
        int i = 55;
        String expResult = "$55.00";
        String result = Utility.currencyFormat(i);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with int, but negative with zeros needing to be added.
     */
    @Test
    public void testCFIntWithZerosNegative() {
        int i = -55;
        String expResult = "-$55.00";
        String result = Utility.currencyFormat(i);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with 0.
     */
    @Test
    public void testCFZero() {
        int i = 0;
        String expResult = "$0.00";
        String result = Utility.currencyFormat(i);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with -0.
     */
    @Test
    public void testCFZeroNegative() {
        int i = -0;
        String expResult = "$0.00";
        String result = Utility.currencyFormat(i);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of currencyFormat method with non-number class input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCFBadInput() {
        Object o = new Object();
        String result = Utility.currencyFormat(o);
    }
    
}
