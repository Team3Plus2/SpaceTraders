/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.save;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import spacetrader.cosmos.Universe;
import spacetrader.player.Player;
import spacetrader.turns.TurnEvent;
import spacetrader.turns.TurnSerializer;

/**
 * Class for saving the game.
 * 
 * @author Carey MacDonald
 */
public class SaveGame {
    
    /**
     * Method for saving the game.  Saves the given Player object and Universe
     * object in a file at the given fileName.  Overwrites the file at fileName
     * if it already exists.
     * 
     * @param fileName the name of the file we are saving
     * @param p the Player object we are saving
     * @param u the Universe object we are saving
     * @return true if the save was successful.
     */
    public static boolean save(String fileName, Player p, Universe u) {
        boolean success = true;
        ObjectOutputStream objectWriter = null;
        try {
            FileOutputStream outputFile = new FileOutputStream(fileName);
            objectWriter = new ObjectOutputStream(outputFile);
            objectWriter.writeObject(p);
            objectWriter.writeObject(u);
            TurnSerializer ts = TurnEvent.getSerializer();
            objectWriter.writeObject(ts);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            success = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            success = false;
        }
        if (objectWriter != null) {
            try {
                objectWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return success;
    }
}
