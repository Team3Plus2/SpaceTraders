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
    
    public static void load() {
        GadgetType.load();
    }
    
    public static Gadget random() {
        ArrayList<GadgetType> rList = (ArrayList<GadgetType>) GadgetType.getList(GadgetType.class);
        Random rand = new Random();
        int index = rand.nextInt(rList.size());
        return new Gadget(rList.get(index));
    }
    
    /**
     * @param gadgetType2 an enum that determines the type of gadget
     */
    public Gadget(GadgetType gadgetType2) {
        this.gadgetType = gadgetType2;
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
    
    /**
     * Used to differentiate between Upgrade types.
     * 
     * @return "Gadget"
     */
    @Override
    public String getClassName() {
        return "Gadget";
    }
    
    /**
     * Used to differentiate between gadget types.
     * 
     * @return the name of the gadget type
     */
    public String getGadgetName() {
        return gadgetType.getName();
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

    private static final String GADGET_FILE_LOCATION = "objects/Gadgets.xml";

    public static void load() {
        GadgetType.load(GadgetType.class, GADGET_FILE_LOCATION, null);
    }
    
    @FromXML
    private int price;
    @FromXML
    private int techLevel;
    
    public GadgetType() {
        
    }
    
    public GadgetType(int price2, int techLevel2) {
        this.price = price2;
        this.techLevel = techLevel2;
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
