package spacetrader.economy;

import java.io.Serializable;
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
public class MarketPlace implements TurnListener, Serializable {
    private ArrayList<TradeGood> tradeGoods;
    private Random rand;
    private TechLevel techLevel;
    private Resource resource;
    private boolean lootingExchange; //if true, then this "marketplace" is a forced exchange from a ship a player has beaten
    
    /**
     * Creates a new MarketPlace object based on the current techLevel and 
     * resource of the planet where the MarketPlace will be stored.
     * 
     * @param techLevel2 the techlevel of the planet
     * @param resource2 the resource of the planet
     */
    public MarketPlace(TechLevel techLevel2, Resource resource2) {
        lootingExchange = false;
        rand = new Random();
        tradeGoods = new ArrayList<TradeGood>();
        ArrayList<TradeGood> list = TradeGood.getTradeGoodTypes();
        for (TradeGood good : list) {
            if (TechLevel.getIndex(good.getMinLevelUse()) <= TechLevel.getIndex(techLevel2)) {
                if (TechLevel.getIndex(good.getMinLevelProduce()) <= TechLevel.getIndex(techLevel2)) {
                    if (TechLevel.getIndex(good.getLevelProduceMost()) == TechLevel.getIndex(techLevel2)) {
                        good.setAmount(rand.nextInt(20) + 20);
                    } else {
                        good.setAmount(rand.nextInt(20));
                    }
                }
                good.computeCurrentPriceEach(techLevel2, resource2);
                tradeGoods.add(good);
            }
        }
        this.techLevel = techLevel2;
        this.resource = resource2;
        TurnEvent.registerListener(this);
    }
    
    /**
     * Takes in an already initialized list of goods for a singular turn marketplace.
     * 
     * @param goods goods to put in the marketplace
     */
    public MarketPlace(ArrayList<TradeGood> goods) {
        lootingExchange = false;
        tradeGoods = goods;
    }
    
    /**
     * Takes in an already initialized list of goods for a singular turn marketplace
     * or looting exchange.
     * 
     * @param goods goods to put in the marketplace
     * @param looting if true, then the marketplace is instead a looting exchange
     */
    public MarketPlace(ArrayList<TradeGood> goods, boolean looting) {
        lootingExchange = looting;
        tradeGoods = goods;
    }
    
    /**
     * Calculate the price of all the marketplace goods with the current location.
     * @param localTech the technology of the local area
     */
    public void setupGoodsWithLocale(TechLevel localTech) {
        for (TradeGood a : tradeGoods) {
            a.computeCurrentPriceEach(localTech, Resource.Default());
        }
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
        for (TradeGood t : tradeGoods) {
            if (tg.equals(t)) {
                if (t.getAmount() < amount) { //not enough in inventory
                    return false;
                }
                float cost = computeCost(t, amount);
                
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
                if (p.removeTradeGood(tg)) {
                    p.setMoney(p.getMoney() + computeCost(tg, amount));
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
        if (lootingExchange) {
            return 0;
        }
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
    public void handleNextTurn(Player player) {
        for (TradeGood tg : tradeGoods) {
            tg.computeCurrentPriceEach(techLevel, resource);
        }
    }
    
    /**
     * Compute the complete cost of the given amount of TradeGoods.
     * 
     * @param good goods to check
     * @param amount amount of goods to check
     * @return complete cost of given goods
     */
    private float computeCost(TradeGood good, int amount) {
        float cost = good.getCurrentPriceEach() * amount;
        if (lootingExchange) {
            cost = 0;
        }
        return cost;
    }
   
}
