package spacetrader.xml;

import java.util.ArrayList;

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
 * in a enum-type accessible setup
 *
 * @author Alex
 */
public class ObjectLoader {
    
    public static void LoadAllObjects() {
        Resource.Load();
        TechLevel.Load();
        TradeGood.load();
        Government.Load();
        SunType.Load();
        Gadget.load();
        Mercenary.load();
        Shield.Load();
        Ship.Load();
        Weapon.Load();
        Encounter.load();
    }
    
}
