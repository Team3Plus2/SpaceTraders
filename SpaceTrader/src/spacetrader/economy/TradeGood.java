package spacetrader.economy;

import java.util.ArrayList;

import spacetrader.xml.XMLReader;
import spacetrader.xml.FromXML;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Resource;

/**
 *
 * @author Carey MacDonald
 */
public class TradeGood {
    
    private static final String tradeGoodsFile = "objects/TradeGoods.xml";
    
    private static ArrayList<TradeGood> tradeGoodTypes;
    
    public static void LoadTradeGoods() {
        XMLReader reader = new XMLReader(TradeGood.class, tradeGoodsFile);
        tradeGoodTypes = reader.read();
    }

    //private int amount;
    @FromXML
    private String name;
    
    @FromXML
    private float basePrice, increasePerLevel, priceVariance, minRandPrice, maxRandPrice;
    
    @FromXML
    private TechLevel minLevelProduce, minLevelUse, levelProduceMost;
    
    @FromXML
    private Resource priceLowCondition, priceHighCondition;
    
    @FromXML(required = false)
    private float currentPrice;
    
    public void computeCurrentPrice(TechLevel planetLevel) {
        
    }
}
