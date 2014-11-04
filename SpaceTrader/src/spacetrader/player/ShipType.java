package spacetrader.player;

import java.util.ArrayList;
import spacetrader.main.SpaceTrader;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 * An enum for ship types with basic stats:
 *      maxCargo, maxFuel, maxWeapons, maxShields, maxGadgets, maxMercenaries.
 */
public class ShipType extends LoadedType {

    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 97L;
    
    /**
     * String name of the file containing the list of ship types.
     */
    private static String shipTypeFileLocation = "objects/Ships.xml";
    
    /**
     * Loads the list of ship types.
     */
    public static void load() {
        ShipType gnat = new ShipType("GNAT", 15, 14, 1, 0, 1, 0, 5, 10000, 2);
        ShipType.load(ShipType.class, shipTypeFileLocation, gnat);
    }
    
    /**
     * Loads the default ship type.
     * 
     * @return the default ShipType
     */
    public static ShipType defaultValue() {
        return (ShipType) ShipType.defaultValue(ShipType.class);
    }
    
    /**
     * Needed by XMLReader.
     */
    public ShipType() {
        
    }
   
    /**
     * The maximum cargo for this ShipType.
     */
    @FromXML
    private int maxCargo;
    /**
     * The maximum weapons for this ShipType.
     */
    @FromXML
    private int maxWeapons;
    /**
     * The maximum shields for this ShipType.
     */
    @FromXML
    private int maxShields;
    /**
     * The maximum gadgets for this ShipType.
     */
    @FromXML
    private int maxGadgets;
    /**
     * The maximum mercenaries for this ShipType.
     */
    @FromXML
    private int maxMercenaries;
    /**
     * The minimum TechLevel for this ShipType to be sold.
     */
    @FromXML
    private int minTechLevel;
    /**
     * The maximum fuel for this ShipType.
     */
    @FromXML
    private float maxFuel;
    /**
     * The price of this ShipType.
     */
    @FromXML
    private float price;
    /**
     * The fuel cost of this ShipType.
     */
    @FromXML
    private float fuelCost;
   
    /**
     * Constructor for ShipType.
     * 
     * @param name the name of this ShipType.
     * @param maxCargo2 the max cargo of this ShipType
     * @param maxFuel2 the max fuel of this ShipType
     * @param maxWeapons2 the max weapons of this ShipType
     * @param maxShields2 the max shields of this ShipType
     * @param maxGadgets2 the max gadgets of this ShipType
     * @param maxMercenaries2 the max mercenaries of this ShipType
     * @param techLevel the minimum TechLevel it takes to sell this ShipType
     * @param price2 the price of this ShipType
     * @param fuelCost2 the cost of fuel for this ShipType
     */
    private ShipType(String name, int maxCargo2, float maxFuel2, int maxWeapons2, int maxShields2, int maxGadgets2, int maxMercenaries2, int techLevel, float price2, float fuelCost2) {
        super(name);
        this.maxCargo = maxCargo2;
        this.maxFuel = maxFuel2;
        this.maxWeapons = maxWeapons2;
        this.maxShields = maxShields2;
        this.maxGadgets = maxGadgets2;
        this.maxMercenaries = maxMercenaries2;
        this.minTechLevel = techLevel;
        this.price = price2;
        this.fuelCost = fuelCost2;
    }

    /**
     * Getter for maxCargo.
     * 
     * @return maxCargo
     */
    public int getMaxCargo() {
        return maxCargo;
    }

    /**
     * Getter for maxWeapons.
     * 
     * @return maxWeapons
     */
    public int getMaxWeapons() {
        return maxWeapons;
    }

    /**
     * Getter for maxShields.
     * 
     * @return maxShields
     */
    public int getMaxShields() {
        return maxShields;
    }

    /**
     * Getter for maxGadgets.
     * 
     * @return maxGadgets
     */
    public int getMaxGadgets() {
        return maxGadgets;
    }

    /**
     * Getter for maxMercenaries.
     * 
     * @return maxMercenaries
     */
    public int getMaxMercenaries() {
        return maxMercenaries;
    }

    /**
     * Getter for maxFuel.
     * 
     * @return maxFuel
     */
    public float getMaxFuel() {
        return maxFuel;
    }
    
    /**
     * Getter for minTechLevel.
     * 
     * @return minTechLevel
     */
    public int getTechLevel() {
        return minTechLevel;
    }
    
    /**
     * Getter for price.
     * 
     * @return price
     */
    public float getPrice() {
        return price;
    }
    
    /**
     * Getter for fuelCost.
     * 
     * @return fuelCost
     */
    public float getFuelCost() {
        return fuelCost;
    }
   
    /**
     * Getter for the list of ShipTypes.
     * 
     * @return the list of ShipTypes
     */
    public static ArrayList<ShipType> getShipTypes() {
        return ShipType.getList(ShipType.class);
    }
    
    /**
     * Getter for information about this ShipType.
     * 
     * @return information about this ShipType
     */
    public String getInfo() {
        return getName()
                + "\nCargo: " + getMaxCargo()
                + "\nWeapon Slots: " + getMaxWeapons()
                + "\nGadget Slots: " + getMaxGadgets()
                + "\nMercenary Positions: " + getMaxGadgets()
                + "\nMax Fuel: " + getMaxFuel()
                + "\nFuel Cost: " + getFuelCost();
    }
    
    @Override
    public String toString() {
        SpaceTrader instance = SpaceTrader.getInstance();
        if (instance != null
                && instance.getPlayer().getShip().getName().equals(getName())) {
            return getName() + " (owned)";
        } else {
            return getName();
        }
    }
}
