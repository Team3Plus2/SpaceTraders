package spacetrader.xml;

import spacetrader.economy.TradeGood;
import spacetrader.cosmos.system.Resource;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.Government;
import spacetrader.cosmos.system.SunType;
import spacetrader.player.Gadget;
import spacetrader.player.Mercenary;
import spacetrader.player.Shield;
import spacetrader.player.Ship;
import spacetrader.player.Weapon;
import spacetrader.encounter.Encounter;

/**
 * 
 * This class may be used to load objects specified in xml files and stores them
 * in a enum-type accessible setup.
 *
 * @author Alex
 */
public class ObjectLoader {
    
    /**
     * This is a driver to load in all the xml types.
     */
    public static void loadAllObjects() {
        Resource.Load();
        TechLevel.Load();
        Government.load();
        TradeGood.load();
        SunType.load();
        Gadget.load();
        Mercenary.load();
        Shield.Load();
        Ship.Load();
        Weapon.Load();
        Encounter.load();
    }
    
}
