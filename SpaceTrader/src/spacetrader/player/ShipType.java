package spacetrader.player;

import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 * An enum for ship types with basic stats:
 *      maxCargo, maxFuel, maxWeapons, maxShields, maxGadgets, maxMercenaries
 */
public class ShipType extends LoadedType{

    private static String ShipTypeFileLocation = "objects/Ships.xml";
    
    public static void Load() {
        ShipType Gnat = new ShipType("GNAT", 15, 14, 1, 0, 1, 0);
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
   private int maxCargo, maxWeapons, maxShields, maxGadgets, maxMercenaries;
   @FromXML
   private float maxFuel;
   
   private ShipType(String name, int maxCargo, float maxFuel, int maxWeapons, int maxShields, int maxGadgets, int maxMercenaries) {
       super(name);
       this.maxCargo = maxCargo;
       this.maxFuel = maxFuel;
       this.maxWeapons = maxWeapons;
       this.maxShields = maxShields;
       this.maxGadgets = maxGadgets;
       this.maxMercenaries = maxMercenaries;
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
   
   
}