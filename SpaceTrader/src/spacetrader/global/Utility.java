/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.global;

import java.text.NumberFormat;

/**
 * A class for any utility/function we may need.
 * 
 * @author Aaron McAnally
 */
public class Utility {
    
    /**
     * formats a number by giving it the following form:
     *      $#.## if positive
     *      -$#.## if negative.
     * 
     * @param o the input that must be formatted;
     *          can be any number primitive type or
     *          any object where toString() returns numbers and may have a decimal
     * @return the currency formatted string
     */
    public static String currencyFormat(Object o) {
        Float num = Float.parseFloat(o.toString());
        boolean negative = false;
        if (num < 0) {
            negative = true;
            num = Math.abs(num);
        }
        String s = NumberFormat.getCurrencyInstance().format(num);
        if (negative) {
            s = "-" + s;
        }
        return s;
    }
}
