/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.view;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.economy.MarketPlace;
import spacetrader.economy.TradeGood;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class SolarSystemViewController implements Initializable {
    
    private GraphicsContext g;
    private GraphicsContext flareG;
    private GraphicsContext selectionG;
    
    private Image starImage;
    private Image flareImage;
    
    private SolarSystem curSystem;
    private Planet curPlanet;
    private Player player;
    
    private Timer timer;
    
    @FXML
    private Slider zoomSlider;
    @FXML
    private Pane starBackdrop;
    @FXML
    private Canvas flareLayer;
    @FXML
    private Canvas viewCanvas;
    @FXML
    private Canvas selectionLayer;
    @FXML
    private Button backToStarScreen;
    
    private double zoom = 1;
    private double preDragX;
    private double preDragY;
    private boolean dragging;
    private double dragOffsetX;
    private double dragOffsetY;
    private double mapOffsetX;
    private double mapOffsetY;
    private boolean dragFinished;
    
    private HashMap<Point, Planet> screenSpace;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize variables
        player = SpaceTrader.getInstance().getPlayer();
        curSystem = player.getCurrentSolarSystem();
        g = viewCanvas.getGraphicsContext2D();
        flareG = flareLayer.getGraphicsContext2D();
        selectionG = selectionLayer.getGraphicsContext2D();
        starImage = new Image("/visuals/Stars/Sol_big.png");
        flareImage = new Image("/visuals/Stars/SolFlareSheet.png");
        Random r = new Random();
        screenSpace = new HashMap<>();
        
        checkRadii();
        
        updateTimer();
        
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue();
                draw();
            }
        
        });
        
        //draw initial view
        draw();
        
        //draw initial flare
        double size = (starImage.getWidth() / 2);
        double posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        double posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.setGlobalBlendMode(BlendMode.ADD);
        drawFlares(size, posx, posy);
    }
    
    private void checkRadii() {
        //ensure no planets are too close to one another
        if(curSystem.Planets().length > 1) {
            for(int i = 0; i < curSystem.Planets().length; i++) {
                for(int j = i + 1; j < curSystem.Planets().length; j++) {
                    int p1 = curSystem.Planets()[i].getLocation().y;
                    int p2 = curSystem.Planets()[j].getLocation().y;
                    while (Math.abs(p1 - p2) < 30) {
                        curSystem.Planets()[j].setLocation(new Point(curSystem.Planets()[j].getLocation().x, p2 + 1));
                        p1 = curSystem.Planets()[i].getLocation().y;
                        p2 = curSystem.Planets()[j].getLocation().y;
                        i = 0;
                    }
                }
            }
        }
    }
    
    private void updateTimer() {
        //update orbits
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    for(Planet p : curSystem.Planets()) {
                        if(p.getLocation().x + 1 > 144000) {
                            p.setLocation(new Point(0, p.getLocation().y));
                        }
                        p.setLocation(new Point(p.getLocation().x + 1, p.getLocation().y));
                    }
                    screenSpace.clear();
                    draw();
                });
            }
        }, 0, 100);
    }
    
    private void updateCanvasScales() {
        starBackdrop.setScaleX(2 + (zoom / 30));
        starBackdrop.setScaleY(2 + (zoom / 30));
        starBackdrop.setTranslateX(-(dragOffsetX + mapOffsetX)/4);
        starBackdrop.setTranslateY(-(dragOffsetY + mapOffsetY)/4);
        selectionLayer.setScaleX(1 + (zoom / 10));
        selectionLayer.setScaleY(1 + (zoom / 10));
        viewCanvas.setScaleX(1 + (zoom / 10));
        viewCanvas.setScaleY(1 + (zoom / 10));
        flareLayer.setScaleX(1 + (zoom / 20));
        flareLayer.setScaleY(1 + (zoom / 20));
    }
    
    /**
     * draws the solar system
     */
    private void draw() {
        g.clearRect(0, 0, 1024, 576);
        updateCanvasScales();
        double size = (starImage.getWidth() / 2);
        double posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        double posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        g.drawImage(starImage, posx, posy, size, size);
        if(dragging){
            flareG.setGlobalBlendMode(BlendMode.SRC_OVER);
            flareG.clearRect(0, 0, 1024, 576);
            flareG.setGlobalBlendMode(BlendMode.ADD);
            drawFlares(size, posx, posy);
        }
        g.setStroke(Color.WHITE);
        for(Planet p : curSystem.Planets()) {
            posx = getCartesianLocation(p.getLocation(), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX;
            posy = getCartesianLocation(p.getLocation(), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY;
            for(int i = 0; i < 90; i++) {
                g.setFill(Color.gray(Math.max(Math.min(Math.abs(Math.sin(i/50.0)), 0.8), 0.5)));
                g.fillOval(getCartesianLocation(new Point(i*14, p.getLocation().y), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX - 1, getCartesianLocation(new Point(i*14, p.getLocation().y), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY - 1, 2, 2);
                g.fillOval(getCartesianLocation(new Point((i + 90)*14, p.getLocation().y), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX - 1, getCartesianLocation(new Point((i + 90)*14, p.getLocation().y), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY - 1, 2, 2);
            }
            g.setFill(Color.WHITE);
            g.fillOval(posx - 5, posy - 5, 10, 10);
            screenSpace.put(new Point((int)(posx - 5), (int)(posy - 5)), p);
        }
    }
    
     /**
     * draws the solar system
     */
    private void drawSelection(Point mouse, Planet selection) {
        selectionG.clearRect(0, 0, 1024, 576);
        selectionG.setStroke(Color.WHITE);
        selectionG.setFill(Color.WHITE);
        selectionG.strokeOval(mouse.x - 15, mouse.y - 15, 30, 30);
        selectionG.fillText(selection.Name(), mouse.getX(), mouse.getY());
    }
    
    private void drawFlares(double size, double posx, double posy) {
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 4), posy + ((dragOffsetY + mapOffsetY) * 4), size, size);
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 2), posy + ((dragOffsetY + mapOffsetY) * 2), size, size);
        size *= 4;
        posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, 0, 0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx, posy, size, size);
        size /= 3;
        posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 3.5), posy + ((dragOffsetY + mapOffsetY) * 3.5), size, size);
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 2.5), posy + ((dragOffsetY + mapOffsetY) * 2.5), size, size);
        size /= 4;
        posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 3), posy + ((dragOffsetY + mapOffsetY) * 3), size, size);
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 1.5), posy + ((dragOffsetY + mapOffsetY) * 1.5), size, size);
        size /= 4;
        posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 1.75), posy + ((dragOffsetY + mapOffsetY) * 1.75), size, size);
        size /= 4;
        posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx + ((dragOffsetX + mapOffsetX) * 3.75), posy + ((dragOffsetY + mapOffsetY) * 3.75), size, size);
        size *= 200;
        posx = 512 - (size / 2) - ((dragOffsetX + mapOffsetX)*4);
        posy = 288 - (size / 2) - ((dragOffsetY + mapOffsetY)*4);
        flareG.drawImage(flareImage, 0, flareImage.getWidth()/2.0, flareImage.getWidth()/2.0, flareImage.getHeight()/2.0, posx, posy, size, size);
    }
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void mouseDown(MouseEvent event) {
        preDragX = event.getX();
        preDragY = event.getY();
    }
    
    @FXML
    private void mouseClick(MouseEvent event) {
        if(curPlanet != null) {
            if (curPlanet.getMarket() == null) {
                planetMarketplaceLabel.setText(curPlanet.Name() + " Marketplace");
                planetMarketplaceLabel.setFont(Font.font(curPlanet.Name().length()));
                market = new MarketPlace(curSystem.TechLevel(), curPlanet.Resources());
                generateBuyList();
                generateSellList();
            }
            marketplaceUI.setVisible(true);
            backToStarScreen.setVisible(false);
            generateBuyList();
        }
    }
    
    @FXML
    private void hideMarketplace() {
        marketplaceUI.setVisible(false);
        backToStarScreen.setVisible(true);
    }
    
    private Planet findClosestPlanet(Point p) {
        if(curSystem.Planets()[0] == null) {
            return null;
        }
        Planet forreturn = screenSpace.get(p);
        if(forreturn == null) {
            Point[] points = new Point[screenSpace.keySet().size()];
            screenSpace.keySet().toArray(points);
            Point closest = points[0];
            for(Point test : points) {
                if(closest.distance(p.x, p.y) > test.distance(p.x, p.y)) {
                    closest = test;
                }
            }
            if(closest.distance(p.x, p.y) < 20 + zoom) {
                forreturn = screenSpace.get(closest);
            }
        }
        return forreturn;
    }
    
    @FXML
    private void mouseMove(MouseEvent event) {
        Planet mouseOver = findClosestPlanet(new Point((int)event.getX(), (int)event.getY()));
        if(mouseOver != null) {
            drawSelection(new Point((int)event.getX(), (int)event.getY()), mouseOver);
            curPlanet = mouseOver;
        } else {
            if(!marketplaceUI.isVisible()) {
                curPlanet = null;
            }
            selectionG.clearRect(0, 0, 1024, 576);
        }
    }
    
    /**
     * update drag offsets when dragging
     * @param event info about the mouse
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        dragging = true;
        double tempX = event.getX();
        double tempY = event.getY();
        dragOffsetX = (preDragX - tempX);
        dragOffsetY = (preDragY - tempY);
        draw();
    }
    
    /**
     * when mouse is released, update mapoffset
     * @param event contains info about the mouse
     */
    @FXML
    private void mouseReleased(MouseEvent event) {
        if (dragging) {
            mapOffsetX += dragOffsetX;
            mapOffsetY += dragOffsetY;
            dragOffsetX = 0;
            dragOffsetY = 0;
            dragging = false;
            dragFinished = true;
            draw();
        }
    }
    
    @FXML
    private void handleBackAction() {
        timer.cancel();
        SpaceTrader.getInstance().goToGame();
    }
    
    /**
     * 
     * @param p
     * @return 
     */
    private Point getCartesianLocation(Point p, Point e) {
        double x,y;
        x = Math.cos(p.x/400.0) * p.y * ((e.x / 10.0) + 1);
        y = Math.sin(p.x/400.0) * p.y * ((e.y / 10.0) + 1);
        x += 512;
        y += 288;
        return new Point((int)x, (int)y);
    }

    /**
     * Used to set the scene so that timer thread can exit
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
            timer.cancel();
        });
    }
    
    /***************************************************
    *   Start of Marketplace Screen functions          *
    ****************************************************/
    
    private MarketPlace market;
    
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
    
    private void generateBuyList() {
        if (curPlanet != null) {
            ArrayList<TradeGood> goods = market.getListOfGoods();
            ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
            planetInventory.setItems(list);
        }
    }
    
    private void generateSellList() {
        ArrayList<TradeGood> goods = player.getShip().getCargo().getNonEmptyCargoList();
        ObservableList<TradeGood> list = FXCollections.observableArrayList(goods);
        playerInventory.setItems(list);
    }
    
    @FXML
    private void selectBuyableItem() {
//        int index = playerInventory.getEditingIndex();
//        if (index != -1) {
//            ArrayList<TradeGood> goods = market.getListOfGoods();
//            TradeGood good = goods.get(index);
//            buyDetails.setText("Cash: $" + player.getMoney());
//        }
        planetInventory.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                        buyDetails.setText(new_val);
                        buyDetails.setTextFill(Color.web(new_val));
            }
        });
    }
}
