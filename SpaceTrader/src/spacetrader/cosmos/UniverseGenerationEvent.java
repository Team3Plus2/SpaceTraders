package spacetrader.cosmos;

import spacetrader.cosmos.system.SolarSystem;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class UniverseGenerationEvent {
    
    private static ArrayList<UniverseGenerationListener> registeredListeners;
    
    public static void registerListener(UniverseGenerationListener listener) {
        if(registeredListeners == null) 
            registeredListeners = new ArrayList<>();
        registeredListeners.add(listener);
    }
    
    public static void universeGenerated(ArrayList<SolarSystem> systems) {
        UniverseGenerationEvent event = new UniverseGenerationEvent(systems);
        for(UniverseGenerationListener listener : registeredListeners) {
            listener.onGeneration(event);
        }
    }
    
    private ArrayList<SolarSystem> systemsAdded;
    
    public UniverseGenerationEvent(ArrayList<SolarSystem> systems) {
        systemsAdded = systems;
    }
    
    public ArrayList<SolarSystem> getSystems() {
        return systemsAdded;
    }
    
}
