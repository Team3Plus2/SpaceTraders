/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.global.Utility;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Gadget;
import spacetrader.player.Player;
import spacetrader.player.Shield;
import spacetrader.player.ShipType;
import spacetrader.player.AbstractUpgrade;
import spacetrader.player.Weapon;

/**
 * FXML Controller class.
 *
 * @author KartikKini
 */
public class PlanetViewController implements Initializable {
    
    /**
     * Local reference of current planet.
     */
    private Planet curPlanet;
    
    /**
     * Local reference of player.
     */
    private Player player;
    
    /**
     * local reference of current system.
     */
    private SolarSystem curSystem;
    
    /**
     * FXML reference of the planet options pane.
     */
    @FXML
    private AnchorPane planetOptions;
    
    /**
     * FXML reference of the planet name label.
     */
    @FXML
    private Label planetName;
    
    /**
     * FXML reference of the resource label.
     */
    @FXML
    private Label resourceLabel;
    
    /**
     * FXML reference of the tech level label.
     */
    @FXML
    private Label techLevelLabel;

    /**
     * Initializes the controller class.
     * @param url URL to load this controller
     * @param rb needed for implementation
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        planetInventory.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
                selectBuyableItem();
            });
        
        playerInventory.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
                selectSellableItem();
            });
        
        availableShips.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
                selectShip();
            });
        
        availableUpgrades.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
                selectUpgrade();
            });
        
        player = SpaceTrader.getInstance().getPlayer();
        curSystem = player.getCurrentSolarSystem();
        curPlanet = player.getCurrentPlanet();
        planetName.setText(curPlanet.name());
        resourceLabel.setText(curPlanet.resources().getName());
        techLevelLabel.setText(curSystem.techLevel().getName());
        ObservableList<ShipType> list = FXCollections.observableArrayList(curPlanet.getShipyard().getListShipsAvailable());
        availableShips.setItems(list);
        ObservableList<AbstractUpgrade> upgradeList = FXCollections.observableArrayList(curPlanet.getShipyard().getListUpgradesAvailable());
        availableUpgrades.setItems(upgradeList);
        if (list.size() > 0) {
            buyShipDetails.setText(((ShipType) availableShips.getItems().get(0)).getInfo());
        }
    }
    /**
     * *************************************************
     * Start of Shipyard Screen functions *
     * **************************************************
     */
    
    /**
     * FXML reference of the shipyard UI.
     */
    @FXML
    private TabPane shipyardUI;
    
    /**
     * FMXL reference of the list of available ships.
     */
    @FXML
    private ListView availableShips;
    
    /**
     * FXML reference of the list of available upgrades.
     */
    @FXML
    private ListView availableUpgrades;
    
    /**
     * FXML reference of the buying ship details label.
     */
    @FXML
    private Label buyShipDetails;
    
    /**
     * FXML reference of the ship cost label.
     */
    @FXML
    private Label shipCost;
    
    /**
     * FXML reference of the label that shows your money.
     */
    @FXML
    private Label yourMoney1;
    
    /**
     * FXML reference of the upgrade details label.
     */
    @FXML
    private Label upgradeDetails;
    
    /**
     * FXML reference of the upgrade cost label.
     */
    @FXML
    private Label upgradeCost;
    
    /**
     * FXML reference of the buy upgrade button.
     */
    @FXML
    private Button buyUpgrade;
    
    /**
     * Handles showing the shipyard.
     */
    @FXML
    private void showShipyard() {
        shipyardUI.setVisible(true);
        planetOptions.setVisible(false);
    }
    
    /**
     * Handles hiding the shipyard.
     */
    @FXML
    private void hideShipyard() {
        shipyardUI.setVisible(false);
        planetOptions.setVisible(true);
    }
    
    /**
     * Handles buying ships.
     */
    @FXML
    private void handleBuyShip() {
        ShipType selected = (ShipType) (availableShips.getSelectionModel().getSelectedItem());
        if (selected != null) {
            curPlanet.getShipyard().buyShip(player, selected);
            ObservableList<ShipType> list = FXCollections.observableArrayList(curPlanet.getShipyard().getListShipsAvailable());
            availableShips.setItems(list);
            buyShipDetails.setText(selected.getInfo());
            yourMoney1.setText("Your Money: " + Utility.currencyFormat(player.getMoney()));
            setShipCostLabel(Utility.currencyFormat(selected.getPrice()));
        }
    }
    
    /**
     * Sets ship cost label.
     * @param price String of price
     */
    private void setShipCostLabel(String price) {
        shipCost.setText("Cost: " + price);
    }
    
    /**
     * Handles selecting ships in the list view.
     */
    private void selectShip() {
        ShipType selected = (ShipType) (availableShips.getSelectionModel().getSelectedItem());
        buyShipDetails.setText(selected.getInfo());
        yourMoney1.setText("Your Money: " + Utility.currencyFormat(player.getMoney()));
        setShipCostLabel(Utility.currencyFormat(selected.getPrice()));
    }
    
    /**
     * Handles upgrading ship.
     */
    @FXML
    private void handleUpgradeShip() {
        AbstractUpgrade selected = (AbstractUpgrade) (availableUpgrades.getSelectionModel().getSelectedItem());
        if (selected != null) {
            if (selected.getClassName().equals("Weapon")) {
                curPlanet.getShipyard().buyWeapon(player, (Weapon) (selected));
            } else if (selected.getClassName().equals("Shield")) {
                curPlanet.getShipyard().buyShield(player, (Shield) (selected));
            } else if (selected.getClassName().equals("Gadget")) {
                curPlanet.getShipyard().buyGadget(player, (Gadget) (selected));
            }
        }
        selectUpgrade();
    }
    
    @FXML
    private void buyFuel() {
        curPlanet.getShipyard().buyFuel(player, player.getShip().getMaxFuel() - player.getShip().getFuel());
        fuelAvailableLabel.setText(String.format("%.1f", player.getShip().getFuel()) + "/" + String.format("%.1f", player.getShip().getMaxFuel()));
        fuelTank.setProgress(player.getShip().getFuel()/player.getShip().getMaxFuel());
    }
    
    @FXML
    ProgressBar fuelTank;
    
    @FXML
    Label fuelAvailableLabel;
    
    @FXML
    private void handleTabChange() {
        fuelTank.setProgress(player.getShip().getFuel()/player.getShip().getMaxFuel());
        fuelAvailableLabel.setText(String.format("%.1f", player.getShip().getFuel()) + "/" + String.format("%.1f", player.getShip().getMaxFuel()));
    }
    
    /**
     * Handles selecting upgrades in the list view.
     */
    private void selectUpgrade() {
        AbstractUpgrade selected = (AbstractUpgrade) (availableUpgrades.getSelectionModel().getSelectedItem());
        if (selected != null) {
            if (selected.getClassName().equals("Weapon")) {
                int maxWeapons = player.getShip().getMaxWeapons();
                int weaponsFilled = player.getShip().getWeaponsFilled();
                upgradeDetails.setText("Weapon Slots: " + maxWeapons
                                       + "\nSlots Filled: " + weaponsFilled);
            } else if (selected.getClassName().equals("Shield")) {
                int maxShields = player.getShip().getMaxShields();
                int shieldsFilled = player.getShip().getShieldsFilled();
                upgradeDetails.setText("Shield Slots: " + maxShields
                                       + "\nSlots Filled: " + shieldsFilled);
            } else if (selected.getClassName().equals("Gadget")) {
                int maxGadgets = player.getShip().getMaxGadgets();
                int gadgetsFilled = player.getShip().getGadgetsFilled();
                upgradeDetails.setText("Gadget Slots: " + maxGadgets
                                       + "\nSlots Filled: " + gadgetsFilled);
            }
            upgradeDetails.setText(upgradeDetails.getText()
                               + "\n\n\n\nYour Money: " + Utility.currencyFormat(player.getMoney()));
            setShipCostLabel(Utility.currencyFormat(selected.getPrice()));
            upgradeCost.setText("Cost: " + Utility.currencyFormat(selected.getPrice()));
        }
    }
    
     /**
     * *************************************************
     * Start of Marketplace Screen functions *
     * **************************************************
     */
    
    /**
     * FXML reference of the planet inventory list view.
     */
    @FXML
    private ListView planetInventory;
    
    /**
     * FXMl reference of the player inventory list view.
     */
    @FXML
    private ListView playerInventory;
    
    /**
     * FXML reference of the back button.
     */
    @FXML
    private Button back1;
    
    /**
     * FXML reference of the buy button.
     */
    @FXML
    private Button buyButton;
    
    /**
     * FXMl reference of the back button.
     */
    @FXML
    private Button back2;
    
    /**
     * FXML reference of the sell button.
     */
    @FXML
    private Button sellButton;
    
    /**
     * FXML reference of the buy quantity text field.
     */
    @FXML
    private TextField buyQuantity;
    
    /**
     * FXML reference of the sell quantity text field.
     */
    @FXML
    private TextField sellQuantity;
    
    /**
     * FXML reference of the sell details label.
     */
    @FXML
    private Label sellDetails;
    
    /**
     * FXML reference of the buy details label.
     */
    @FXML
    private Label buyDetails;
    
    /**
     * FXML reference of the marketplace UI tabpane.
     */
    @FXML
    private TabPane marketplaceUI;
    
    /**
     * FXML reference of the planet marketplace label.
     */
    @FXML
    private Label planetMarketplaceLabel;
    
    /**
     * FXML reference of the buy sum label.
     */
    @FXML
    private Label buySum;

    /**
     * local reference of the market.
     */
    private MarketPlace market;
    
    /**
     * Local variable for the goods being bought.
     */
    private TradeGood buyableGood;
    
    /**
     * Local variable for the goods being sold.
     */
    private TradeGood sellableGood;

    /**
     * Handles generating buy list.
     */
    private void generateBuyList() {
        if (curPlanet != null) {
            ArrayList<TradeGood> goods = market.getListOfGoods();
            ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
            planetInventory.setItems(list);
        }
    }

    /**
     * Handles generating the sell list.
     */
    private void generateSellList() {
        ArrayList<TradeGood> goods = player.getShip().getCargo().getNonEmptyCargoList();
        for (TradeGood tg : goods) {
            tg.setPrice(market.priceOfGood(tg));
        }
        ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
        playerInventory.setItems(list);
    }
    
    /**
     * Handles pressing the back button.
     */
    @FXML
    private void backToSolarSystem() {
        SpaceTrader.getInstance().goToSolarSystemView();
    }

    /**
     * Handles selecting buyable item.
     */
    @FXML
    private void selectBuyableItem() {
        TradeGood selected = (TradeGood) (planetInventory.getSelectionModel().getSelectedItem());
        if (selected != null) {
            buyableGood = selected;
        }
        updateBuyableItem();
    }
    
    /**
     * Handles selecting sellable item.
     */
    @FXML
    private void selectSellableItem() {
        TradeGood selected = (TradeGood) (playerInventory.getSelectionModel().selectedItemProperty().get());
        if (selected != null) {
            sellableGood = selected;
        }
        updateSellableItem();
    }
    
    /**
     * Handles hiding the marketplace.
     */
    @FXML
    private void hideMarketplace() {
        marketplaceUI.setVisible(false);
        planetOptions.setVisible(true);
    }
    
    /**
     * Handles showing the marketplace.
     */
    @FXML
    private void openMarketplace() {
        if (curPlanet != null) {
            planetMarketplaceLabel.setText(curPlanet.name() + " Marketplace");
            planetMarketplaceLabel.setFont(Font.font(curPlanet.name().length()));
            market = curPlanet.getMarket();
            generateBuyList();
            generateSellList();
            marketplaceUI.setVisible(true);
            planetOptions.setVisible(false);
            generateBuyList();
        }
    }

    /**
     * updates the buyable item label.
     */
    private void updateBuyableItem() {
        try {
            buyDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                    + "\nCost: " + Utility.currencyFormat(buyableGood.getCurrentPriceEach()));
            buySum.setText("Sum: " + Utility.currencyFormat(player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText())));
//            if (player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText()) < 0) {
//                
//            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the sellable item label.
     */
    private void updateSellableItem() {
        try {
            if (sellableGood != null) {
                sellDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                        + "\nValue: " + Utility.currencyFormat(sellableGood.getCurrentPriceEach())
                        + "\n\n\n\nSum: " + Utility.currencyFormat(player.getMoney() + sellableGood.getCurrentPriceEach() * Integer.parseInt(sellQuantity.getText())));
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles pressing the buy button.
     */
    @FXML
    private void handleBuyAction() {
        if (buyableGood != null) {
            market.buy(player, buyableGood, Integer.parseInt(buyQuantity.getText()));
            generateBuyList();
            generateSellList();
            updateBuyableItem();
        }
    }

    /**
     * Handles pressing the sell button.
     */
    @FXML
    private void handleSellAction() {
        if (sellableGood != null) {
            TradeGood temp = new TradeGood(sellableGood);
            temp.setPrice(sellableGood.getCurrentPriceEach());
            market.sell(player, temp, Integer.parseInt(sellQuantity.getText()));
            generateSellList();
            generateBuyList();
            updateSellableItem();
        }
    }
    
}
