package spacetrader.xml;

import java.util.ArrayList;

import spacetrader.economy.TradeGood;
import spacetrader.cosmos.system.Resource;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Government;
import spacetrader.cosmos.system.SunType;
import spacetrader.player.Gadget;

/**
 * 
 * This class may be used to load objects specified in xml files and stores them
 * in a enum-type accessible setup
 *
 * @author Alex
 */
public class ObjectLoader {
    
    public static void LoadAllObjects() {
        Resource.Load();
        TechLevel.Load();
        TradeGood.Load();
        Government.Load();
        SunType.Load();
        Gadget.Load();
    }
    
}
