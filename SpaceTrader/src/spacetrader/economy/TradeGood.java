package spacetrader.economy;

import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resources;

/**
 *
 * @author Carey MacDonald
 */
public class TradeGood {
    //private int amount;
    private String name;
    private float basePrice, increasePerLevel, priceVariance, minRandPrice, maxRandPrice;
    private TechLevel minLevelProduce, minLevelUse, levelProduceMost;
    private Resources priceLowCondition, priceHighCondition;
    
    private float currentPrice;
    
    public void computeCurrentPrice(TechLevel planetLevel) {
        
    }
}
