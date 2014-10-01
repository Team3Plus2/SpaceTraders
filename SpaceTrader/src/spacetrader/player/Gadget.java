package spacetrader.player;

import spacetrader.xml.LoadedType;

/**
 * A gadget can be used to upgrade a ship.
 * 
 * @author Aaron McAnally
 */
public class Gadget {

    private GadgetType gadgetType;
    
    public static void Load() {
        GadgetType.Load();
    }
    
    /**
     * @param gadgetType an enum that determines the type of gadget
     */
    public Gadget(GadgetType gadgetType) {
        this.gadgetType = gadgetType;
    }
}

class GadgetType extends LoadedType {

    private static final String GadgetFileLocation = "objects/Gadgets.xml";

    public static void Load() {
        GadgetType.Load(GadgetType.class, GadgetFileLocation, null);
    }

    public GadgetType() {
        
    }
}
