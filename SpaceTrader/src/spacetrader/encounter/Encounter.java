package spacetrader.encounter;

import java.util.ArrayList;

import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.TradeGood;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;
import spacetrader.player.Weapon;
import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;

/**
 * An interface for the trader, pirate, and police encounters
 * 
 * @author Aaron McAnally
 */
public class Encounter{
    
    public static void Load() {
        EncounterType.Load();
    }
    
    private Player captain;
    private SolarSystem systemAssociation;
    private EncounterType type;
    private boolean willAttack;
    private boolean willTrade;
    private ArrayList<TradeGood> searchFor;

    
    public Encounter(SolarSystem system, Player player) {
        type = EncounterType.Random();
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
        
        for(int i = 0; i < captain.getShip().getMaxWeapons(); i++) {
            captain.getShip().addWeapon(Weapon.Random());
        }
        
        for(int i = 0; i < captain.getShip().getMaxShields(); i++) {
            captain.getShip().addShield(Shield.Random());
        }
        
        for(int i = 0; i < captain.getShip().getMaxGadgets(); i++) {
            captain.getShip().addGadget(Gadget.Random());
        }
        
        
        
        int cargoFilled = (int) (Math.random() * type.getMaxCargoUsed() + 1);
        for (int i = 0; i < cargoFilled; i++) {
            captain.getShip().getCargo().addTradeGood(new TradeGood());
        }
        
        if(type.isAssociatedWithSystem()) {
            systemAssociation = system;
            if(system.shouldAttack()) {
                willAttack = true;
            } else if(system.shouldSearch()) {
                searchFor = new ArrayList<TradeGood>(type.getLookingFor());
            }
        } else {
            searchFor = null;
            willAttack = type.getAggression() > Math.random();
            willTrade = type.getTrade() > Math.random();
        }
    }
    
    public String getGreeting() {
        return type.getGreeting();
        //"Hey, gimme all ur stufs, im pore and nede mor mony >:0";
    }
    
    public boolean willAttack() {
        return willAttack;
    }
    
    public boolean willingToTrade() {
        return type.getWillingToTrade();
    }
    
    public boolean willRequestTrade() {
        return willTrade;
    } 
    
    public boolean willRequestSearch() {
        return !searchFor.isEmpty();
    }
    
    public boolean willSurrender() {
        return captain.getShip().getPower() == 0;
    }
    
    public ArrayList<TradeGood> getGoods() {
        return captain.getShip().getCargo().getCargoList();
    }
    
    /**
     * Simulates one round of combat
     * 
     * @param player the player involved in the combat
     * @param targets the parts of the ship the player wishes to target
     * @return 0: both still alive, -1: enemy dead, -2: enemy surrenders, 1: player dead
     */
    public int roundOfCombat(Player player, ArrayList targets) {
        if(captain.attack(player, null))
            return 1;
        
        if(player.attack(captain, targets))
            return -1;
        
        if(willSurrender())
            return -2;
        
        return 0;
    }
    
    /**
     * 
     * @param other 
     * @return true if something is found
     */
    public boolean search(Player other) {
        return other.removeTradeGoodsByType(searchFor);
    }
    
    //public abstract void handleEncounter();
}