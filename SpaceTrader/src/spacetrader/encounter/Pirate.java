/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.encounter;

import spacetrader.economy.TradeGood;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;

/**
 *
 * @author Aaron McAnally
 */
/*public class Pirate extends Encounter {
    private Player pirate;
    
    public Pirate() {
        int traderSkill = (int) (Math.random() * 10 + 1);
        pirate = new Player("trader", 5, 5, traderSkill, 5, 5);
        
        pirate.setShip(new Ship((ShipType)ShipType.get("FIREFLY")));
        int cargoFilled = (int) (Math.random() * 20 + 1);
        for (int i = 0; i < cargoFilled; i++) {
            pirate.getShip().getCargo().addTradeGood(new TradeGood());
        }
    }
    
    @Override
    public void handleEncounter() {
        Player player = SpaceTrader.getInstance().getPlayer();
    }
}*/
