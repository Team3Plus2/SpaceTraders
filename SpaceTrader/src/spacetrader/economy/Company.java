package spacetrader.economy;

import java.util.ArrayList;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.Planet;

/**
 *
 * @author Gaby
 */
public class Company {
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
    public Company (String name2, ArrayList<SolarSystem> solarSystems2, ArrayList<ArrayList<Planet>> planets2) {
        this.solarSystems = solarSystems2;
        this.planets = planets2;
        this.name = name2;
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
}
