/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.utility;

import java.text.NumberFormat;

/**
 * A class for any utility objects we may need.
 * 
 * @author Aaron McAnally
 */
public class Utility {
    
    /**
     * @return a NumberFormat that formats a number to dollar (2 decimal) format
     */
    public static NumberFormat currencyFormat() {
        return NumberFormat.getCurrencyInstance();
    }
}
