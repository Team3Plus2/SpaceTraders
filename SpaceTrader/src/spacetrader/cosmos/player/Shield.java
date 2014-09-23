/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.cosmos.player;

/**
 * A shield is added protection to a ship.
 * 
 * @author Aaron McAnally
 */
public class Shield {
    
    /**
     * ShieldTypes:
     * ShieldType(1): energy shield
     * ShieldType(2): reflective shield
     */
    public enum ShieldType {
        ENERGY(1), REFLECTIVE(2);
        
        private final int strength;
        ShieldType(int strength) {
            this.strength = strength;
        }
    }
    
    private ShieldType shieldType;
    
    /**
     * @param shieldType an enum that determines the type of shield
     */
    public Shield(ShieldType shieldType) {
        this.shieldType = shieldType;
    }
}
