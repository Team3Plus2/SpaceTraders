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

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class CharacterConfigurationScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void handleBackAction(ActionEvent event) {
        SpaceTrader.getInstance().goToWelcomeScreen();
    }
    
    @FXML
    private void handleBeginAction(ActionEvent event) {
        Player newPlayer = new Player("name", 0, 0, 0, 0, 0);
        SpaceTrader.getInstance().goToGame();
    }
    
}
