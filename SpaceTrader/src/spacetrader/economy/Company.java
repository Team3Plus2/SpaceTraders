package spacetrader.economy;

import java.util.ArrayList;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.UniverseGenerationListener;
import spacetrader.cosmos.UniverseGenerationEvent;
import java.util.Random;
import java.io.Serializable;

/**
 *
 * @author Gaby
 */
public class Company implements UniverseGenerationListener, Serializable {
    static final long serialVersionUID = (long) 4006L;
    
    /**
     * List of SolarSystems this Company is affiliated with.
     */
    private ArrayList<SolarSystem> solarSystems;
    /**
     * List of Planets this Company is affiliated with.
     */
    private ArrayList<ArrayList<Planet>> planets;
    /**
     * Name of this Company.
     */
    private String name;
    /**
     * 
     */
    private int stockOwned;
    /**
     * NetWorth of this Company.
     */
    private double netWorth;
    /**
     * MeanTechLevel of this Company.
     */
    private double meanTechLevel;
    /**
     * MeanRelativeWealth of this Company.
     */
    private double meanRelativeWealth;
    /**
     * TotalSize of this Company.
     */
    private int totalSize;
    
    /**
     * Constructor for Company.
     * 
     * @param name2 the name of this Company
     * @param solarSystems2 the list of SolarSystems this Company will be affiliated with.
     * @param planets2 the list of Planets this Company will be affiliated with.
     */
    public Company (String name2) {
        this.name = name2;
        solarSystems = new ArrayList<SolarSystem>();
        planets = new ArrayList<ArrayList<Planet>>();
        stockOwned = 0;
    }
    
    public void onGeneration(UniverseGenerationEvent event) {
        ArrayList<SolarSystem> existingSystems = event.getSystems();
        for (int count = 0; count < existingSystems.size(); count++) {
            Random rand = new Random();
            int decider = rand.nextInt() * (2 ^ existingSystems.get(count).planets().length);
            if (decider != 0) {
                solarSystems.add(existingSystems.get(count));
                planets.add(new ArrayList<Planet>());
                Planet[] planets1 = existingSystems.get(count).planets();
                for (int count1 = 0; count1 < existingSystems.get(count).planets().length; count1++ ) {
                    if (decider%2 == 1) {
                        planets.get(solarSystems.size() - 1).add(planets1[count1]);
                    }
                    decider = decider >> 1;
                }
            }
        }
    }
    
    /**
     * Calculate the totalSize of this Company.
     */
    public void totalSize() {
        int total = 0;
        for (int count = 0; count < planets.size(); count++) {
            total = total + planets.get(count).size();
        }
        totalSize = total;
    }
    
    /**
     * 
     */
    public void addStock() {
        stockOwned++;
    }
    
    /**
     * 
     */
    public void removeStock() {
        stockOwned--;
    }
    
    /**
     * 
     * @return 
     */
    public int getStockOwned() {
        return stockOwned;
    }
    
    /**
     * Checks if this Company is affiliated with the given SolarSystem and Planet.
     * 
     * @param solarSystem the SolarSystem we are checking the affiliation of
     * @param planet the Planet we are checking the affiliation of
     * @return true if this Company is affiliated with the given SolarSystem and Planet.
     */
    public boolean companyCheck(SolarSystem solarSystem, Planet planet) {
        if (solarSystems.contains(solarSystem)) {
            int index = solarSystems.indexOf(solarSystem);
            if (planets.get(index).contains(planet)) {
                return true;
            }
        }
        return false;
    }
            
    /**
     * Calculates the meanTechLevel of this Company.
     */
    public void calcMeanTechLevel() {
        meanTechLevel = 0;
        for (int count = 0; count < solarSystems.size(); count++) {
            int techLevel;
            String currentLevel = solarSystems.get(count).techLevel().getName();
            techLevel = TechLevel.getIndex(currentLevel);
            meanTechLevel = meanTechLevel + (techLevel * planets.get(count).size());
        }
        meanTechLevel = meanTechLevel / totalSize;
    }
    
    /**
     * Calculates the meanRelativeWealth of this Company.
     */
    public void calcMeanRelativeWealth() {
        for (int count = 0; count < solarSystems.size(); count++) {
            for (int count1 = 0; count1 < planets.get(count).size(); count1++) {
                meanRelativeWealth = meanRelativeWealth + planets.get(count).get(count).wealth();
            }
            meanRelativeWealth++;
            meanRelativeWealth = meanRelativeWealth / 2;
        }
        meanRelativeWealth = meanRelativeWealth / totalSize;
    }
    
    /**
     * Calculates the netWorth of this Company.
     */
    public void calcNetWorth() {
        netWorth = 3000 * (meanTechLevel + meanRelativeWealth);
    }
    
    /**
     * Returns the netWorth of this Company.
     * 
     * @return netWorth
     */
    public double netWorth() {
        return netWorth;
    }
    
    /**
     * Returns the list of Planets affiliated with this Company.
     * 
     * @return planets
     */
    public ArrayList<ArrayList<Planet>> planets() {
        return planets;
    }
    
    /**
     * Returns the list of SolarSystems affiliated with this Company.
     * 
     * @return solarSystems
     */
    public ArrayList<SolarSystem> solarSystems() {
        return solarSystems;
    }

    /**
     * Returns name of company.
     * @return String name of company
     */
    public String getName() {
        return name;
    }
    
    
}
