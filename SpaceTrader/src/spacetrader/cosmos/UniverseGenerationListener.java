package spacetrader.cosmos;

/**
 * Listener for UniverseGenerationEvents.
 * @author Alex
 */
public interface UniverseGenerationListener {
    
    /**
     * Handles generation events.
     * 
     * @param event the fired event
     */
    void onGeneration(UniverseGenerationEvent event);
    
}
