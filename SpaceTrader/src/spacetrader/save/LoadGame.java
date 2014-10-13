/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.save;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import spacetrader.cosmos.Universe;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 *
 * @author Carey MacDonald
 */
public class LoadGame {
    /**
     * Method for loading the game.  Loads the given Player object and Universe
     * object in a file at the given fileName.
     * 
     * @param fileName the name of the file we are loading
     * @param s the SpaceTrader object we are setting the Player and Universe of.
     * @return true if the save was successful.
     */
    public static boolean load(String fileName, SpaceTrader s) {
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
        ObjectInputStream objectReader;
        try {
            objectReader = new ObjectInputStream(inputFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Player p = null;
        Universe u = null;
        try {
            p = (Player) objectReader.readObject();
            s.setPlayer(p);
            u = (Universe) objectReader.readObject();
            s.setUniverse(u);
            objectReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
