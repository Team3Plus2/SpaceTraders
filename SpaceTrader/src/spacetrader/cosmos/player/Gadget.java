/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.cosmos.player;

/**
 * A gadget can be used to upgrade a ship.
 * 
 * @author Aaron McAnally
 */
public class Gadget {
    /**
     * GadgetTypes: CARGO, NAVIGATION, AUTOREPAIR, TARGETING, CLOAKING
     */
    public enum GadgetType {
        CARGO, NAVIGATION, AUTOREPAIR, TARGETING, CLOAKING;
    }
    
    private GadgetType gadgetType;
    
    /**
     * @param gadgetType an enum that determines the type of gadget
     */
    public Gadget(GadgetType gadgetType) {
        this.gadgetType = gadgetType;
    }
}
