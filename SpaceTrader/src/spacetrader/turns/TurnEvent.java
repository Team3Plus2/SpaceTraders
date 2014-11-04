package spacetrader.turns;

import spacetrader.player.Player;

import java.util.ArrayList;

/**
 * This event will be triggered every time there is a new turn.
 * 
 * @author Alex
 */
public class TurnEvent {
    /**
     * ArrayList of listeners.
     */
    private static ArrayList<TurnListener> listeners = new ArrayList<TurnListener>();
    
    /**
     * Turn the player is on.
     */
    private static int turn = 0;
    
    /**
     * Register the provided listener with the TurnEvent.
     * 
     * @param listener listener to register
     */
    public static void registerListener(TurnListener listener) {
        listeners = new ArrayList<TurnListener>();
        listeners.add(listener);
    }
    
    /**
     * Calls handleNextTurn() for all TurnListeners registered.
     * @param player the player
     */
    public static void nextTurn(Player player) {
        turn++;
        for (TurnListener listener : listeners) {
            listener.handleNextTurn(player);
        }
    }
    
    /**
     * get the current game turn.
     * @return turn
     */
    public static int getTurn() {
        return turn;
    }
    
    /**
     * get a TurnSerializer object with the static turn event data.
     * @return new TurnSerializer
     */
    public static TurnSerializer getSerializer() {
        return new TurnSerializer(listeners, turn);
    }
    
    /**
     * load the TurnEvent class with the serialized data.
     * 
     * @param serialized data to load
     */
    public static void load(TurnSerializer serialized) {
        listeners = serialized.getListeners();
        turn = serialized.getTurn();
    }
}
