package spacetrader.encounter;

import spacetrader.economy.TradeGood;
import spacetrader.player.Player;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;

/**
 * An interface for the trader, pirate, and police encounters
 * 
 * @author Aaron McAnally
 */
public abstract class Encounter {
    private Player ringleader;
    
    public Encounter() {
        int traderSkill = (int) (Math.random() * 10 + 1);
        ringleader = new Player("trader", 5, 5, traderSkill, 5, 5);
        
        ringleader.setShip(new Ship((ShipType)ShipType.get("FIREFLY")));
        int cargoFilled = (int) (Math.random() * 20 + 1);
        for (int i = 0; i < cargoFilled; i++) {
            ringleader.getShip().getCargo().addTradeGood(new TradeGood());
        }
    }
    
    public abstract void handleEncounter();
}