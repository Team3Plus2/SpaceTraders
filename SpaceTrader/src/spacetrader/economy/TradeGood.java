package spacetrader.economy;

import java.util.ArrayList;
import java.util.Random;

import spacetrader.xml.XMLReader;
import spacetrader.xml.FromXML;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;

/**
 * TradeGood defines the various types of TradeGoods that players can buy or sell.
 * Trade goods should have the following names:
 *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots
 * 
 * @author Carey MacDonald
 */
public class TradeGood {
    
    private static final String tradeGoodsFile = "objects/TradeGoods.xml";
    
    private static ArrayList<TradeGood> tradeGoodTypes;
    
    /**
     * Loads the various TradeGood types from TradeGoods.
     */
    public static void LoadTradeGoods() {
        XMLReader reader = new XMLReader(TradeGood.class, tradeGoodsFile);
        tradeGoodTypes = reader.read();
    }

    @FromXML
    private String name;
    
    @FromXML
    private float basePrice, increasePerLevel, minRandPrice, maxRandPrice;
    
    @FromXML
    private int priceVariance;
    
    @FromXML
    private TechLevel minLevelProduce, minLevelUse, levelProduceMost;
    
    @FromXML (required = false)
    private Resource priceLowCondition, priceHighCondition;
    
    private float currentPriceEach;
    
    private int amount;
    
    private Random rand;

    /**
     * Getter for currentPriceEach.
     * 
     * @return currentPriceEach
     */
    public float getCurrentPriceEach() {
        return currentPriceEach;
    }
    
    /**
     * Computes the currentPriceEach of this TradeGood based off the current
     * planet's TechLevel and Resource status.
     * 
     * @param planetLevel the TechLevel of the planet this TradeGood is being sold on.
     * @param resource the Resource of the planet this TradeGood is being sold on.
     */
    public void computeCurrentPriceEach(TechLevel planetLevel, Resource resource) {
        int variance1 = rand.nextInt(1);
        int variance2 = rand.nextInt(priceVariance);
        if (variance1 == 0) {
            if (resource.equals(priceLowCondition)) {
                currentPriceEach = (basePrice * .5) + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    - (basePrice * ((float) variance2/100));
            } else if (resource.equals(priceHighCondition)) {
                currentPriceEach = (basePrice * 1.5) + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    - (basePrice * ((float) variance2/100));
            } else {
                currentPriceEach = basePrice + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    - (basePrice * ((float) variance2/100));
            }
        } else {
            if (resource.equals(priceLowCondition)) {
                currentPriceEach = (basePrice * .5) + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    + (basePrice * ((float) variance2/100));
            } else if (resource.equals(priceHighCondition)) {
                currentPriceEach = (basePrice * 1.5) + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    + (basePrice * ((float) variance2/100));
            } else {
                currentPriceEach = basePrice + (increasePerLevel * (TechLevel.getLevelNum(planetLevel) - minLevelProduce))
                    + (basePrice * ((float) variance2/100));
            }
        }
    }
    
    /**
     * Getter for amount.
     * 
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for amount.
     * 
     * @param amount the amount we want to set this TradeGood to have.
     */
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
        this.rand = Random();
    }
    
    /**
     * Required by XMLReader
     */
    public TradeGood() {
        this.rand = Random();
    }
    
    /**
     * Getter for name.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for tradeGoodTypes.
     * 
     * @return tradeGoodTypes
     */
    public static ArrayList<TradeGood> getTradeGoodTypes() {
        return tradeGoodTypes;
    }

    /**
     * Getter for minRandPrice.
     * 
     * @return minRandPrice
     */
    public float getMinRandPrice() {
        return minRandPrice;
    }

    /**
     * Getter for maxRandPrice.
     * 
     * @return maxRandPrice
     */
    public float getMaxRandPrice() {
        return maxRandPrice;
    }

    /**
     * Getter for minLevelProduce.
     * 
     * @return minLevelProduce
     */
    public TechLevel getMinLevelProduce() {
        return minLevelProduce;
    }

    /**
     * Getter for minLevelUse.
     * 
     * @return minLevelUse
     */
    public TechLevel getMinLevelUse() {
        return minLevelUse;
    }

    /**
     * Getter for levelProduceMost.
     * 
     * @return levelProduceMost
     */
    public TechLevel getLevelProduceMost() {
        return levelProduceMost;
    }
    
    /**
     * Overriding equals to determine if two TradeGood objects are equal.
     * 
     * @param other the Object we want to know if this is equal to.
     * @return true is this and other are equal.
     */
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other instanceof TradeGood) {
            TradeGood tg = TradeGood (other);
            if (tg.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }

}
