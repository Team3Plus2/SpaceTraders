package spacetrader.turns;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * This class may be used to save TurnEvent data
 * @author Alex
 */
public class TurnSerializer implements Serializable {
    private ArrayList<TurnListener> listeners;
    private int turn = 0;
    
    TurnSerializer(ArrayList<TurnListener> listeners, int turn) {
        this.listeners = listeners;
        this.turn = turn;
    }
    
    /**
     * Get the listeners of the serialized object- package protected
     */
    ArrayList<TurnListener> getListeners() {
        return listeners;
    }
    
    /**
     * Get the turn number of the serialized object- package protected
     */
    int getTurn() {
        return turn;
    }
}
