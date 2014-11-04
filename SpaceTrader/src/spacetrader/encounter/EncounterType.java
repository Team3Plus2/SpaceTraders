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
    
    /**
     * An id for serialization.
     */
    static final long serialVersionUID = (long) 54L;
    
    /**
     * Location of the encounters xml file.
     */
    private static String encounterFileLocation = "objects/encounters.xml";

    /**
     * Default method to load encounters.
     */
    public static void load() {
        EncounterType.load(EncounterType.class, encounterFileLocation, null);
    }
    
    /**
     * returns random encounter type.
     * @return the random encounter type.
     */
    public static EncounterType random() {
        return (EncounterType) EncounterType.get((int) (Math.random() * EncounterType.size(EncounterType.class)), EncounterType.class);
    }
    
    /**
     * The minimum trade skill.
     */
    @FromXML
    private int minTrade;
    
    /**
     * The max trade skill.
     */
    @FromXML
    private int maxTrade;
    
    /**
     * The min pilot skill.
     */
    @FromXML
    private int minPilot;
    
    /**
     * The max pilot skill.
     */
    @FromXML
    private int maxPilot;
    
    /**
     * The min engineer skill.
     */
    @FromXML
    private int minEngineer;
    
    /**
     * The max engineer skill.
     */
    @FromXML
    private int maxEngineer;
    
    /**
     * The min investor skill.
     */
    @FromXML
    private int minInvestor;
    
    /**
     * The max investor skill.
     */
    @FromXML
    private int maxInvestor;
    
    /**
     * The min fighter skill.
     */
    @FromXML
    private int minFighter;
    
    /**
     * The max fighter skill.
     */
    @FromXML
    private int maxFighter;
    
    /**
     * The greeting string.
     */
    @FromXML
    private String greeting;
    
    /**
     * The aggressive float.
     */
    @FromXML
    private float aggression;
    
    /**
     * Whether the person is willing to trade.
     */
    @FromXML
    private boolean willingToTrade;
    
    /**
     * The chance the person will request a trade.
     */
    @FromXML
    private float tradeRequestChance;
    
    /**
     * The other ship.
     */
    @FromXML
    private ShipType ship;
    
    /**
     * The max cargo used int.
     */
    @FromXML
    private int maxCargoUsed;
    
    /**
     * What the other ship is looking for.
     */
    @FromXML (required = false)
    private TradeGood[] lookingFor;
    
    /**
     * If this ship is associated with a system.
     */
    @FromXML
    private boolean associatedWithSystem;
    
    /**
     * Getter for the greeting.
     * @return String greeting
     */
    public String getGreeting() {
        return greeting;
    }

    /**
     * Getter for the aggression float.
     * @return float aggression
     */
    public float getAggression() {
        return aggression;
    }

    /**
     * Getter for the shiptype of this ship.
     * @return ShipType of ship
     */
    public ShipType getShip() {
        return ship;
    }

    /**
     * Getter for the list of tradegoods this person is looking for.
     * @return TradeGood[] looking for
     */
    public TradeGood[] getLookingFor() {
        if (lookingFor == null) {
            return new TradeGood[0];
        }
        return lookingFor.clone();
    }

    /**
     * Getter for whether this ship is associated with system.
     * @return boolean associated with system
     */
    public boolean isAssociatedWithSystem() {
        return associatedWithSystem;
    }

    /**
     * Getter for the min trader level.
     * @return int the min trade level
     */
    public int getMinTrade() {
        return minTrade;
    }

    /**
     * Getter for the max trader level.
     * @return int the max trade level
     */
    public int getMaxTrade() {
        return maxTrade;
    }

    /**
     * Getter for the min pilot level.
     * @return int the min pilot level
     */
    public int getMinPilot() {
        return minPilot;
    }

    /**
     * Getter for the max pilot level.
     * @return int the max pilot level
     */
    public int getMaxPilot() {
        return maxPilot;
    }

    /**
     * Getter for the min engineer level.
     * @return int the min engineer level
     */
    public int getMinEngineer() {
        return minEngineer;
    }

    /**
     * Getter for the max engineer level.
     * @return int the max engineer level
     */
    public int getMaxEngineer() {
        return maxEngineer;
    }

    /**
     * Getter for the min investor level.
     * @return int the min investor level
     */
    public int getMinInvestor() {
        return minInvestor;
    }

    /**
     * Getter for the max investor level.
     * @return the max investor level
     */
    public int getMaxInvestor() {
        return maxInvestor;
    }

    /**
     * Getter for the min fighter level.
     * @return int the min fighter level
     */
    public int getMinFighter() {
        return minFighter;
    }

    /**
     * Getter for the max fighter level.
     * @return int the max fighter level
     */
    public int getMaxFighter() {
        return maxFighter;
    }
    
    /**
     * Getter for the max cargo used.
     * @return int the max cargo used
     */
    public int getMaxCargoUsed() {
        return maxCargoUsed;
    }
    
    /**
     * Getter for the willing to trade boolean.
     * @return boolean whether the ship is willing to trade
     */
    public boolean getWillingToTrade() {
        return willingToTrade;
    }
    
    /**
     * Getter for the trade request chance.
     * @return float the trade request chance
     */
    public float getTrade() {
        return tradeRequestChance;
    }
    
}
