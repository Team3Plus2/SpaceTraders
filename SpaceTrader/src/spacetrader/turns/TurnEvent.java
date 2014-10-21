package spacetrader.turns;

import java.awt.AWTEvent;
import java.awt.Event;
import java.util.ArrayList;

/**
 * This event will be triggered every time there is a new turn
 * 
 * @author Alex
 */
public class TurnEvent {
    private static ArrayList<TurnListener> listeners;
        
    private static int turn = 0;
    
    /**
     * Register the provided listener with the TurnEvent
     * 
     * @param listener listener to register
     */
    public static void RegisterListener(TurnListener listener) {
        if(listeners == null)
            listeners = new ArrayList<TurnListener>();
        listeners.add(listener);
    }
    
    /**
     * Calls handleNextTurn() for all TurnListeners registered
     */
    public static void NextTurn() {
        turn++;
        for(TurnListener listener : listeners) {
            listener.handleNextTurn();
        }
    }
    
    /**
     * get the current game turn
     */
    public static int getTurn() {
        return turn;
    }
    
    /**
     * get a TurnSerializer object with the static turn event data
     * @return 
     */
    public static TurnSerializer getSerializer() {
        return new TurnSerializer(listeners, turn);
    }
    
    /**
     * Load the TurnEvent class with the serialized data
     * 
     * @param serialized data to load
     */
    public static void Load(TurnSerializer serialized) {
        listeners = serialized.getListeners();
        turn = serialized.getTurn();
    }
}
