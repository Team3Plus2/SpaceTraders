package spacetrader.economy;

import java.util.HashMap;
import spacetrader.cosmos.player.Player;
import spacetrader.cosmos.system.TechLevel;

/**
 * The Marketplace class allows players to buy and sell TradeGoods at each Planet.
 * The amount and price of each good will depend on the TechLevel and/or the
 * state of the Planet.
 * 
 * @author Carey MacDonald
 */
public class MarketPlace {
    private HashMap<TradeGood, TradeGood> tradeGoods;
    
    public MarketPlace(TechLevel techLevel) {
        
    }
    
    public boolean buy(Player p, TradeGood tg, int amount) {
        float cost = tradeGoods.get(tg).getCurrentPriceEach() * amount;
        tg.setAmount(amount);
        if (cost < p.getMoney() && p.getShip().getCargo().addTradeGood(tg)) {
            p.setMoney(p.getMoney() - cost);
            tradeGoods.get(tg).setAmount(tradeGoods.get(tg).getAmount() - amount);
            return true;
        }
        return false;
    }
    
    public boolean sell(Player p, TradeGood tg, int amount) {
        tg.setAmount(amount);
        return p.getShip().getCargo().removeTradeGood(tg);
    }
    
    public int amountOfGood(TradeGood tg) {
        return tradeGoods.get(tg).getAmount();
    }
}
