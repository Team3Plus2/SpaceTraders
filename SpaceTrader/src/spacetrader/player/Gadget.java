package spacetrader.player;

import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 * A gadget can be used to upgrade a ship.
 * 
 * @author Aaron McAnally
 */
public class Gadget extends Upgrade implements Serializable {

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
    
    /**
     * @return the list of all gadget types
     */
    public static ArrayList<Gadget> getGadgetTypes() {
        ArrayList<Gadget> gadgets = new ArrayList<Gadget>();
        ArrayList<GadgetType> types = GadgetType.getGadgetTypes();
        for (GadgetType type: types) {
            gadgets.add(new Gadget(type));
        }
        return gadgets;
    }
    
    @Override
    public String getClassName() {
        return "Gadget";
    }
    
    @Override
    public int getPrice() {
        return gadgetType.getPrice();
    }
    
    @Override
    public int getTechLevel() {
        return gadgetType.getTechLevel();
    }
    
    @Override
    public String toString() {
        return "Gadget - Type: " + this.gadgetType.getName();
    }
    
}

class GadgetType extends LoadedType implements Serializable {

    private static final String GadgetFileLocation = "objects/Gadgets.xml";

    public static void Load() {
        GadgetType.Load(GadgetType.class, GadgetFileLocation, null);
    }
    
    @FromXML
    private int price, techLevel;
    
    public GadgetType() {
        
    }
    
    public GadgetType(int price, int techLevel) {
        this.price = price;
        this.techLevel = techLevel;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getTechLevel() {
        return techLevel;
    }
    
    /**
     * @return the list of all gadget types
     */
    public static ArrayList<GadgetType> getGadgetTypes() {
        return GadgetType.getList(GadgetType.class);
    }
}
