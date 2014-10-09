package spacetrader.economy;

import java.util.ArrayList;
import java.util.Random;
import spacetrader.player.Player;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;
import spacetrader.turns.TurnListener;
import spacetrader.turns.TurnEvent;

/**
 * The Marketplace class allows players to buy and sell TradeGoods at each Planet.
 * The amount and price of each good will depend on the TechLevel and/or the
 * state of the Planet.
 * 
 * @author Carey MacDonald
 */
public class MarketPlace implements TurnListener {
    //private HashMap<TradeGood, TradeGood> tradeGoods;
    private ArrayList<TradeGood> tradeGoods;
    private Random rand;
    private TechLevel techLevel;
    private Resource resource;
    
    /**
     * Creates a new MarketPlace object based on the current techLevel and 
     * resource of the planet where the MarketPlace will be stored.
     * 
     * @param techLevel the techlevel of the planet
     * @param resource the resource of the planet
     */
    public MarketPlace(TechLevel techLevel, Resource resource) {
        rand = new Random();
        tradeGoods = new ArrayList<TradeGood>();
        ArrayList<TradeGood> list = TradeGood.getTradeGoodTypes();
        for (TradeGood good : list) {
            if (TechLevel.getIndex(good.getMinLevelUse()) <= TechLevel.getIndex(techLevel)) {
                if (TechLevel.getIndex(good.getMinLevelProduce()) <= TechLevel.getIndex(techLevel)) {
                    if (TechLevel.getIndex(good.getLevelProduceMost()) == TechLevel.getIndex(techLevel)) {
                        good.setAmount(rand.nextInt(20) + 20);
                    } else {
                        good.setAmount(rand.nextInt(20));
                    }
                }
                good.computeCurrentPriceEach(techLevel, resource);
                tradeGoods.add(good);
            }
        }
        this.techLevel = techLevel;
        this.resource = resource;
        TurnEvent.RegisterListener(this);
    }
    
    /**
     * Allows a Player to buy TradeGoods from this MarketPlace.
     * 
     * @param p the Player trying to buy TradeGoods from this MarketPlace
     * @param tg the TradeGood that Player p is trying to buy.
     * @param amount the amount of TradeGood tg that Player p is trying to buy.
     * @return true if buying was successful, false otherwise.
     */
    public boolean buy(Player p, TradeGood tg, int amount) {
        for(TradeGood t : tradeGoods) {
            if (tg.equals(t)) {
                if (t.getAmount() < amount) { //not enough in inventory
                    return false;
                }
                float cost = t.getCurrentPriceEach() * amount;
                TradeGood temp = new TradeGood(t);
                temp.setPrice(t.getCurrentPriceEach());
                temp.setAmount(amount);
                if (cost < p.getMoney() && p.addTradeGood(temp)) {
                    p.setMoney(p.getMoney() - cost);
                    t.setAmount(t.getAmount() - amount);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Allows a Player to sell TradeGoods to this MarketPlace.
     * 
     * @param p the Player trying to sell TradeGoods to this MarketPlace
     * @param tg the TradeGood that Player p is trying to sell.
     * @param amount the amount of TradeGood tg that Player p is trying to sell.
     * @return true if selling was successful, false otherwise.
     */
    public boolean sell(Player p, TradeGood tg, int amount) {
        for (TradeGood t : tradeGoods) {
            if (tg.equals(t)) {
                tg.setAmount(amount);
                if(p.removeTradeGood(tg)) {
                    p.setMoney(p.getMoney() + (tg.getCurrentPriceEach() * amount));
                    t.setAmount(t.getAmount() + amount);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns the amount of the given TradeGood is currently available in this
     * MarketPlace.
     * 
     * @param tg the TradeGood we need to know the amount of.
     * @return the amount of that TradeGood currently available.
     */
    public int amountOfGood(TradeGood tg) {
        for (TradeGood t : tradeGoods) {
            if (tg.equals(t)) {
                return t.getAmount();
            }
        }
        return 0;
    }
    
    /**
     * returns the current price of the given TradeGood.
     * 
     * @param tg the TradeGood we want the price of.
     * @return the price of the TradeGood.
     */
    public float priceOfGood(TradeGood tg) {
        for (TradeGood t: tradeGoods) {
            if (tg.equals(t)) {
                return t.getCurrentPriceEach();
            }
        }
        return 0;
    }
    
    /**
     * @return a list of the goods in the marketplace
     */
    public ArrayList<TradeGood> getListOfGoods() {
        return tradeGoods;
    }
    
    @Override
    public void handleNextTurn() {
        for(TradeGood tg : tradeGoods) {
            tg.computeCurrentPriceEach(techLevel, resource);
        }
    }
   
}
