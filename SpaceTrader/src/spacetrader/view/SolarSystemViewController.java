/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.view;

import java.awt.Point;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class SolarSystemViewController implements Initializable {
    
    @FXML
    private Canvas viewCanvas;
    
    private GraphicsContext g;
    
    private GraphicsContext flareG;
    
    private Image starImage;
    private Image flareImage;
    
    private SolarSystem curSystem;
    
    private Player player;
    
    private Timer timer;
    
    @FXML
    private Slider zoomSlider;
    
    @FXML
    private Pane starBackdrop;
    
    @FXML
    private Canvas flareLayer;
    
    private double zoom = 1;
    private double preDragX;
    private double preDragY;
    private boolean dragging;
    private double dragOffsetX;
    private double dragOffsetY;
    private double mapOffsetX;
    private double mapOffsetY;
    private boolean dragFinished;

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
        starImage = new Image("/visuals/Stars/Sol_big.png");
        flareImage = new Image("/visuals/Stars/SolFlareSheet.png");
        Random r = new Random();
        
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
                    draw();
                });
            }
        }, 0, 100);
        
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
    
    /**
     * draws the solar system
     */
    private void draw() {
        g.clearRect(0, 0, 1024, 576);
        starBackdrop.setScaleX(2 + (zoom / 30));
        starBackdrop.setScaleY(2 + (zoom / 30));
        starBackdrop.setTranslateX(-(dragOffsetX + mapOffsetX)/4);
        starBackdrop.setTranslateY(-(dragOffsetY + mapOffsetY)/4);
        viewCanvas.setScaleX(1 + (zoom / 10));
        viewCanvas.setScaleY(1 + (zoom / 10));
        flareLayer.setScaleX(1 + (zoom / 20));
        flareLayer.setScaleY(1 + (zoom / 20));
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
        }
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
    
    //GOING TO IMPLEMENT MOUSE OVER
//    @FXML
//    private void mouseMove(MouseEvent event) {
//        if(mouseOver != null) {
//            draw();
//            //Point p = getSolarSystemCoords(mouseOver);
//            g.setStroke(Color.WHITE);
//            g.strokeOval(event.getX() - 15, event.getY() - 15, 30, 30);
//            g.fillText(mouseOver.Name(), event.getX(), event.getY());
//        } else {
//            draw();
//        }
//    }
    
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
     * @param scene 
     */
    public void setScene(Scene scene) {
        scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
            timer.cancel();
        });
    }
    
}
