/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import spacetrader.encounter.Encounter;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = SpaceTrader.getInstance().getPlayer();
        other = new Encounter(player.getCurrentSolarSystem(), player);
        logText.setText(other.getGreeting());
    }
    
    
}
