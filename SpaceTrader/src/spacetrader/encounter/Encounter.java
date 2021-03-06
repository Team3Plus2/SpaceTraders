package spacetrader.encounter;

import java.util.ArrayList;
import java.util.Random;

import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.Weapon;

/**
 * An interface for the trader, pirate, and police encounters.
 * 
 * @author Aaron McAnally
 */
public class Encounter {
    
    /**
     * Loads the list of EncounterTypes.
     */
    public static void load() {
        EncounterType.load();
    }
    
    /**
     * The captain of this Encounter.
     */
    private Player captain;
    /**
     * The SolarSystem this Encounter is associated with.
     */
    private SolarSystem systemAssociation;
    /**
     * Type of this Encounter.
     */
    private EncounterType type;
    /**
     * Whether or not this Encounter will attack.
     */
    private boolean willAttack;
    /**
     * Whether of not this Encounter will trade.
     */
    private boolean willTrade;
    /**
     * Array of TradeGoods that this Encounter will search for.
     */
    private TradeGood[] searchFor;

    /**
     * Constructor for Encounter.
     * 
     * @param system the SolarSystem this Encounter will be affiliated with.
     */
    public Encounter(SolarSystem system) {
        type = EncounterType.random();
        int traderSkill = (int) (Math.random() * (type.getMaxTrade() - type.getMinTrade())) + type.getMinTrade();
        int pilotSkill = (int) (Math.random() * (type.getMaxPilot() - type.getMinPilot())) + type.getMinPilot();
        int engineerSkill = (int) (Math.random() * (type.getMaxEngineer() - type.getMinEngineer())) + type.getMinEngineer();
        int investorSkill = (int) (Math.random() * (type.getMaxInvestor() - type.getMinInvestor())) + type.getMinInvestor();
        int fighterSkill = (int) (Math.random() * (type.getMaxFighter() - type.getMinFighter())) + type.getMinFighter();
        
        captain = new Player(type.getName(), pilotSkill, fighterSkill, traderSkill, engineerSkill, investorSkill);
        
        captain.setShip(new Ship(type.getShip()));
        
        /*
            Breaking the law of not calling stuff via forced delegation because player doesn't need to know how to talk about max slots
        */
        
        for (int i = 0; i < captain.getShip().getMaxWeapons(); i++) {
            captain.getShip().addWeapon(Weapon.random());
        }
        
        for (int i = 0; i < captain.getShip().getMaxShields(); i++) {
            captain.getShip().addShield(Shield.random());
        }
        
        for (int i = 0; i < captain.getShip().getMaxGadgets(); i++) {
            captain.getShip().addGadget(Gadget.random());
        }
        
        
        
        int cargoFilled = (int) (Math.random() * type.getMaxCargoUsed() + 1);
        for (int i = 0; i < cargoFilled; i++) {
            captain.getShip().getCargo().addTradeGood(TradeGood.randomSingleInstance());
        }
        
        if (type.isAssociatedWithSystem()) {
            systemAssociation = system;
            if (system.shouldAttack()) {
                willAttack = true;
            } else if (system.shouldSearch()) {
                searchFor = type.getLookingFor();
            }
        } else {
            if (type.getLookingFor().length != 0) {
                searchFor = type.getLookingFor();
            } else {
                searchFor = null;
            }
            willAttack = type.getAggression() > Math.random();
            willTrade = type.getTrade() > Math.random();
        }
    }
    
    /**
     * Getter for this Encounter's greeting.
     * 
     * @return this Encounter's greeting
     */
    public String getGreeting() {
        return type.getGreeting();
        //"Hey, gimme all ur stufs, im pore and nede mor mony >:0";
    }
    
    /**
     * Returns true if the encounter will try to attack the player.
     * @return true if the encounter will try to attack the player
     */
    public boolean willAttack() {
        return willAttack;
    }
    
    /**
     * returns true if the encounter will accept the player's request to trade.
     * @return true if the encounter will accept the player's request to trade
     */
    public boolean willingToTrade() {
        return type.getWillingToTrade();
    }
    
    /**
     * returns true if the encounter will request the player to trade.
     * @return true if the encounter will request the player to trade
     */
    public boolean willRequestTrade() {
        return willTrade;
    } 
    
    /**
     * returns true if the encounter will request to search the player.
     * @return true if the encounter will request to search the player
     */
    public boolean willRequestSearch() {
        return searchFor != null;
    }
    
    /**
     * returns true if the encounter will surrender to the player,
     * depends on encounter's current health and player's power.
     * 
     * @return true if the encounter will surrender to the player
     */
    public boolean willSurrender() {
        return captain.getShip().getPower() == 0;
    }
    
    /**
     * Returns an arraylist of the encounter's goods.
     * @return an arraylist of the encounter's goods
     */
    public ArrayList<TradeGood> getGoods() {
        return captain.getCargoList();
    }
    
    /**
     * Simulates one round of combat.
     * 
     * @param player the player involved in the combat
     * @param targets the parts of the ship the player wishes to target
     * @return 0: both still alive, -1: enemy dead, -2: enemy surrenders, 1: player dead
     */
    public int roundOfCombat(Player player, ArrayList targets) {
        if (captain.attack(player, null)) {
            return 1;
        }
        
        if (player.attack(captain, targets)) {
            return -1;
        }
        
        if (willSurrender()) {
            return -2;
        }
        
        return 0;
    }
    
    /**
     * @return the damage to the ship's shields
     */
    public int getDamageToShields() {
        return captain.getDamageToShields();
    }
    
    /**
     * @return the most recently destroyed objects from the ship
     */
    public ArrayList getDestroyed() {
        return captain.getDestroyed();
    }
    
    /**
     * @return the damage this ship dealt in the last attack
     */
    public int getDamageDealt() {
        return captain.getDamageDealt();
    }
    
    /**
     * @return the ship for this encounter
     */
    public Ship getShip() {
        return captain.getShip();
    }
    
    /**
     * 
     * @param other 
     * @return true if something is found
     */
    public boolean search(Player other) {
        return other.removeTradeGoodsByType(searchFor);
    }
    
    /**
     * Loot all the tradegoods of the given player.
     * (Goods unable to be held will be jetissoned, so all goods are removed)
     * 
     * @param other player to loot
     */
    public void loot(Player other) {
        ArrayList<TradeGood> goodTypes = TradeGood.getTradeGoodTypes();
        Object[] goodObjs = goodTypes.toArray();
        TradeGood[] goods = new TradeGood[goodObjs.length];
        for (int i = 0; i < goods.length; i++) {
            goods[i] = TradeGood.class.cast(goodObjs[i]);
        }
        other.removeTradeGoodsByType(goods);
    }
    
    /**
     * Gets a marketplace from this encounter's tradegoods.
     * @param system system context of the market
     * @return a marketplace
     */
    public MarketPlace getMarketPlace(SolarSystem system) {
        MarketPlace market = new MarketPlace(captain.getCargoList());
        market.setupGoodsWithLocale(system.techLevel());
        return market;
    }
    
    /**
     * Gets a looting echange in which all goods in the encounter's ship are
     * priced at zero.
     * @return a looting exchange
     */
    public MarketPlace getLootingExchange() {
        return new MarketPlace(captain.getCargoList(), true);
    }
    
    /**
     * Gets a looting exchange of a random size as the player tries to salvage
     * what remains from the destroyed ship (may still be all goods).
     * @return a salvage exchange (as defined above)
     */
    public MarketPlace getSalvageExchange() {
        Random rand = new Random();
        int size = rand.nextInt(captain.getCargoList().size());
        ArrayList<TradeGood> subList = new ArrayList<TradeGood>();
        for (int i = 0; i < size; i++) {
            subList.add(captain.getCargoList().get(i));
        }
        return new MarketPlace(subList, true);
    }

    /**
     * Getter for the SystemAssociation.
     * @return SolarSystem system association
     */
    public SolarSystem getSystemAssociation() {
        return systemAssociation;
    }
    
    
}
