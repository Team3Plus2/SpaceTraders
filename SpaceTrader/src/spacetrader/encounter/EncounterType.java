package spacetrader.encounter;

import spacetrader.economy.TradeGood;
import spacetrader.player.ShipType;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

/**
 * Types of encounter, loaded from xml.
 * 
 * @author Alex
 */
public class EncounterType extends LoadedType {
    private static String encounterFileLocation = "objects/encounters.xml";

    public static void load() {
        EncounterType.Load(EncounterType.class, encounterFileLocation, null);
    }
    
    public static EncounterType random() {
        return (EncounterType) EncounterType.get((int) (Math.random() * EncounterType.size(EncounterType.class)), EncounterType.class);
    }

    @FromXML
    private int minTrade;
    @FromXML
    private int maxTrade;
    
    @FromXML
    private int minPilot;
    @FromXML
    private int maxPilot;
    
    @FromXML
    private int minEngineer;
    @FromXML
    private int maxEngineer;
    
    @FromXML
    private int minInvestor;
    @FromXML
    private int maxInvestor;
    
    @FromXML
    private int minFighter;
    @FromXML
    private int maxFighter;
    
    @FromXML
    private String greeting;
    
    @FromXML
    private float aggression;
    
    @FromXML
    private boolean willingToTrade;
    
    @FromXML
    private float tradeRequestChance;
    
    @FromXML
    private ShipType ship;
    
    @FromXML
    private int maxCargoUsed;
    
    @FromXML (required = false)
    private TradeGood[] lookingFor;
    
    @FromXML
    private boolean associatedWithSystem;
    
    public String getGreeting() {
        return greeting;
    }

    public float getAggression() {
        return aggression;
    }

    public ShipType getShip() {
        return ship;
    }

    public TradeGood[] getLookingFor() {
        return lookingFor;
    }

    public boolean isAssociatedWithSystem() {
        return associatedWithSystem;
    }

    public int getMinTrade() {
        return minTrade;
    }

    public int getMaxTrade() {
        return maxTrade;
    }

    public int getMinPilot() {
        return minPilot;
    }

    public int getMaxPilot() {
        return maxPilot;
    }

    public int getMinEngineer() {
        return minEngineer;
    }

    public int getMaxEngineer() {
        return maxEngineer;
    }

    public int getMinInvestor() {
        return minInvestor;
    }

    public int getMaxInvestor() {
        return maxInvestor;
    }

    public int getMinFighter() {
        return minFighter;
    }

    public int getMaxFighter() {
        return maxFighter;
    }
    
    public int getMaxCargoUsed() {
        return maxCargoUsed;
    }
    
    public boolean getWillingToTrade() {
        return willingToTrade;
    }
    
    public float getTrade() {
        return tradeRequestChance;
    }
    
}