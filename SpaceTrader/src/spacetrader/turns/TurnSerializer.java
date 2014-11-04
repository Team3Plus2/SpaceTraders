package spacetrader.turns;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * This class may be used to save TurnEvent data.
 * @author Alex
 */
public class TurnSerializer implements Serializable {
    
    /**
     * Used for Serializable class.
     */
    static final long serialVersionUID = 103L;
    
    /**
     * An ArrayList of listeners.
     */
    private ArrayList<TurnListener> listeners;
    /**
     * The current turn the player is on.
     */
    private int turn = 0;
    
    /**
     * Initializes the turn and the ArrayList of listeners.
     * @param listeners2 ArrayList of listeners.
     * @param turn2 The turn you are on
     */
    TurnSerializer(ArrayList<TurnListener> listeners2, int turn2) {
        this.listeners = listeners2;
        this.turn = turn2;
    }
    
    /**
     * Get the listeners of the serialized object- package protected.
     * @return listeners returns the ArrayList of listeners.
     */
    ArrayList<TurnListener> getListeners() {
        return listeners;
    }
    
    /**
     * Get the turn number of the serialized object- package protected.
     * @return turn the turn the player is currently on.
     */
    int getTurn() {
        return turn;
    }
}
