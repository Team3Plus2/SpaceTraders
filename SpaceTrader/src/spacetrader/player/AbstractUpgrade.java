/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

/**
 * Used for polymorphism.
 * 
 * @author Aaron McAnally
 */
public abstract class AbstractUpgrade {
    /**
     * @return The class name that extends Upgrade
     */
    public abstract String getClassName();
    /**
     * @return the price of the upgrade
     */
    public abstract int getPrice();
    /**
     * @return the tech level of the upgrade
     */
    public abstract int getTechLevel();
    @Override
    public abstract String toString();
}
