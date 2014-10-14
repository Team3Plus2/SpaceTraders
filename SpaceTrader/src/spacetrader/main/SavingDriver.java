/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.main;

import spacetrader.save.*;
import spacetrader.player.Player;
import spacetrader.cosmos.Universe;
import spacetrader.player.Ship;
import spacetrader.xml.ObjectLoader;

/**
 * @author Careyjmac
 */

public class SavingDriver {
    public static void main(String[] args) {
        ObjectLoader.LoadAllObjects();
        Player p = new Player("Carey", 3, 2, 5, 5, 0);
        p.setShip(new Ship());
        Universe u = new Universe();
        boolean check = SaveGame.save("test.sav", p , u);
        if (!check) {
            System.out.println("Saving failed");
        } else {
            SpaceTrader s = new SpaceTrader();
             System.out.println("Unloaded name: " + s.getPlayer().getName());
             System.out.println("Unloaded skills: " + s.getPlayer().getPilotSkill()
                                                     + s.getPlayer().getFighterSkill()
                                                     + s.getPlayer().getTraderSkill()
                                                     + s.getPlayer().getEngineerSkill()
                                                     + s.getPlayer().getInvestorSkill());
            boolean check2 = LoadGame.load("test.sav", s);
            if (!check2) {
                System.out.println("Loading failed");
            } else {
                System.out.println("Loaded name: " + s.getPlayer().getName());
                System.out.println("Loaded skills: " + s.getPlayer().getPilotSkill()
                                                     + s.getPlayer().getFighterSkill()
                                                     + s.getPlayer().getTraderSkill()
                                                     + s.getPlayer().getEngineerSkill()
                                                     + s.getPlayer().getInvestorSkill());
            }
        }
    }
}
