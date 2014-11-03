package spacetrader.economy;

import java.util.ArrayList;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.Planet;

/**
 *
 * @author Gaby
 */
public class StockExchange {
    
    private SolarSystem solarSystem;
    private Planet planet;
    private ArrayList<Company> companies;
    private Company[] company;
    
    public StockExchange (SolarSystem solarSystem2, Company[] company2) {
        this.solarSystem = solarSystem2;
        this.company = company2;
    }
    
    public void setCompanies (Planet planet2) {
        this.planet = planet2;
        for (int count = 0; count < company.length; count++) {
            if (company[count].companyCheck(solarSystem, this.planet)) {
                companies.add(company[count]);
            }
        }
    }
    
    public ArrayList<Company> getCompanies() {
        return companies;
    }
    
    
    
    
}
