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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.encounter.Encounter;
import spacetrader.global.Utility;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class EncounterController implements Initializable {
    
    private Player player;
    private Encounter other;
    
    @FXML
    private Label logText, otherShipDetails, yourShipDetails;
    
    @FXML
    private Button button1, button2, button3;
    
    @FXML
    private TabPane marketplaceUI;
    
    @FXML
    private AnchorPane encounterUI;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = SpaceTrader.getInstance().getPlayer();
        other = new Encounter(player.getCurrentSolarSystem(), player);
        logText.setText(other.getGreeting());
        if(other.willAttack()) {
            button1.setText("Attack");
            button2.setText("Surrender");
            button3.setText("N/A");
            button3.setVisible(false);
        } else if (other.willRequestTrade() || other.willingToTrade()) {
            button1.setText("Attack");
            button2.setText("Trade");
            button3.setText("Leave");
            button3.setVisible(true);
        }
        
        planetInventory.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
            selectBuyableItem();
        });
        
        playerInventory.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
            selectSellableItem();
        });
    }
    
    @FXML
    private void handle1() {
        
    }
    
    @FXML
    private void handle2() {
        if(other.willRequestTrade()) {
            openMarketplace();
        }
    }
    
    @FXML
    private void handle3() {
        if(other.willRequestTrade()) {
            SpaceTrader.getInstance().goToSolarSystemView();
        }
    }
    
    @FXML
    private void hideMarketplace() {
        marketplaceUI.setVisible(false);
        encounterUI.setVisible(true);
    }
    
    TradeGood sellableGood;
    
    @FXML
    private TextField sellQuantity;
    
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
    
    @FXML
    private ListView planetInventory;
    
    @FXML
    private ListView playerInventory;
    
    private void generateBuyList() {
            ArrayList<TradeGood> goods = market.getListOfGoods();
            ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
            planetInventory.setItems(list);
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
    private Label planetMarketplaceLabel;
    
    private MarketPlace market;
    
    @FXML
    private void openMarketplace() {
        planetMarketplaceLabel.setText("Trader Marketplace");
        market = other.getMarketPlace();
        generateBuyList();
        generateSellList();
        marketplaceUI.setVisible(true);
        encounterUI.setVisible(false);
        generateBuyList();
    }
    
    @FXML
    private Label buyDetails;
    
    @FXML
    private Label buySum;
    
    private TradeGood buyableGood;
    
    @FXML
    private TextField buyQuantity;

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
    
    @FXML
    private Label sellDetails;

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
    
    
}
