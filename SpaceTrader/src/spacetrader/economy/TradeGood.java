package spacetrader.economy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import spacetrader.xml.LoadedType;
import spacetrader.xml.FromXML;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;
import spacetrader.global.Utility;

/**
 * TradeGood defines the various types of TradeGoods that players can buy or sell.
 * Trade goods should have the following names:
 *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots
 * 
 * Doesn't ovverride .equals since it should actually just use the Loaded Type super class check
 * 
 * @author Carey MacDonald
 */
public class TradeGood extends LoadedType {
    
    /**
     * An id for serialization.
     */
    static final long serialVersionUID = (long) 52L;
    
    /**
     * The file that contains all the TradeGood type definitions.
     */
    private static final String TRADES_GOODS_FILE = "objects/TradeGoods.xml";
    
    /**
     * Loads the various TradeGood types from TradeGoods.
     */
    public static void load() {
        TradeGood.load(TradeGood.class, TRADES_GOODS_FILE, null);
    }
    
    /**
     * Loads the default TradeGood type from TradeGoods.
     * 
     * @return the default TradeGood type
     */
    public static TradeGood defaultValue() {
        return (TradeGood) TradeGood.defaultValue(TradeGood.class);
    }
    
    /**
     * 
     * @return a single TradeGood of a randomly selected type
     */
    public static TradeGood randomSingleInstance() {
        Random rand = new Random();
        ArrayList<TradeGood> goods = TradeGood.getList(TradeGood.class);
        TradeGood good = (TradeGood) goods.get(rand.nextInt(goods.size()));
        good.setAmount(1);
        return good;
    }

    /**
     * BasePrice of this TradeGood.
     */
    @FromXML
    private float basePrice;
    /**
     * IncreasePerLevel of this TradeGood.
     */
    @FromXML
    private float increasePerLevel;
    /**
     * MinRandPrice of this TradeGood.
     */
    @FromXML
    private float minRandPrice;
    /**
     * MaxRandPrice of this TradeGood.
     */
    @FromXML
    private float maxRandPrice;
    
    /**
     * PriceVariance of this TradeGood.
     */
    @FromXML
    private int priceVariance;
    
    /**
     * MinLevelProduce of this TradeGood.
     */
    @FromXML
    private TechLevel minLevelProduce;
    /**
     * MinLevelUse of this TradeGood.
     */
    @FromXML
    private TechLevel minLevelUse;
    /**
     * LevelProduceMost of this TradeGood.
     */
    @FromXML
    private TechLevel levelProduceMost;
    
    /**
     * PriceLowCondition of this TradeGood.
     */
    @FromXML (required = false)
    private Resource priceLowCondition;
    /**
     * PriceHighCondition of this TradeGood.
     */
    @FromXML (required = false)
    private Resource priceHighCondition;
    /**
     * CurrentPriceEach of this TradeGood.
     */
    private float currentPriceEach;
    /**
     * Amount of this TradeGood.
     */
    private int amount;
    /**
     * Random variable for computing price.
     */
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
        boolean variance1 = rand.nextBoolean();
        int variance2 = rand.nextInt(priceVariance);
        if (variance1) {
            if (resource.equals(priceLowCondition)) {
                currentPriceEach = (basePrice * .5f) + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) - (basePrice * ((float) variance2 / 100));
            } else if (resource.equals(priceHighCondition)) {
                currentPriceEach = (basePrice * 1.5f) + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) - (basePrice * ((float) variance2 / 100));
            } else {
                currentPriceEach = basePrice + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) - (basePrice * ((float) variance2 / 100));
            }
        } else {
            if (resource.equals(priceLowCondition)) {
                currentPriceEach = (basePrice * .5f) + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) + (basePrice * ((float) variance2 / 100));
            } else if (resource.equals(priceHighCondition)) {
                currentPriceEach = (basePrice * 1.5f) + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) + (basePrice * ((float) variance2 / 100));
            } else {
                currentPriceEach = basePrice + (increasePerLevel * (TechLevel.getIndex(planetLevel)
                        - TechLevel.getIndex(minLevelProduce))) + (basePrice * ((float) variance2 / 100));
            }
        }
        if (currentPriceEach <= 0) {
            currentPriceEach = basePrice;
        }
    }
    
    /**
     * Price setter.
     * 
     * @param price the price to set the good to
     */
    public void setPrice(float price) {
        this.currentPriceEach = price;
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
     * @param amount2 the amount we want to set this TradeGood to have.
     */
    public void setAmount(int amount2) {
        this.amount = amount2;
    }
    
    /**
     * Trade goods should have the following names:
     *      water, furs, food, ore, games, firearms, medicine, machines, narcotics, robots.
     * 
     * @param good the TradeGood type to be instantiated
     */
    public TradeGood(TradeGood good) {
        super(good.getName());
        this.amount = 0;
        this.rand = new Random();
        this.minLevelProduce = good.minLevelProduce;
        this.minLevelUse = good.minLevelUse;
        this.basePrice = good.basePrice;
        this.currentPriceEach = good.currentPriceEach;
        this.levelProduceMost = good.levelProduceMost;
        this.priceVariance = good.priceVariance;
        this.priceLowCondition = good.priceLowCondition;
        this.priceHighCondition = good.priceHighCondition;
    }
    
    /**
     * Required by XMLReader.
     */
    public TradeGood() {
        this.rand = new Random();
    }

    /**
     * Getter for tradeGoodTypes.
     * 
     * @return tradeGoodTypes
     */
    public static ArrayList<TradeGood> getTradeGoodTypes() {
        ArrayList<TradeGood> list = (ArrayList<TradeGood>) TradeGood.getList(TradeGood.class);
        ArrayList<TradeGood> list2 = new ArrayList<TradeGood>();
        for (TradeGood tg : list) {
            list2.add(new TradeGood(tg));
        }
        return list2;
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

    @Override
    public String toString() {
        return getName() + " - " + Utility.currencyFormat(getCurrentPriceEach()) + " - Amount: " + getAmount();
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) { return false; }
        if (other instanceof TradeGood && ((TradeGood) other).getName().equals(this.getName())) {
            return true;
        }
        return false;
    }
}
