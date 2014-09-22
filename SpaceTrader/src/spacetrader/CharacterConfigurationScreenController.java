/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.cosmos.player.Player;
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
import javafx.scene.control.TextField;

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
    private Label skillPointsAvailableLabel;
    
    @FXML
    private TextField name;
    
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
        fighterLevelSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                handleProgressBarUpdate(new ActionEvent());
            }
        
        });
        traderLevelSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                handleProgressBarUpdate(new ActionEvent());
            }
        
        });
        engineerLevelSlider.valueProperty().addListener(new ChangeListener<Number>() {
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
        if (!(name.getText().equals("")) && skillPointsAvailable.getProgress() == 0) {
            Player newPlayer = new Player(name.getText(), 10f, (int) pilotLevelSlider.getValue(), (int) fighterLevelSlider.getValue(), (int) traderLevelSlider.getValue(), (int) engineerLevelSlider.getValue(), 0);
            SpaceTrader.getInstance().goToGame(newPlayer);
        } else {
            System.out.println("Please enter a name for your character or finish setting your skill points.");
        }
    }
    
    @FXML
    private void handleProgressBarUpdate(ActionEvent event) {
        double pilotSliderValue = (double) pilotLevelSlider.getValue();
        double fighterSliderValue = (double) fighterLevelSlider.getValue();
        double tradeSliderValue = (double) traderLevelSlider.getValue();
        double engineerSliderValue = (double) engineerLevelSlider.getValue();
//        if(skillPointsAvailable.getProgress() >= 0) {
        pilotLevelLabel.setText((int)pilotSliderValue + "");
        fighterLevelLabel.setText((int)fighterSliderValue + "");
        traderLevelLabel.setText((int)tradeSliderValue + "");
        engineerLevelLabel.setText((int)engineerSliderValue + "");
        if (15 - ((int)fighterSliderValue - (int)tradeSliderValue - (int)engineerSliderValue) >= 0) {
            pilotLevelSlider.setMax(15 - (int)fighterSliderValue - (int)tradeSliderValue - (int)engineerSliderValue);
        } else {
            pilotLevelSlider.setMax(0);
        }
        if (15 - ((int)pilotSliderValue - (int)tradeSliderValue - (int)engineerSliderValue) >= 0) {
            fighterLevelSlider.setMax(15 - (int)pilotSliderValue - (int)tradeSliderValue - (int)engineerSliderValue);
        } else {
            fighterLevelSlider.setMax(0);
        }
        if (15 - ((int)fighterSliderValue - (int)pilotSliderValue - (int)engineerSliderValue) >= 0) {
            traderLevelSlider.setMax(15 - (int)fighterSliderValue - (int)pilotSliderValue - (int)engineerSliderValue);
        } else {
            traderLevelSlider.setMax(0);
        }
        if (15 - ((int)fighterSliderValue - (int)tradeSliderValue - (int)pilotSliderValue) >= 0) {
            engineerLevelSlider.setMax(15 - (int)fighterSliderValue - (int)tradeSliderValue - (int)pilotSliderValue);
        } else {
            engineerLevelSlider.setMax(0);
        }
        skillPointsAvailableLabel.setText((15 - (int)pilotSliderValue - (int)fighterSliderValue - (int)tradeSliderValue - (int)engineerSliderValue) + "/15");
        skillPointsAvailable.setProgress(1 - (pilotSliderValue/15.0 + fighterSliderValue/15.0 + tradeSliderValue/15.0 + engineerSliderValue/15.0));
//        if (pilotSliderValue <  pilotLevelSlider.getValue() || fighterSliderValue < fighterLevelSlider.getValue() || tradeSliderValue < traderLevelSlider.getValue() || engineerSliderValue < engineerLevelSlider.getValue()) {
//            pilotLevelSlider.adjustValue(Math.floor(pilotSliderValue));
//            fighterLevelSlider.adjustValue(Math.floor(fighterSliderValue));
//            traderLevelSlider.adjustValue(Math.floor(tradeSliderValue));
//            engineerLevelSlider.adjustValue(Math.floor(engineerSliderValue));
//        }
    }
    
}
