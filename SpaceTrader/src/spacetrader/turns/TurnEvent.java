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
    
    public static void RegisterListener(TurnListener listener) {
        if(listeners == null)
            listeners = new ArrayList<TurnListener>();
        listeners.add(listener);
    }
    
    public static void NextTurn() {
        turn++;
        for(TurnListener listener : listeners) {
            listener.handleNextTurn();
        }
    }
    
    public static int getTurn() {
        return turn;
    }
}
