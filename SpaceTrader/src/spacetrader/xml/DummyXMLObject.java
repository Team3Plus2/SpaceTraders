package spacetrader.xml;

import spacetrader.cosmos.system.*;

/**
 * Example XMLReader interfacing object
 * @author Alex
 */
public class DummyXMLObject {
    
    @FromXML //if this variable is not in the xml, an exception will be thrown
    private int f1;
    @FromXML(required = false) //if this variable is not in the xml, the value of the variable will be left untouched
    private String f2;
    @FromXML
    private double f3;
    @FromXML(required = false)
    private float f4 = 0.0f;
    @FromXML(required = false)
    protected TechLevel tech;
    
    public void print() {
        System.out.println("Begin object:");
        System.out.println("f1: " + f1);
        if(f2 != null)
            System.out.println("f2: " + f2);
        System.out.println("f3: " + f3);
        if(f4 != 0.0f)
            System.out.println("f4: " + f4);
        if(tech != null)
            System.out.println("Tech: " + tech);
    }
}
