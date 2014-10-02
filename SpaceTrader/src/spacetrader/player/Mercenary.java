/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

import spacetrader.xml.LoadedType;

/**
 * Mercenaries can be hired on the ship to add to player skills.
 * 
 * @author Aaron McAnally
 */
public class Mercenary {
    
    private MercenaryType mercenaryType;
    
    public static void Load() {
        MercenaryType.Load();
    }
    
    public Mercenary(MercenaryType mercenaryType) {
        this.mercenaryType = mercenaryType;
    }
}

/*
 *PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
 */
class MercenaryType extends LoadedType {

    private static final String MercenaryFileLocation = "objects/Mercenaries.xml";

    public static void Load() {
        MercenaryType.Load(MercenaryType.class, MercenaryFileLocation, null);
    }

    public MercenaryType() {
        
    }
}
