/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.cosmos;

/**
 *
 * @author Alex
 */
public class SolarSystem extends OuterSpace {
    
    private SunType sun;
    
    public SolarSystem() {
        sun = SunType.Random();
    }
    
    @Override
    public String toString() {
        return "S";
    }
}
