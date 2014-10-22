/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.Shipyard;
import spacetrader.economy.TradeGood;
import spacetrader.global.Utility;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;
import spacetrader.player.ShipType;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class PlanetViewController implements Initializable {
    
    private Planet curPlanet;
    private Player player;
    private SolarSystem curSystem;
    
    @FXML
    private AnchorPane planetOptions;
    
    @FXML
    private Label planetName;
    
    @FXML
    private Label resourceLabel;
    
    @FXML
    private Label techLevelLabel;

    /**
     * Initializes the controller class.
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
        
        player = SpaceTrader.getInstance().getPlayer();
        curSystem = player.getCurrentSolarSystem();
        curPlanet = player.getCurrentPlanet();
        planetName.setText(curPlanet.Name());
        resourceLabel.setText(curPlanet.Resources().getName());
        techLevelLabel.setText(curSystem.TechLevel().getName());
        ObservableList<ShipType> list = FXCollections.observableArrayList(curPlanet.getShipyard().getListAvailable());
        availableShips.setItems(list);
        if(list.size() > 0) {
            buyShipDetails.setText(((ShipType)availableShips.getItems().get(0)).getInfo());
        }
    }
    /**
     * *************************************************
     * Start of Shipyard Screen functions *
     * **************************************************
     */
    
    @FXML
    private TabPane shipyardUI;
    
    @FXML
    private ListView availableShips;
    
    @FXML
    private ListView availableUpgrades;
    
    @FXML
    private Label buyShipDetails;
    
    @FXML
    private Label shipCost;
    
    @FXML
    private Label yourMoney1;
    
    @FXML
    private void showShipyard() {
        shipyardUI.setVisible(true);
        planetOptions.setVisible(false);
    }
    
    @FXML
    private void hideShipyard() {
        shipyardUI.setVisible(false);
        planetOptions.setVisible(true);
    }
    
    private void selectShip() {
        ShipType selected = (ShipType) (availableShips.getSelectionModel().getSelectedItem());
        buyShipDetails.setText(selected.getInfo());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        shipCost.setText("Cost: " + nf.format(selected.getPrice()));
    }
    
     /**
     * *************************************************
     * Start of Marketplace Screen functions *
     * **************************************************
     */
    @FXML
    private ListView planetInventory;
    @FXML
    private ListView playerInventory;
    @FXML
    private Button back1;
    @FXML
    private Button buyButton;
    @FXML
    private Button back2;
    @FXML
    private Button sellButton;
    @FXML
    private TextField buyQuantity;
    @FXML
    private TextField sellQuantity;
    @FXML
    private Label sellDetails;
    @FXML
    private Label buyDetails;
    @FXML
    private TabPane marketplaceUI;
    @FXML
    private Label planetMarketplaceLabel;
    @FXML
    private Label buySum;

    private MarketPlace market;
    private TradeGood buyableGood, sellableGood;

    private void generateBuyList() {
        if (curPlanet != null) {
            ArrayList<TradeGood> goods = market.getListOfGoods();
            ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
            planetInventory.setItems(list);
        }
    }

    private void generateSellList() {
        ArrayList<TradeGood> goods = player.getShip().getCargo().getNonEmptyCargoList();
        for (TradeGood tg : goods) {
            tg.setPrice(market.priceOfGood(tg));
        }
        ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
        playerInventory.setItems(list);
    }
    
    @FXML
    private void backToSolarSystem() {
        SpaceTrader.getInstance().goToSolarSystemView();
    }

    @FXML
    private void selectBuyableItem() {
        TradeGood selected = (TradeGood) (planetInventory.getSelectionModel().getSelectedItem());
        if (selected != null) {
            buyableGood = selected;
        }
        updateBuyableItem();
    }

    @FXML
    private void selectSellableItem() {
        TradeGood selected = (TradeGood) (playerInventory.getSelectionModel().selectedItemProperty().get());
        if (selected != null) {
            sellableGood = selected;
        }
        updateSellableItem();
    }
    
    @FXML
    private void hideMarketplace() {
        marketplaceUI.setVisible(false);
        planetOptions.setVisible(true);
    }
    
    @FXML
    private void openMarketplace() {
        if (curPlanet != null) {
            planetMarketplaceLabel.setText(curPlanet.Name() + " Marketplace");
            planetMarketplaceLabel.setFont(Font.font(curPlanet.Name().length()));
            market = curPlanet.getMarket();
            generateBuyList();
            generateSellList();
            marketplaceUI.setVisible(true);
            planetOptions.setVisible(false);
            generateBuyList();
        }
    }

    private void updateBuyableItem() {
        try {
            buyDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                    + "\nCost: " + Utility.currencyFormat(buyableGood.getCurrentPriceEach()));
            buySum.setText("Sum: " + Utility.currencyFormat(player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText())));
            if (player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText()) < 0) {
                
            }
        } catch (Exception e) {

        }
    }

    private void updateSellableItem() {
        try {
            if (sellableGood != null) {
                sellDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                        + "\nValue: " + Utility.currencyFormat(sellableGood.getCurrentPriceEach())
                        + "\n\n\n\nSum: " + Utility.currencyFormat(player.getMoney() + sellableGood.getCurrentPriceEach() * Integer.parseInt(sellQuantity.getText())));
            }
        } catch (Exception e) {

        }
    }

    @FXML
    private void handleBuyAction() {
        if (buyableGood != null) {
            boolean success = market.buy(player, buyableGood, Integer.parseInt(buyQuantity.getText()));
            generateBuyList();
            generateSellList();
            updateBuyableItem();
        }
    }

    @FXML
    private void handleSellAction() {
        if (sellableGood != null) {
            TradeGood temp = new TradeGood(sellableGood);
            temp.setPrice(sellableGood.getCurrentPriceEach());
            boolean success = market.sell(player, temp, Integer.parseInt(sellQuantity.getText()));
            generateSellList();
            generateBuyList();
            updateSellableItem();
        }
    }
    
}
