package spacetrader.player;

import java.io.Serializable;
import java.util.ArrayList;
import spacetrader.main.SpaceTrader;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 * An enum for ship types with basic stats:
 *      maxCargo, maxFuel, maxWeapons, maxShields, maxGadgets, maxMercenaries
 */
public class ShipType extends LoadedType implements Serializable {

    private static String ShipTypeFileLocation = "objects/Ships.xml";
    
    public static void Load() {
        ShipType Gnat = new ShipType("GNAT", 15, 14, 1, 0, 1, 0, 5, 10000, 2);
        ShipType.Load(ShipType.class, ShipTypeFileLocation, Gnat);
    }
    
    public static ShipType Default() {
        return (ShipType)ShipType.Default(ShipType.class);
    }
    
   /*
    Needed by XMLReader
   */
   public ShipType() {
       
   }
   
   @FromXML
   private int maxCargo, maxWeapons, maxShields, maxGadgets, maxMercenaries, minTechLevel;
   @FromXML
   private float maxFuel, price, fuelCost;
   
   private ShipType(String name, int maxCargo, float maxFuel, int maxWeapons, int maxShields, int maxGadgets, int maxMercenaries, int techLevel, float price, float fuelCost) {
       super(name);
       this.maxCargo = maxCargo;
       this.maxFuel = maxFuel;
       this.maxWeapons = maxWeapons;
       this.maxShields = maxShields;
       this.maxGadgets = maxGadgets;
       this.maxMercenaries = maxMercenaries;
       this.minTechLevel = techLevel;
       this.price = price;
       this.fuelCost = fuelCost;
   }

    public int getMaxCargo() {
        return maxCargo;
    }

    public int getMaxWeapons() {
        return maxWeapons;
    }

    public int getMaxShields() {
        return maxShields;
    }

    public int getMaxGadgets() {
        return maxGadgets;
    }

    public int getMaxMercenaries() {
        return maxMercenaries;
    }

    public float getMaxFuel() {
        return maxFuel;
    }
    
    public int getTechLevel() {
        return minTechLevel;
    }
    
    public float getPrice() {
        return price;
    }
    
    public float getFuelCost() {
        return fuelCost;
    }
   
    public static ArrayList<ShipType> getShipTypes() {
        return ShipType.getList(ShipType.class);
    }
    
    public String getInfo() {
        return getName() +
                "\nCargo: " + getMaxCargo() +
                "\nWeapon Slots: " + getMaxWeapons() +
                "\nGadget Slots: " + getMaxGadgets() +
                "\nMercenary Positions: " + getMaxGadgets() +
                "\nMax Fuel: " + getMaxFuel() +
                "\nFuel Cost: " + getFuelCost();
    }
    
    @Override
    public String toString() {
        SpaceTrader instance = SpaceTrader.getInstance();
        if(instance != null &&
                instance.getPlayer().getShip().getName().equals(getName())) {
            return getName() + " (owned)";
        } else {
            return getName();
        }
    }
}