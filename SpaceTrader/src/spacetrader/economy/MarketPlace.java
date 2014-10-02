package spacetrader.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import spacetrader.player.Player;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;

/**
 * The Marketplace class allows players to buy and sell TradeGoods at each Planet.
 * The amount and price of each good will depend on the TechLevel and/or the
 * state of the Planet.
 * 
 * @author Carey MacDonald
 */
public class MarketPlace {
    private HashMap<TradeGood, TradeGood> tradeGoods;
    private Random rand;
    
    /**
     * Creates a new MarketPlace object based on the current techLevel and 
     * resource of the planet where the MarketPlace will be stored.
     * 
     * @param techLevel the techlevel of the planet
     * @param resource the resource of the planet
     */
    public MarketPlace(TechLevel techLevel, Resource resource) {
        rand = new Random();
        //ArrayList<TradeGood> tradeGoodTypes = TradeGood.getTradeGoodTypes();
        tradeGoods = TradeGood.getEmptyGoodMap();
        for (TradeGood good : tradeGoods.values()) {
            //TradeGood good = new TradeGood(type);
            if (TechLevel.getIndex(good.getMinLevelUse()) >= TechLevel.getIndex(techLevel)) {
                if (TechLevel.getIndex(good.getMinLevelProduce()) >= TechLevel.getIndex(techLevel)) {
                    if (TechLevel.getIndex(good.getLevelProduceMost()) == TechLevel.getIndex(techLevel)) {
                        good.setAmount(rand.nextInt(100) + 100);
                    } else {
                        good.setAmount(rand.nextInt(100));
                    }
                    good.computeCurrentPriceEach(techLevel, resource);
                }
                //tradeGoods.put(type, good);
            }
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
//        System.out.println(tradeGoods.get(tg));
//        if (tradeGoods.get(tg) == null || tradeGoods.get(tg).getAmount() < amount) {
//            return false;
//        }
//        
//        System.out.println("here");
//        float cost = tradeGoods.get(tg).getCurrentPriceEach() * amount;
//        tg.setAmount(amount);
//        if (cost < p.getMoney() && p.getShip().getCargo().addTradeGood(tg)) {
//            p.setMoney(p.getMoney() - cost);
//            tradeGoods.get(tg).setAmount(tradeGoods.get(tg).getAmount() - amount);
//            return true;
//        }
//        return false;
        float cost = tg.getCurrentPriceEach() * amount;
        TradeGood temp = new TradeGood(tg);
        temp.setPrice(tg.getCurrentPriceEach());
        temp.setAmount(amount);
        if (cost < p.getMoney() && p.getShip().getCargo().addTradeGood(temp)) {
            p.setMoney(p.getMoney() - cost);
            tg.setAmount(tg.getAmount() - amount);
            return true;
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
//        if (tradeGoods.get(tg) == null) { //this planet cannot use this TradeGood
//            return false;
//        }
//        tg.setAmount(amount);
//        if (p.getShip().getCargo().removeTradeGood(tg)) {
//            tradeGoods.get(tg).setAmount(tradeGoods.get(tg).getAmount() + amount);
//            p.setMoney(p.getMoney() + (tradeGoods.get(tg).getCurrentPriceEach() * amount));
//            return true;
//        }
//        return false;
        tg.setAmount(amount);
        if(p.getShip().getCargo().removeTradeGood(tg)) {
            p.setMoney(p.getMoney() + (tg.getCurrentPriceEach() * amount));
            System.out.println("FJLKSD");
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
        return tradeGoods.get(tg).getAmount();
    }
    
    /**
     * returns the current price of the given TradeGood.
     * 
     * @param tg the TradeGood we want the price of.
     * @return the price of the TradeGood.
     */
    public float priceOfGood(TradeGood tg) {
        for (TradeGood good: tradeGoods.values()) {
            if (tg.getName().equals(good.getName())) {
                return good.getCurrentPriceEach();
            }
        }
        return 0;
    }
    
    /**
     * @return a list of the goods in the marketplace
     */
    public ArrayList<TradeGood> getListOfGoods() {
        ArrayList<TradeGood> goods = new ArrayList<TradeGood>();
        for (TradeGood good: tradeGoods.values()) {
            if (good.getAmount() > 0) {
                goods.add(good);
            }
        }
        return goods;
    }
    
    //TODO Implement method to update MarketPlace prices/amounts/items across turns
   
}
