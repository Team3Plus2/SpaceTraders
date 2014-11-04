/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates.
 * and open the template in the editor.
 */
package spacetrader.view;

import java.awt.Point;
import java.awt.geom.Point2D;
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
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import spacetrader.cosmos.system.Planet;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;

/**
 * FXML Controller class.
 *
 * @author KartikKini
 */
public class SolarSystemViewController implements Initializable {

    /**
     * Draws planets.
     */
    private GraphicsContext g;
    /**
     * Draws flares.
    */
    private GraphicsContext flareG;
    /**
     * Draws overlays.
     */
    private GraphicsContext selectionG;

    /**
     * The image of the star.
     */
    private Image starImage;
    /**
     * The image of the flare.
     */
    private Image flareImage;

    /**
     * Current solar system.
     */
    private SolarSystem curSystem;
    /**
     * Current planet.
     */
    private Planet curPlanet;
    /**
     * Current player.
     */
    private Player player;

    /**
     * Timer for the game.
     */
    private Timer timer;

    /**
     * Slider that controls zoom.
     */
    @FXML
    private Slider zoomSlider;
    /**
     * Background.
     */
    @FXML
    private Pane starBackdrop;
    /**
     * Layer where flare is drawn.
     */
    @FXML
    private Canvas flareLayer;
    /**
     * Main layer.
     */
    @FXML
    private Canvas viewCanvas;
    /**
     * Layer for selection.
     */
    @FXML
    private Canvas selectionLayer;
    /**
     * Button to go to star screen.
     */
    @FXML
    private Button backToStarScreen;
    /**
     * Button to travel.
     */
    @FXML
    private Button travelToPlanet;

    /**
     * Zoom controller.
     */
    private double zoom = 1;
    /**
     * Pre drag X direction.
     */
    private double preDragX;
    /**
     * Pre drag Y direction.
     */
    private double preDragY;
    /**
     * Check for drag.
     */
    private boolean dragging;
    /**
     * Drag offset X direction.
     */
    private double dragOffsetX;
    /**
     * Drag offset Y direction.
     */
    private double dragOffsetY;
    /**
     * Map offset X direction.
     */
    private double mapOffsetX;
    /**
     * Map offset Y direction.
     */
    private double mapOffsetY;
    /**
     * Check for drag done.
     */
    private boolean dragFinished;
    /**
     * Current planet coordinates.
     */
    private Point.Double currentPlanetCoords;
    /**
     * Selected planet coordinates.
     */
    private Point.Double selectedPlanetCoords;
    /**
     * Mouse position.
     */
    private Point.Double currentMousePosition;
    /**
     * Selected planet.
     */
    private Planet selectedPlanet;
    
    /**
     * Map of planets.
     */
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
        if (curSystem.SunType().usesImage() && curSystem.SunType().getImage().contains("RedGiant")) {
            starImage = new Image("/visuals/Stars/RedGiant_big.png");
            flareImage = new Image("/visuals/Stars/RedGiantFlareSheet.png");
        } else if (curSystem.SunType().usesImage() && curSystem.SunType().getImage().contains("Binary")) {
            starImage = new Image("/visuals/Stars/Binary_big.png");
            flareImage = new Image("/visuals/Stars/BinaryFlareSheet.png");
        } else {
            starImage = new Image("/visuals/Stars/Sol_big.png");
            flareImage = new Image("/visuals/Stars/SolFlareSheet.png");
        }
        screenSpace = new HashMap<>();

        checkRadii();

        updateTimer();

        //adds listener for the zoom slider.
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float) zoomSlider.getValue();
                draw();
            }

        });

        //draw initial view.
        draw();

        //draw initial flare.
        double size = (starImage.getWidth() / 2);
        double posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        double posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        flareG.setGlobalBlendMode(BlendMode.ADD);
        drawFlares(size, posx, posy);
    }
    
    /**
     * Ensure no planets are too close to one another.
     */
    private void checkRadii() {
        if (curSystem.Planets().length > 1) {
            int i = 0;
            while (i < curSystem.Planets().length) {
                for (int j = i + 1; j < curSystem.Planets().length; j++) {
                    int p1 = curSystem.Planets()[i].getLocation().y;
                    int p2 = curSystem.Planets()[j].getLocation().y;
                    while (Math.abs(p1 - p2) < 30) {
                        curSystem.Planets()[j].setLocation(new Point(curSystem.Planets()[j].getLocation().x, p2 + 1));
                        p1 = curSystem.Planets()[i].getLocation().y;
                        p2 = curSystem.Planets()[j].getLocation().y;
                        i = 0;
                    }
                }
                i++;
            }
        }
    }

    /**
     * Update orbits.
     */
    private void updateTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                        for (Planet p : curSystem.Planets()) {
                            if (p.getLocation().x + 1 > 144000) {
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

    /**
     * Updates the canvas scales.
     */
    private void updateCanvasScales() {
        starBackdrop.setScaleX(2 + (zoom / 30));
        starBackdrop.setScaleY(2 + (zoom / 30));
        starBackdrop.setTranslateX(-(dragOffsetX + mapOffsetX) / 4);
        starBackdrop.setTranslateY(-(dragOffsetY + mapOffsetY) / 4);
        selectionLayer.setScaleX(1 + (zoom / 10));
        selectionLayer.setScaleY(1 + (zoom / 10));
        viewCanvas.setScaleX(1 + (zoom / 10));
        viewCanvas.setScaleY(1 + (zoom / 10));
        flareLayer.setScaleX(1 + (zoom / 20));
        flareLayer.setScaleY(1 + (zoom / 20));
    }

    /**
     * Draws the solar system.
     */
    private void draw() {
        g.clearRect(0, 0, 1024, 576);
        updateCanvasScales();
        double size = (starImage.getWidth() / 2);
        double posx = 512 - (size / 2) - dragOffsetX - mapOffsetX;
        double posy = 288 - (size / 2) - dragOffsetY - mapOffsetY;
        g.drawImage(starImage, posx, posy, size, size);
        if (dragging) {
            flareG.setGlobalBlendMode(BlendMode.SRC_OVER);
            flareG.clearRect(0, 0, 1024, 576);
            flareG.setGlobalBlendMode(BlendMode.ADD);
            drawFlares(size, posx, posy);
        }
        g.setStroke(Color.WHITE);
        for (Planet p : curSystem.Planets()) {
            posx = getCartesianLocation(p.getLocation(), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX;
            posy = getCartesianLocation(p.getLocation(), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY;
            for (int i = 0; i < 90; i++) {
                g.setFill(Color.gray(Math.max(Math.min(Math.abs(Math.sin(i / 50.0)), 0.8), 0.5)));
                g.fillOval(getCartesianLocation(new Point(i * 14, p.getLocation().y), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX - 1, getCartesianLocation(new Point(i * 14, p.getLocation().y), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY - 1, 2, 2);
                g.fillOval(getCartesianLocation(new Point((i + 90) * 14, p.getLocation().y), p.getOrbitEllipse()).x - dragOffsetX - mapOffsetX - 1, getCartesianLocation(new Point((i + 90) * 14, p.getLocation().y), p.getOrbitEllipse()).y - dragOffsetY - mapOffsetY - 1, 2, 2);
            }
            if (p.equals(player.getCurrentPlanet())) {
                currentPlanetCoords = new Point.Double(posx, posy);
                g.setFill(Color.CORNFLOWERBLUE);
                g.fillOval(posx - 20, posy - 20, 40, 40);
            }
            g.setFill(Color.WHITE);
            g.fillOval(posx - 5, posy - 5, 10, 10);
            screenSpace.put(new Point((int) (posx - 5), (int) (posy - 5)), p);
            if(p.equals(curPlanet)) {
                selectionG.clearRect(0, 0, 1024, 576);
                selectionG.setStroke(Color.WHITE);
                selectionG.setFill(Color.WHITE);
                selectionG.strokeOval(posx - 15, posy - 15, 30, 30);
                selectionG.fillText(curPlanet.Name(), posx + 15, posy - 15);
                selectionG.fillText(curPlanet.Resources().toString(), posx + 15, posy);
                if (selectedPlanetCoords != null) {
                    g.strokeLine(currentPlanetCoords.x, currentPlanetCoords.y, posx, posy);
                }
            }
        }
        
        curPlanet = planetHitTest();
        
    }

    /**
     * Draws solar flares.
     * @param size size of the sun
     * @param posx position x of the sun
     * @param posy position y of the sun
     */
    private void drawFlares(double size, double posx, double posy) {
        double size1 = size;
        double posx1 = posx;
        double posy1 = posy;
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 4), posy1 + ((dragOffsetY + mapOffsetY) * 4), size1, size1);
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 2), posy1 + ((dragOffsetY + mapOffsetY) * 2), size1, size1);
        size1 *= 4;
        posx1 = 512 - (size1 / 2) - dragOffsetX - mapOffsetX;
        posy1 = 288 - (size1 / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, 0, 0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1, posy1 - 10, size1, size1);
        size1 /= 3;
        posx1 = 512 - (size1 / 2) - dragOffsetX - mapOffsetX;
        posy1 = 288 - (size1 / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, 0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 3.5), posy1 + ((dragOffsetY + mapOffsetY) * 3.5), size1, size1);
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 2.5), posy1 + ((dragOffsetY + mapOffsetY) * 2.5), size1, size1);
        size1 /= 4;
        posx1 = 512 - (size1 / 2) - dragOffsetX - mapOffsetX;
        posy1 = 288 - (size1 / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, 0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 3), posy1 + ((dragOffsetY + mapOffsetY) * 3), size1, size1);
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 1.5), posy1 + ((dragOffsetY + mapOffsetY) * 1.5), size1, size1);
        size1 /= 4;
        posx1 = 512 - (size1 / 2) - dragOffsetX - mapOffsetX;
        posy1 = 288 - (size1 / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 1.75), posy1 + ((dragOffsetY + mapOffsetY) * 1.75), size1, size1);
        size1 /= 4;
        posx1 = 512 - (size1 / 2) - dragOffsetX - mapOffsetX;
        posy1 = 288 - (size1 / 2) - dragOffsetY - mapOffsetY;
        flareG.drawImage(flareImage, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1 + ((dragOffsetX + mapOffsetX) * 3.75), posy1 + ((dragOffsetY + mapOffsetY) * 3.75), size1, size1);
        size1 *= 200;
        posx1 = 512 - (size1 / 2) - ((dragOffsetX + mapOffsetX) * 4);
        posy1 = 288 - (size1 / 2) - ((dragOffsetY + mapOffsetY) * 4);
        flareG.drawImage(flareImage, 0, flareImage.getWidth() / 2.0, flareImage.getWidth() / 2.0, flareImage.getHeight() / 2.0, posx1, posy1, size1, size1);
    }

    /**
     * On click what happens.
     * @param event Something happened
     */
    @FXML
    private void mouseDown(MouseEvent event) {
        preDragX = event.getX();
        preDragY = event.getY();
        currentMousePosition = new Point2D.Double(event.getX(), event.getY());
    }

    /**
     * Finds closest planet.
     * @param p planet
     * @return closest planet
     */
    private Planet findClosestPlanet(Point p) {
        if (curSystem.Planets()[0] == null) {
            return null;
        }
        Planet forreturn = screenSpace.get(p);
        if (forreturn == null) {
            Point[] points = new Point[screenSpace.keySet().size()];
            screenSpace.keySet().toArray(points);
            Point closest = points[0];
            for (Point test : points) {
                if (closest.distance(p.x, p.y) > test.distance(p.x, p.y)) {
                    closest = test;
                }
            }
            if (closest.distance(p.x, p.y) < 20 + zoom) {
                forreturn = screenSpace.get(closest);
            }
        }
        return forreturn;
    }

    /**
     * Detect mouse movement.
     * @param event Something happened
     */
    @FXML
    private void mouseMove(MouseEvent event) {
        currentMousePosition = new Point.Double(event.getX(), event.getY());
        curPlanet = planetHitTest();
    }
    
    /**
     * Check if planet has been touched.
     * @return hit planet
     */
    private Planet planetHitTest() {
        if (currentMousePosition == null) {
            return null;
        }
        Planet mouseOver = findClosestPlanet(new Point((int) currentMousePosition.x, (int) currentMousePosition.y));
        if (mouseOver != null) {
            selectedPlanetCoords = currentMousePosition;
            return mouseOver;
        } else {
            selectedPlanetCoords = null;
            selectionG.clearRect(0, 0, 1024, 576);
            return null;
        }
    }

    /**
     * Update drag offsets when dragging.
     * @param event info about the mouse
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        currentMousePosition.x = event.getX();
        currentMousePosition.y = event.getY();
        dragging = true;
        double tempX = event.getX();
        double tempY = event.getY();
        dragOffsetX = (preDragX - tempX);
        dragOffsetY = (preDragY - tempY);
        draw();
    }
    
    /**
     * Detect mouse click.
     * @param event Something happened
     */
    @FXML
    private void mouseClick(MouseEvent event) {
        selectedPlanet = planetHitTest();
        if (selectedPlanet != null) {
            travelToPlanet.setVisible(true);
            travelToPlanet.setText("Travel To " + selectedPlanet.Name());
        } else {
            travelToPlanet.setVisible(false);
        }
    }
    
    /**
     * Button to travel to a planet.
     */
    @FXML
    private void travelButton() {
        if (selectedPlanet != null) {
            timer.cancel();
            player.move(selectedPlanet);
            
            if (player.isDead()) {
                SpaceTrader.getInstance().goToWelcomeScreen();
                return;
            }
            
            SpaceTrader.getInstance().goToPlanetView();
        }
    }

    /**
     * When mouse is released, update mapoffset.
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

    /**
     * Go to the universe page.
     */
    @FXML
    private void handleBackAction() {
        timer.cancel();
        SpaceTrader.getInstance().goToGame();
    }

    /**
     * Location.
     * @param p planet
     * @param e planet
     * @return new Point
     */
    private Point getCartesianLocation(Point p, Point e) {
        double x;
        double y;
        x = Math.cos(p.x / 400.0) * p.y * ((e.x / 10.0) + 1);
        y = Math.sin(p.x / 400.0) * p.y * ((e.y / 10.0) + 1);
        x += 512;
        y += 288;
        return new Point((int) x, (int) y);
    }

    /**
     * Used to set the scene so that timer thread can exit.
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
                timer.cancel();
            });
    }
}
