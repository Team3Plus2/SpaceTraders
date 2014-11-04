package spacetrader.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.encounter.Encounter;
import spacetrader.global.Utility;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 * FXML Controller class.
 *
 * @author KartikKini
 */
public class EncounterController implements Initializable {
    
    /**
     * The player having the encounter.
     */
    private Player player;
    
    /**
     * The encounter encountered.
     */
    private Encounter other;
    
    /**
     * Any exchange between the player and encounter.
     */
    private MarketPlace market;

    /**
     * The current state of the encounter.
     */
    private int state; //0-standard, 1-attack, 2-enemy surrender, 3-player is destroyed, 4-leave state, 5-search request
    
    
    /**
     * The log text on the screen.
     */
    @FXML
    private Label logText;
    
    /**
     * The other ship's details on the screen.
     */
    @FXML
    private Label otherShipDetails;
    
    /**
     * The player's ship details on the screen.
     */
    @FXML
    private Label yourShipDetails;
    
    /**
     * The top button.
     */
    @FXML
    private Button button1;
    
    /**
     * The middle button.
     */
    @FXML
    private Button button2;
    
    /**
     * The bottom button.
     */
    @FXML
    private Button button3;
    
    /**
     * The interface for the market.
     */
    @FXML
    private TabPane marketplaceUI;
    
    /**
     * The interface for the encounter itself.
     */
    @FXML
    private AnchorPane encounterUI;

    /**
     * Initializes the controller class.
     * @param url the url of the controller
     * @param rb the resource bundle for the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = SpaceTrader.getInstance().getPlayer();
        other = new Encounter(player.getCurrentSolarSystem(), player);
        
        yourShipDetails.setText(("Ship: " + player.getShip().getName()));
        otherShipDetails.setText(("Ship: " + other.getShip().getName()));
        
        logText.setText(other.getGreeting());
        if (other.willAttack()) {
            attack();
        } else if (other.willRequestSearch()) {
            log("We are the local police force in this sector, please allow us to search your ship for illegal goods");
            button1.setText("Okay");
            button2.setText("Never!");
            button3.setText("N/A");
            button3.setVisible(false);
            state = 5;
        } else {
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
    
    /**
     * Handle press of button 1.
     */
    @FXML
    private void handle1() {
        if (state == 3) {
            destroyShip();
        } else if (state == 4) {
            leaveEncounter();
        } else if (state == 5) {
            search();
        } else {
            attack();
        }
    }
    
    /**
     * handle press of button 2.
     */
    @FXML
    private void handle2() {
        if (state == 1) {
            other.loot(player);
            log("The enemy looted your ship");
            leaveInterface();
        } else if (state == 2) {
            loot();
        } else if (state == 5) {
            log("Very well, then we will search by force!");
            button1.setText("Attack");
            button2.setText("Surrender");
            button3.setText("N/A");
            button3.setVisible(false);
            state = 1;
        } else {
            if (other.willingToTrade()) {
                trade();
            }
        }
    }
    
    /**
     * handle press of button 3.
     */
    @FXML
    private void handle3() {
        leaveEncounter();
    }
    
    /**
     * write item as a new line to the ui log.
     * @param item item to write
     */
    public void log(String item) {
        String current = logText.getText();
        current += "\n" + item;
        String[] tempstrings = current.split("\n");
        if (tempstrings.length > 8) {
            tempstrings = Arrays.copyOfRange(tempstrings, 1, tempstrings.length);
            current = String.join("\n", tempstrings);
        }
        logText.setText(current);
    }
    
    /**
     * Clear all current text from the log.
     */
    public void clearLog() {
        logText.setText("");
    }
    
    /**
     * sends the player on his way.
     */
    private void leaveEncounter() {
        SpaceTrader.getInstance().goToSolarSystemView();
    }
    
    /**
     * setup buttons and state to give user leave button.
     */
    private void leaveInterface() {
        button1.setText("Okay");
        button2.setVisible(false);
        button3.setVisible(false);
        state = 4;
    }
    
    /***************************************************************************
     * Attacking interface
     **************************************************************************/
    
    /**
     * executes a round of combat.
     */
    public void attack() {
        if (state != 1) {
            state = 1;
            button1.setText("Attack");
            button2.setText("Surrender");
            button3.setText("N/A");
            button3.setVisible(false);

            log("Entered Combat");
        } else {
            button1.setText("Continue");
        }
        
        int result = other.roundOfCombat(player, null);
        log("Player dealt damage: " + player.getDamageDealt());
        if (other.getDestroyed() != null) {
            for (Object o : other.getDestroyed()) {
                log("Item destroyed: " + o.toString());
            }
        }
        log("Top shield has sustained " + other.getDamageToShields() + "damage");
        
        log("Enemy dealt damage: " + other.getDamageDealt());
        if (other.getDestroyed() != null) {
            for (Object o : player.getDestroyed()) {
                log("Item destroyed: " + o.toString());
            }
        }
        log("Top shield has sustained " + player.getDamageToShields() + "damage");
        
        if (result == -1) { //enemy is destroyed
            log("");
            log("Enemy destroyed");
            salvage();
        } else if (result == -2) { //enemy surrenders
            log("Enemy offers surrender");
            button1.setText("Destroy");
            button2.setText("Accept");
            state = 2;
        } else if (result == 1) { //player ship is destroyed
            log("");
            log("Your ship is destroyed");
            button1.setText("Awww...");
            button2.setText("N/A");
            button2.setVisible(false);
            state = 3;
        }
    }
    
    /**
     * destroy the player's ship.
     */
    private void destroyShip() {
        SpaceTrader.getInstance().goToWelcomeScreen();
    }
    

    
    /***************************************************************************
     * Searching interface
     **************************************************************************/
    
    /**
     * Have encounter search the player for illegal goods.
     */
    private void search() {
        if (other.search(player)) {
            log("We have confiscated some illegal goods from your cargo hold");
        } else {
            log("Nothing illegal in your hold, sorry to bother you");
        }
        leaveInterface();
    }
    
    /***************************************************************************
     * Trading/Looting/Salvaging interface
     **************************************************************************/
    
    /**
     * sets up and displays the salvage interface.
     */
    private void salvage() {
        planetMarketplaceLabel.setText("Salvage from the enemy wreckage");
        market = other.getSalvageExchange();
        openMarketplace();
    }
    
    /**
     * sets up and displays the looting interface.
     */
    private void loot() {
        planetMarketplaceLabel.setText("Loot the enemy ship");
        market = other.getLootingExchange();
        openMarketplace();
    }
    
    /**
     * sets up and displays the looting interface.
     */
    private void trade() {
        planetMarketplaceLabel.setText("Trader Marketplace");
        market = other.getMarketPlace(player.getCurrentSolarSystem());
        openMarketplace();
    }
    
    /**
     * Hide the marketplace ui.
     */
    @FXML
    private void hideMarketplace() {
        marketplaceUI.setVisible(false);
        encounterUI.setVisible(true);
    }
    
    /**
     * The good being sold.
     */
    TradeGood sellableGood;
    
    /**
     * The textbox of the quantity of goods to sell/buy.
     */
    @FXML
    private TextField sellQuantity;
    
    /**
     * handle player selling goods.
     */
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
    
    /**
     * the ui element for the encounter's inventory.
     */
    @FXML
    private ListView planetInventory;
    
    /**
     * the ui element for the player's inventory.
     */
    @FXML
    private ListView playerInventory;
    
    /**
     * generate the list of things that can be bought.
     */
    private void generateBuyList() {
        ArrayList<TradeGood> goods = market.getListOfGoods();
        ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
        planetInventory.setItems(list);
    }

    /**
     * generate the list of things that can be sold.
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
     * the label for the encounter market.
     */
    @FXML
    private Label planetMarketplaceLabel;
    
    /**
     * open the marketplace ui.
     */
    @FXML
    private void openMarketplace() {
        generateBuyList();
        generateSellList();
        marketplaceUI.setVisible(true);
        encounterUI.setVisible(false);
        generateBuyList();
    }
    
    /**
     * The details of the current buy transaction in label.
     */
    @FXML
    private Label buyDetails;
    
    /**
     * The label for the sum of things to buy.
     */
    @FXML
    private Label buySum;
    
    /**
     * The current good being considered for buying.
     */
    private TradeGood buyableGood;
    
    /**
     * the field displaying the buy quantity.
     */
    @FXML
    private TextField buyQuantity;

    /**
     * update the ui with with the selected buyable item.
     */
    private void updateBuyableItem() {
        buyDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                + "\nCost: " + Utility.currencyFormat(buyableGood.getCurrentPriceEach()));
        buySum.setText("Sum: " + Utility.currencyFormat(player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText())));
        if (player.getMoney() - buyableGood.getCurrentPriceEach() * Integer.parseInt(buyQuantity.getText()) < 0) {

        }
    }
    
    /**
     * the sell details.
     */
    @FXML
    private Label sellDetails;

    /**
     * update the ui with with the selected selling item.
     */
    private void updateSellableItem() {
        if (sellableGood != null) {
            sellDetails.setText("Cash: " + Utility.currencyFormat(player.getMoney())
                    + "\nValue: " + Utility.currencyFormat(sellableGood.getCurrentPriceEach())
                    + "\n\n\n\nSum: " + Utility.currencyFormat(player.getMoney() + sellableGood.getCurrentPriceEach() * Integer.parseInt(sellQuantity.getText())));
        }
    }

    /**
     * handle player buying goods.
     */
    @FXML
    private void handleBuyAction() {
        if (buyableGood != null) {
            boolean success = market.buy(player, buyableGood, Integer.parseInt(buyQuantity.getText()));
            generateBuyList();
            generateSellList();
            updateBuyableItem();
        }
    }
    
    /**
     * handle selecting of a new buyable item.
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
     * handle selecting of a new item to sell.
     */
    @FXML
    private void selectSellableItem() {
        TradeGood selected = (TradeGood) (playerInventory.getSelectionModel().selectedItemProperty().get());
        if (selected != null) {
            sellableGood = selected;
        }
        updateSellableItem();
    }
    
    
}
