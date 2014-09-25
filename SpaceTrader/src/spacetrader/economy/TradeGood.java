package spacetrader.economy;

import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resources;

/**
 * Trade goods should have the following names:
 *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots
 * 
 * @author Carey MacDonald
 */
public class TradeGood {
    private int amount;
    private String name;
    private float basePrice, increasePerLevel, priceVariance, minRandPrice, maxRandPrice;
    private TechLevel minLevelProduce, minLevelUse, levelProduceMost;
    private Resources priceLowCondition, priceHighCondition;
    
    private float currentPrice;

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    /**
     * Trade goods should have the following names:
 *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots
     * 
     * @param name 
     */
    public TradeGood(String name) {
        this.name = name;
        this.amount = 0;
    }
    
    public void computeCurrentPrice(TechLevel planetLevel) {
        
    }
    
    public String getName() {
        return name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
