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
import spacetrader.turns.TurnEvent;
import spacetrader.turns.TurnSerializer;

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
        boolean success = true;
        ObjectInputStream objectReader = null;
        try {
            FileInputStream  inputFile = new FileInputStream(fileName);
            objectReader = new ObjectInputStream(inputFile);
            Player p = (Player) objectReader.readObject();
            s.setPlayer(p);
            Universe u = (Universe) objectReader.readObject();
            s.setUniverse(u);
            TurnSerializer ts = (TurnSerializer) objectReader.readObject();
            TurnEvent.load(ts);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            success = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            success = false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            success = false;
        }
        if (objectReader != null) {
            try {
                objectReader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return success;
    }
    
    /**
     * Method for loading the game.  Loads the given Player object and Universe
     * object in a file at the given fileName.
     * 
     * @param fileName the name of the file we are loading
     * @return true if the save was successful.
     */
    public static Player loadPlayer(String fileName) {
        Player p = null;
        ObjectInputStream objectReader = null;
        try {
            FileInputStream inputFile = new FileInputStream(fileName);
            objectReader = new ObjectInputStream(inputFile);
            p = (Player) objectReader.readObject();
            objectReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (objectReader != null) {
            try {
                objectReader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return p;
    }
}
