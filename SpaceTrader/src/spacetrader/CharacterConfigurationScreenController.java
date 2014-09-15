/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class CharacterConfigurationScreenController implements Initializable {

    @FXML
    private Slider pilotLevelSlider;
    
    @FXML
    private Slider fighterLevelSlider;
    
    @FXML
    private Slider traderLevelSlider;
    
    @FXML
    private Slider engineerLevelSlider;
    
    @FXML
    private ProgressBar skillPointsAvailable;
    
    @FXML
    private Label pilotLevelLabel, fighterLevelLabel, traderLevelLabel, engineerLevelLabel;
    
    @FXML
    private Label progressBarLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pilotLevelSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                handleProgressBarUpdate(new ActionEvent());
            }
        
        });
    }
    
    @FXML
    private void handleBackAction(ActionEvent event) {
        SpaceTrader.getInstance().goToWelcomeScreen();
    }
    
    @FXML
    private void handleBeginAction(ActionEvent event) {
        Player newPlayer = new Player("name", (int) pilotLevelSlider.getValue(), (int) fighterLevelSlider.getValue(), (int) traderLevelSlider.getValue(), (int) engineerLevelSlider.getValue(), 0);
        SpaceTrader.getInstance().goToGame();
    }
    
    @FXML
    private void handleProgressBarUpdate(ActionEvent event) {
        skillPointsAvailable.setProgress(1 - (pilotLevelSlider.getValue()/15.0 + fighterLevelSlider.getValue()/15.0 + traderLevelSlider.getValue()/15.0 + engineerLevelSlider.getValue()/15.0));
    }
    
}
