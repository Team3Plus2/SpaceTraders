package spacetrader.economy;

import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;

/**
 * Trade goods should have the following names:
 *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots
 * 
 * @author Carey MacDonald
 */
public class TradeGood {
    private String name;
    private float basePrice, increasePerLevel, priceVariance, minRandPrice, maxRandPrice;
    private TechLevel minLevelProduce, minLevelUse, levelProduceMost;
    private Resource priceLowCondition, priceHighCondition;
    
    private float currentPriceEach;
    private int amount;

    public float getCurrentPriceEach() {
        return currentPriceEach;
    }
    
    public void computeCurrentPriceEach(TechLevel planetLevel) {
        
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
    
    public String getName() {
        return name;
    }

}
