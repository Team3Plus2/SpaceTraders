/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.cosmos.player;

/**
 * A weapon may be stored on a ship.
 * 
 * @author Aaron McAnally
 */
public class Weapon {
    
    /**
     * LaserTypes:
     * LaserType(1) = pulse laser
     * LaserType(2) = beam laser
     * LaserType(3) = military laser
     */
    public enum LaserType {
        PLUSE(1), BEAM(2), MILITARY(3);
        
        private final int strength;
        LaserType(int strength) {
            this.strength = strength;
        }
    }
    
    private LaserType laserType;
    
    /**
     * @param laserType an enum that determines the type of weapon
     */
    public Weapon(LaserType laserType) {
        this.laserType = laserType;
    }
    
    public LaserType getLaserType() {
        return laserType;
    }
}
