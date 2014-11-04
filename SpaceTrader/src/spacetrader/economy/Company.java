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
    
    private ArrayList<SolarSystem> solarSystems;
    private ArrayList<ArrayList<Planet>> planets;
    private String name;
    private double netWorth;
    private double meanTechLevel;
    private double meanRelativeWealth;
    private int totalSize;
    
    public Company (String name2, ArrayList<SolarSystem> solarSystems2, ArrayList<ArrayList<Planet>> planets2) {
        this.solarSystems = solarSystems2;
        this.planets = planets2;
        this.name = name2;
    }
    
    public void totalSize() {
        int total = 0;
        for (int count = 0; count < planets.size(); count++) {
            total = total + planets.get(count).size();
        }
        totalSize = total;
    }
    
    public boolean companyCheck(SolarSystem solarSystem, Planet planet) {
        if (solarSystems.contains(solarSystem)) {
            int index = solarSystems.indexOf(solarSystem);
            if (planets.get(index).contains(planet)) {
                return true;
            }
        }
        return false;
    }
            
    public void calcMeanTechLevel() {
        meanTechLevel = 0;
        for (int count = 0; count < solarSystems.size(); count++) {
            int techLevel;
            String currentLevel = solarSystems.get(count).TechLevel().getName();
            techLevel = TechLevel.getIndex(currentLevel);
            meanTechLevel = meanTechLevel + (techLevel * planets.get(count).size());
        }
        meanTechLevel = meanTechLevel / totalSize;
    }
    
    public void calcMeanRelativeWealth() {
        for (int count = 0; count < solarSystems.size(); count++) {
            for (int count1 = 0; count1 < planets.get(count).size(); count1++) {
                meanRelativeWealth = meanRelativeWealth + planets.get(count).get(count).Wealth();
            }
            meanRelativeWealth++;
            meanRelativeWealth = meanRelativeWealth / 2;
        }
        meanRelativeWealth = meanRelativeWealth / totalSize;
    }
    
    public void calcNetWorth() {
        netWorth = 3000 * (meanTechLevel + meanRelativeWealth);
    }
    
    public double netWorth() {
        return netWorth;
    }
    
    public ArrayList<ArrayList<Planet>> planets() {
        return planets;
    }
    
    public ArrayList<SolarSystem> solarSystems() {
        return solarSystems;
    }
}
