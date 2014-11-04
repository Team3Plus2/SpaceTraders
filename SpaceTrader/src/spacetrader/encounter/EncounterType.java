package spacetrader.encounter;

import spacetrader.economy.TradeGood;
import spacetrader.player.ShipType;
import spacetrader.xml.FromXML;
import spacetrader.xml.LoadedType;

import java.util.ArrayList;

/**
 * Types of encounter, loaded from xml
 * 
 * @author Alex
 */
public class EncounterType extends LoadedType {
    private static String EncounterFileLocation = "objects/encounters.xml";

    public static void Load() {
        EncounterType.load(EncounterType.class, EncounterFileLocation, null);
    }
    
    public static EncounterType Random() {
        return (EncounterType) EncounterType.get((int)(Math.random() * EncounterType.size(EncounterType.class)), EncounterType.class);
    }

    @FromXML
    private int minTrade, maxTrade;
    
    @FromXML
    private int minPilot, maxPilot;
    
    @FromXML
    private int minEngineer, maxEngineer;
    
    @FromXML
    private int minInvestor, maxInvestor;
    
    @FromXML
    private int minFighter, maxFighter;
    
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