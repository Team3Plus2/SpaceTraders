/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.view;

import spacetrader.main.SpaceTrader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author KartikKini
 */
public class WelcomeScreenController implements Initializable {
    
    /**
     * Handles what happens when the Start button is pressed
     */
    @FXML
    private void handleStartAction(ActionEvent event) {
        SpaceTrader.getInstance().goToLoadGame();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
