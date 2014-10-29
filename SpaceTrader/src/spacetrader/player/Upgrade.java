/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.player;

/**
 * Used for polymorphism
 * 
 * @author Aaron McAnally
 */
public abstract class Upgrade {
    public abstract String getClassName();
    public abstract int getPrice();
    public abstract int getTechLevel();
    @Override
    public abstract String toString();
}
