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
import java.util.Scanner;//used in temporary command line encounters
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.WindowEvent;
import spacetrader.cosmos.SparseSpace;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.Planet;
import spacetrader.player.Player;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.SunType;
import spacetrader.main.SpaceTrader;
import spacetrader.save.SaveGame;
import spacetrader.turns.TurnListener;
import spacetrader.economy.MarketPlace;//used in temporary command line encounters
import spacetrader.economy.TradeGood;//used in temporary command line encounters
import spacetrader.player.*;//used in temporary command line encounters
import spacetrader.encounter.Encounter;//used in temporary command line encounters
import spacetrader.turns.TurnEvent;


/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class StarScreenController implements Initializable {

    private static final int GENERATION_BUFFER = 10;
    
    @FXML
    private Label playerInfo;
    
    @FXML
    private Pane starBackdrop;
    
    private Player player;
    
    private Universe universe;
    
    private GraphicsContext g;
    
    private double zoom = 10;
    
    private double dragOffsetX = 0, dragOffsetY = 0, preDragX = 0, preDragY = 0;
    
    private double mapOffsetX = 0, mapOffsetY = 0;
    
    private boolean dragging = false, dragFinished = true;
    
    private HashMap<String, Image> sunImages;
    
    @FXML
    private Canvas gameCanvas;
    
    @FXML
    private Slider zoomSlider;
    
    private HashMap<Point, SolarSystem> solarSystemLocations;
    
    private SolarSystem selectedSolarSystem;
    
    @FXML
    private Button goToPlanet;
    
    private ArrayList<SolarSystem> travelable;
    
    private final int universeScale = 20;
    
    @FXML
    private BorderPane solarSystemInfo;
    
    @FXML
    private Label solarSystemName;
    
    @FXML
    private ListView planetList;
    
    @FXML
    private Label fuelInfo;
    
    private Timer timer;
    
    int tempSelectedX = 0, tempSelectedY = 0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        universe = SpaceTrader.getInstance().getUniverse();
        player = SpaceTrader.getInstance().getPlayer();
        travelable = player.getTravelable(universe);
        solarSystemLocations = new HashMap<>();
        sunImages = new HashMap<>();
        
        for(SunType star : (ArrayList<SunType>)SunType.getList(SunType.class)) {
            if(star.usesImage())
                sunImages.put(star.getName(), new Image(star.getImage()));
        }
        
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue();
                dragFinished = true;
                drawUniverse();
            }
        
        });
        
        playerInfo.setText(player.getName() + "\n"
                + "Pilot Skill: " + player.getPilotSkill() + "\n"
                + "Fighter Skill: " + player.getFighterSkill() + "\n"
                + "Trader Skill: " + player.getTraderSkill() + "\n"
                + "Engineer Skill: " + player.getEngineerSkill() + "\n"
                + "Investor Skill: " + player.getInvestorSkill());
        // gets graphic object and draws universe to begin
        g = gameCanvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        
        SaveGame.save(player.getName() + ".sav", player, universe);
        
        drawUniverse();
    }
    
    /**
     * Happens any time mouse is clicked
     * @param event 
     */
    @FXML
    private void mouseClicked(MouseEvent event) {
        checkClick(event);
    }
    
    /**
     * Occurs any time the mouse is clicked
     * @param event 
     */
    @FXML
    private void mouseDown(MouseEvent event) {
        preDragX = event.getX();
        preDragY = event.getY();
    }
    
    /**
     * Moves player to selected solar system
     */
    @FXML
    private void goToSolarSystem() {
        Planet selected = (Planet)planetList.getSelectionModel().getSelectedItem();
        if(selected == null) {
            player.move(selectedSolarSystem);
        } else {
            player.move(selectedSolarSystem, selected);
        }
        if(timer != null) {
            timer.cancel();
        }
        Random rand = new Random();
        if(rand.nextFloat() < 0.5) {
            System.out.println("HERE");
            SpaceTrader.getInstance().goToEncounter();
        }
        if(player.isDead()) {
            SpaceTrader.getInstance().goToWelcomeScreen();
            return;
        }
        SpaceTrader.getInstance().goToSolarSystemView();
    }
    
    /**
     * Helper method that finds solar system closest to a point
     * @param p
     * @return SolarSystem closest
     */
    private SolarSystem findClosestSolarSystem(Point p) {
        SolarSystem forreturn = solarSystemLocations.get(p);
        if(forreturn == null) {
            Point[] points = new Point[solarSystemLocations.keySet().size()];
            solarSystemLocations.keySet().toArray(points);
            Point closest = points[0];
            for(Point test : points) {
                if(closest.distance(p.x, p.y) > test.distance(p.x, p.y)) {
                    closest = test;
                }
            }
            if(closest.distance(p.x, p.y) < 10) {
                forreturn = solarSystemLocations.get(closest);
            }
        }
        return forreturn;
    }
    
    @FXML
    private void mouseMove(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if(mouseOver != null) {
            drawUniverse();
            g.setStroke(Color.WHITE);
            g.setFill(Color.WHITE);
            g.strokeOval(event.getX() - 15, event.getY() - 15, 30, 30);
            g.fillText(mouseOver.Name(), event.getX(), event.getY());
            if(travelable.contains(mouseOver)) {
                double radx = ((player.getCurrentSolarSystem().getX() * universeScale) - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX);
                double rady = ((player.getCurrentSolarSystem().getY() * universeScale) - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY);
                g.strokeLine(radx, rady, event.getX(), event.getY());
            }
        } else {
            drawUniverse();
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
        dragOffsetX = (preDragX - tempX)/2;
        dragOffsetY = (preDragY - tempY)/2;
        drawUniverse();
    }
    
    private Point getSolarSystemCoords(SolarSystem s) {
        for (Point p : solarSystemLocations.keySet()) {
            if(solarSystemLocations.get(p).equals(s)) {
                return p;
            }
        }
        return null;
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
            solarSystemLocations.clear();
            drawUniverse();
        }
    }
    
    /**
     * helper method that checks if we clicked on a solar system
     * @param event contains info about where the mouse was clicked
     */
    private void checkClick(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if(mouseOver != null && travelable.contains(mouseOver)) {
            selectedSolarSystem = mouseOver;
            animateInfoScreen(true);
            solarSystemName.setText(mouseOver.Name());
            ObservableList<Planet> planets = FXCollections.observableArrayList(mouseOver.Planets());
            planetList.setItems(planets);
            fuelInfo.setText("Current:\n" + player.getShip().getFuel() + "\n\nCost:\n" + player.distanceToSolarSystem(mouseOver) + "\n\nSum:\n" + (player.getShip().getFuel() - player.distanceToSolarSystem(mouseOver)));
            drawUniverse();
        } else {
            drawUniverse();
        }
    }
    
    /**
     * Helper method that draws universe based on map offset and scale
     */
    private void drawUniverse() {
        Random r = new Random();
        Point lower = getLower();
        Point upper = getUpper();
        lower = new Point(lower.x / (universeScale), lower.y / (universeScale));
        upper = new Point(upper.x / (universeScale), upper.y / (universeScale));

        universe.generateFrom(lower.x, lower.y, upper.x, upper.y );
        
        //sets scale of star backdrop
        starBackdrop.setScaleX(zoom/5);
        starBackdrop.setScaleY(zoom/5);
        gameCanvas.setScaleX(zoom/10);
        gameCanvas.setScaleY(zoom/10);
        
        //clears before drawing
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        //int x = universe.xMin(), y = universe.yMin();
        starBackdrop.setTranslateX((-mapOffsetX - dragOffsetX)*2);
        starBackdrop.setTranslateY((-mapOffsetY - dragOffsetY)*2);
        
        //draw non travelable solar systems
        for (SparseSpace.SparseIterator iter = universe.iterateFrom(lower.x, lower.y, upper.x, upper.y); iter.hasNext();) {
            SolarSystem a = iter.next();
            Image starImage = null;
            if(a != null && !travelable.contains(a)) {
                int x = a.getX() * universeScale;
                int y = a.getY() * universeScale;
                r.setSeed(a.Name().hashCode());
                
                //set color of star based on suntype
                if(a.SunType().usesColor()) {
                    g.setFill(new Color(a.SunType().getR(), a.SunType().getG(), a.SunType().getB(), 1.0));
                } else {
                    starImage = sunImages.get(a.SunType().getName());
                }
                
                //initializes size and position variables
                double size = 20 + a.Planets().length * 2;
                double xPosition = (x - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX) - (size/2);
                double yPosition = (y - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY) - (size/2);
                if(dragFinished) {
                    solarSystemLocations.put(new Point((int) (xPosition + (size/2)), (int) (yPosition + (size/2))), a);
                }
                if(starImage == null) {
                    g.fillOval(xPosition, yPosition, size, size);
                } else {
                    Affine old = g.getTransform();
                    double angle = r.nextDouble() * 360;
                    rotate(angle, xPosition + size/2, yPosition + size/2);
                    g.drawImage(starImage, xPosition, yPosition, size, size);
                    g.setTransform(old);
                }
                g.setFill(Color.WHITE);
            }
        }
        
        //darken non-travelable planets
        g.setFill(new Color(0, 0, 0, 0.7));
        g.fillRect(0, 0, 1024, 512);
        
        //draw travel radius
        double radsize = player.getTravelRadius()*universeScale*2;
        double radx = (((player.getCurrentSolarSystem().getX() * universeScale) - mapOffsetX - dragOffsetX)) + (512 - mapOffsetX - dragOffsetX) - (radsize/2f);
        double rady = (((player.getCurrentSolarSystem().getY() * universeScale) - mapOffsetY - dragOffsetY)) + (288 - mapOffsetY - dragOffsetY) - (radsize/2f);
        g.strokeOval(radx, rady, radsize, radsize);
        g.setFill(new Color(0, 0, 1, 0.1));
        g.fillOval(radx, rady, radsize, radsize);
        
        //draw travelable planets
        g.setStroke(Color.WHITE);
        g.setFill(Color.WHITE);
        for(SolarSystem a : travelable) {
            r.setSeed(a.Name().hashCode());
            int x = a.getX() * universeScale;
            int y = a.getY() * universeScale;
            Image starImage = null;
            if(a.SunType().usesColor()) {
                g.setFill(new Color(a.SunType().getR(), a.SunType().getG(), a.SunType().getB(), 1.0));
            } else {
                starImage = sunImages.get(a.SunType().getName());
            }
            double size = 20 + a.Planets().length * 2;
            double xPosition = (x - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX) - (size/2);
            double yPosition = (y - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY) - (size/2);

            if(dragFinished) {
                solarSystemLocations.put(new Point((int) (xPosition + (size/2)), (int) (yPosition + (size/2))), a);
            }
            if(starImage == null) {
                g.fillOval(xPosition,yPosition, size, size);
            } else {
                Affine old = g.getTransform();
                double angle = r.nextDouble() * 360;
                rotate(angle, xPosition + size/2, yPosition + size/2);
                g.drawImage(starImage, xPosition, yPosition, size, size);
                g.setTransform(old);
            }
            
            if(a.equals(selectedSolarSystem)) {
                tempSelectedX = (int) ((x - dragOffsetX - mapOffsetX) + (dragOffsetX - mapOffsetX));
                tempSelectedY = (int) ((y - dragOffsetY - mapOffsetY) + (dragOffsetY - mapOffsetY));
            }
        }
        
    }
    
    private void rotate(double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
    
    private Point getLower() {
        int lowerX;
        int lowerY;
        lowerY = (int)(-gameCanvas.getHeight() + mapOffsetY*2);
        lowerX = (int)(-gameCanvas.getWidth() + mapOffsetX*2);
        return new Point(lowerX, lowerY);
    }
    
    private Point getUpper() {
        int upperX;
        int upperY;
        upperY = (int)(gameCanvas.getHeight() + mapOffsetY*2);
        upperX = (int)(gameCanvas.getWidth() + mapOffsetX*2);
        return new Point(upperX, upperY);
    }
    
    private void animateInfoScreen(boolean appear) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if(appear) {
                        dragging = true;
                        if (solarSystemInfo.getScaleY() < 1) {
                            solarSystemInfo.setScaleY(solarSystemInfo.getScaleY() + 0.1);
                        } else {
                            solarSystemInfo.setScaleY(1);
                            this.cancel();
                        }
                    } else {
                        if(solarSystemInfo.getScaleY() > 0) {
                            solarSystemInfo.setScaleY(solarSystemInfo.getScaleY() - 0.1);
                        } else {
                            solarSystemInfo.setScaleY(0);
                            this.cancel();
                        }
                    }
                    drawUniverse();
                });
            }
        }, 0, 10);
    }
    
    /**
     * Used to set the scene so that timer thread can exit
     *
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
            if(timer != null) {
                System.out.println("here");
                timer.cancel();
            }
        });
    }
    
    @FXML
    private void closeInfoPane() {
        animateInfoScreen(false);
    }
    
    
}
