/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.xml;

/**
 *
 * @author Alex
 */
public class DummyXMLObject {
    
    @FromXML
    private int f1;
    @FromXML
    private String f2;
    @FromXML
    private double f3;
    @FromXML(required = false)
    private float f4;

    public void print() {
        System.out.println("Begin object:");
        System.out.println("f1: " + f1);
        System.out.println("f2: " + f2);
        System.out.println("f3: " + f3);
    }
}
