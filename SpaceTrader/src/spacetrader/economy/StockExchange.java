package spacetrader.economy;

import java.util.ArrayList;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.Planet;
import spacetrader.player.Player;

/**
 *
 * @author Gaby
 */
public class StockExchange {
    
    /**
     * The SolarSystem this StockExchange is on.
     */
    private SolarSystem solarSystem;
    /**
     * The Planet this StockExchange is on.
     */
    private Planet planet;
    /**
     * The list of Companies this StockExchange can sell the stock for.
     */
    private ArrayList<Company> companies;
    /**
     * The array of all of the Companies in this instance of the game.
     */
    private Company[] company;
    
    /**
     * Constructor for StockExchange.
     * 
     * @param solarSystem2 the SolarSystem this StockExchange will be on.
     * @param company2 the array of all of the Companies in this instance of the game.
     */
    public StockExchange (SolarSystem solarSystem2, Company[] company2) {
        this.solarSystem = solarSystem2;
        this.company = company2.clone();
        companies = new ArrayList<Company>();
    }
    
    /**
     * Set up the Companies that are supposed to be in this StockExchange.
     * 
     * @param planet2 the Planet this StockExchange is on.
     */
    public void setCompanies (Planet planet2) {
        this.planet = planet2;
        for (int count = 0; count < company.length; count++) {
            if (company[count].companyCheck(solarSystem, this.planet)) {
                companies.add(company[count]);
            }
        }
        for (int count = 0; count < companies.size(); count++) {
            companies.get(count).calcMeanRelativeWealth();
            companies.get(count).calcMeanTechLevel();
            companies.get(count).calcNetWorth();
        }
    }
    
    /**
     * 
     * @param p
     * @param c 
     */
    public void buyStock(Player p, Company c) {
        if(p.getMoney()>= c.netWorth()) {
            p.setMoney(p.getMoney() - (float)c.netWorth());
            c.addStock();
        }
    }
    
    /**
     * 
     * @param p
     * @param c 
     */
    public void sellStock(Player p, Company c) {
        if(c.getStockOwned() > 0) {
            p.setMoney(p.getMoney() + (float)c.netWorth());
            c.removeStock();
        }
    }
    
    /**
     * Returns the list of Companies affiliated with this StockExchange.
     * 
     * @return companies
     */
    public ArrayList<Company> getCompanies() {
        return companies;
    }
    
    
    
    
}
