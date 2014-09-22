/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.cosmos.player;

/**
 * Mercenaries can be hired on the ship to add to player skills.
 * 
 * @author Aaron McAnally
 */
public class Mercenary {
    public enum MercenaryType {
        PILOT, FIGHTER, TRADER, ENGINEER, INVESTOR;
    }
    
    private MercenaryType mercenaryType;
    
    public Mercenary(MercenaryType mercenaryType) {
        this.mercenaryType = mercenaryType;
    }
}
