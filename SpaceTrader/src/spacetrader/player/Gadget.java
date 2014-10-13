package spacetrader.player;

import java.util.ArrayList;
import java.util.Random;
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
    
    public static Gadget Random() {
        ArrayList<GadgetType> rList = (ArrayList<GadgetType>) GadgetType.getList(GadgetType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Gadget(rList.get(index));
    }
    
    /**
     * @param gadgetType an enum that determines the type of gadget
     */
    public Gadget(GadgetType gadgetType) {
        this.gadgetType = gadgetType;
    }
    
    public GadgetType getType() {
        return this.gadgetType;
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
