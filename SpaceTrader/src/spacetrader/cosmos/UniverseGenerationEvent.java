package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class UniverseGenerationEvent {
    
    /**
     * List of registeredListeners for this Event.
     */
    private static ArrayList<UniverseGenerationListener> registeredListeners = new ArrayList<>();
    
    /**
     * Registers the given listener with this Event.
     * 
     * @param listener the listener we want to register.
     */
    public static void registerListener(UniverseGenerationListener listener) {
        registeredListeners.add(listener);
    }
    
    /**
     * Does what needs to be done when the Universe if generated.
     * 
     * @param systems the list of SolarSystems we need to handle events for.
     */
    public static void universeGenerated(ArrayList<SolarSystem> systems) {
        if (registeredListeners == null || systems.size() == 0) {
            return;
        }
        UniverseGenerationEvent event = new UniverseGenerationEvent(systems);
        for (UniverseGenerationListener listener : registeredListeners) {
            listener.onGeneration(event);
        }
    }
    
    /**
     * List of SolarSystems for this Event.
     */
    private ArrayList<SolarSystem> systemsAdded;
    
    /**
     * Constructor for UniverseGenerationEvent.
     * 
     * @param systems the list of SolarSystems for this Event.
     */
    public UniverseGenerationEvent(ArrayList<SolarSystem> systems) {
        systemsAdded = systems;
    }
    
    /**
     * Getter for systemsAdded.
     * 
     * @return systemsAdded
     */
    public ArrayList<SolarSystem> getSystems() {
        return systemsAdded;
    }
    
}
