/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

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
    
    @FXML
    private Label label;
    
    /**
     * Handles what happens when the Start button is pressed
     */
    @FXML
    private void handleStartAction(ActionEvent event) {
        SpaceTrader.getInstance().goToCharacterConfig();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
