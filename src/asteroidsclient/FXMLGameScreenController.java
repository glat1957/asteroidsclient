package asteroidsclient;

import asteroids.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.layout.Pane;
import java.lang.Math;
import javafx.scene.text.Text;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    private AsteroidsGateway gateway;
    private final Lock lock = new ReentrantLock();

    @FXML
    public AnchorPane mainPane;
    private ShipModel currentPlayer;
    public Simulation sim = new Simulation(gateway);
    public List<Bullet> bulletsInScene = sim.getBullets();
    public ArrayList<Circle> bulletShapes = sim.getBulletShapes();
    public List<Asteroid> asteroidsInScene = sim.getAsteroids();
    public ArrayList<Circle> asteroidShapes = sim.getAsteroidShapes();
    @FXML
    public Text score;
    public int scorecount = 0;
    @FXML
    public Text player1Lives;
    @FXML
    public Text player2Lives;

    @FXML
    private ImageView BackgroundImageView;
    private double playerRotation = 0.0;

    @FXML
    private ImageView player1Ship;
    @FXML
    private Pane player1Pane;
    @FXML
    private Circle player1Nose;
    private Point player1Loc = new Point(305, 250);

    @FXML
    private ImageView player2Ship;
    @FXML
    private Pane player2Pane;
    @FXML
    private Circle player2Nose;
    private Point player2Loc = new Point(395, 250);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setGateway(AsteroidsGateway gateway) {
        this.gateway = gateway;
    }

    public ShipModel getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(ShipModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getPlayerNum() {
        return currentPlayer.getPlayerNum();
    }

    public Point getPlayerLoc() {
        if (currentPlayer.getPlayerNum() == 1) {
            return player1Loc;
        } else {
            return player2Loc;
        }
    }

    public Point getPlayerNoseLoc() {
        if (currentPlayer.getPlayerNum() == 1) {
            return new Point(player1Nose.localToScene(player1Nose.getLayoutX(), player1Nose.getLayoutX()).getX(),
                    player1Nose.localToScene(player1Nose.getLayoutX(), player1Nose.getLayoutX()).getY());
        } else {
            return new Point(player2Nose.localToScene(player2Nose.getLayoutX(), player2Nose.getLayoutX()).getX(),
                    player2Nose.localToScene(player2Nose.getLayoutX(), player2Nose.getLayoutX()).getY());
        }
    }

    public Pane getPlayer1Pane() {
        return player1Pane;
    }

    public Pane getPlayer2Pane() {
        return player2Pane;
    }

    private Double setPlayerRotation(Double rotation) {
        playerRotation += rotation;
        if (playerRotation > 360.0) {
            playerRotation -= 360.0;
        } else if (playerRotation < 0.0) {
            playerRotation = 360.0 + playerRotation;
        }
        return playerRotation;
    }

    private void rotateCurrentPlayer(Double rotation) {
        if (currentPlayer.getPlayerNum() == 1) {
            player1Pane.setRotate(rotation);
            gateway.sendPlayer1Rot(playerRotation);
        } else {
            player2Pane.setRotate(rotation);
            gateway.sendPlayer2Rot(playerRotation);
        }
    }

    @FXML
    public void keyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT: {
                setPlayerRotation(-5.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case RIGHT: {
                setPlayerRotation(5.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case SPACE: {
                // Use player location to set spawn point for bullet and use the nose
                // location to set the velocity so that the bullet will be directed in the
                // direction of the nose.
                sim.getBullets().add(new Bullet(getPlayerLoc().x, getPlayerLoc().y,
                        10, Math.cos(Math.toRadians(playerRotation - 90)), Math.sin(Math.toRadians(playerRotation - 90))));
                sim.getBulletShapes().add(new Circle(getPlayerLoc().x, getPlayerLoc().y, 5, Color.RED));
                mainPane.getChildren().add(sim.getBulletShapes().get(sim.getBulletShapes().size() - 1));
                break;
            }
            case A: {
                sim.getAsteroids().add(new Asteroid());
                sim.getAsteroidShapes().add(new Circle(sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnX(),
                        sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnY(),
                        sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnRadius(), Color.GREY));
                mainPane.getChildren().add(sim.getAsteroidShapes().get(sim.getAsteroidShapes().size() - 1));
                break;
            }
        }
    }
}

// Thread to update players' screens every few milliseconds.
class UpdateOtherPlayer implements Runnable, asteroids.AsteroidsConstants {

    private final Lock lock = new ReentrantLock();
    private final int playerNum;
    private final AsteroidsGateway gateway;
    private final Pane player1Pane;
    private final Pane player2Pane;
    private Text player1Lives;
    private Text player2Lives;
    private final ShipModel currentPlayer;

    public UpdateOtherPlayer(int playerNum, AsteroidsGateway gateway, Pane player1Pane, Pane player2Pane,
            Text player1Lives, Text player2Lives, ShipModel currentPlayer) {
        this.playerNum = playerNum;
        this.gateway = gateway;
        this.player1Pane = player1Pane;
        this.player2Pane = player2Pane;
        this.player1Lives = player1Lives;
        this.player2Lives = player2Lives;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            while (true) {
                if (playerNum == 1) {
                    double tempPlayer2Rot = gateway.getPlayer2Rot();
                    Platform.runLater(() -> player2Pane.setRotate(tempPlayer2Rot));
                } else {
                    double tempPlayer1Rot = gateway.getPlayer1Rot();
                    Platform.runLater(() -> player1Pane.setRotate(tempPlayer1Rot));
                }

                /*
                if (playerNum == 1) {
                    Platform.runLater(() -> {
                        player2Lives.setText("P2 Lives: " + gateway.);
                    });
                } else if (playerNum == 2) {
                    Platform.runLater(() -> {
                        player1Lives.setText("P1 Lives: " + gateway.);
                    });
                }*/
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class GenerateAsteroids implements Runnable, asteroids.AsteroidsConstants {

    private final Lock lock = new ReentrantLock();
    private Simulation sim;
    private AnchorPane mainPane;

    public GenerateAsteroids(Simulation sim, AnchorPane mainPane) {
        this.sim = sim;
        this.mainPane = mainPane;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            while (true) {

                sim.getAsteroids().add(new Asteroid());
                sim.getAsteroidShapes().add(new Circle(sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnX(),
                        sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnY(),
                        sim.getAsteroids().get(sim.getAsteroids().size() - 1).returnRadius(), Color.GREY));
                Platform.runLater(() -> mainPane.getChildren().add(sim.getAsteroidShapes().get(sim.getAsteroidShapes().size() - 1)));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Simulate implements Runnable, asteroids.AsteroidsConstants {

    private final AsteroidsGateway gateway;
    private final Lock lock = new ReentrantLock();
    private final ShipModel currentPlayer;
    private final Simulation sim;
    private List<Bullet> bulletsInScene;
    private ArrayList<Circle> bulletShapes;
    private List<Asteroid> asteroidsInScene;
    private ArrayList<Circle> asteroidShapes;
    private AnchorPane mainPane;
    private Pane player1Pane;
    private Pane player2Pane;
    private int playerNum;
    private Text score;
    private int scorecount;
    private Text player1Lives;
    private Text player2Lives;

    public Simulate(AsteroidsGateway gateway, Simulation sim, List<Bullet> bulletsInScene,
            ArrayList<Circle> bulletShapes, List<Asteroid> asteroidsInScene,
            ArrayList<Circle> asteroidShapes, AnchorPane mainPane, Text score, int scorecount,
            Pane player1Pane, Pane player2Pane, int playerNum, ShipModel currentPlayer,
            Text player1Lives, Text player2Lives) {
        this.gateway = gateway;
        this.sim = sim;
        this.bulletsInScene = bulletsInScene;
        this.bulletShapes = bulletShapes;
        this.asteroidsInScene = asteroidsInScene;
        this.asteroidShapes = asteroidShapes;
        this.mainPane = mainPane;
        this.score = score;
        this.scorecount = scorecount;
        this.player1Pane = player1Pane;
        this.player2Pane = player2Pane;
        this.playerNum = playerNum;
        this.currentPlayer = currentPlayer;
        this.player1Lives = player1Lives;
        this.player2Lives = player2Lives;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            while (true) {

                sim.evolve(1.0);
                sim.updateShapes();

                // Every time the simulation updates, check for bullet/asteroid collisions.
                for (int k = 0; k < bulletsInScene.size(); k++) {
                    for (int m = 0; m < asteroidsInScene.size(); m++) {
                        if (sim.isHit(bulletsInScene.get(k), asteroidsInScene.get(m))) {
                            Circle tempBulletToRemove = bulletShapes.get(k);
                            Circle tempAsteroidToRemove = asteroidShapes.get(m);

                            scorecount++;
                            Platform.runLater(() -> {
                                mainPane.getChildren().remove(tempBulletToRemove);
                                mainPane.getChildren().remove(tempAsteroidToRemove);
                                score.setText("Score: " + scorecount);
                            });

                            sim.removeBullet(k);
                            sim.removeAsteroid(m);
                        }
                    }
                }

                // Every time the simulation updates, check for asteroid/ship collisions.
                if (playerNum == 1) {
                    for (int d = 0; d < asteroidsInScene.size(); d++) {
                        if (player1Pane.getBoundsInParent().intersects(asteroidShapes.get(d).getBoundsInParent())) {
                            Circle tempAsteroidToRemove = asteroidShapes.get(d);

                            currentPlayer.hit();

                            Platform.runLater(() -> {
                                mainPane.getChildren().remove(tempAsteroidToRemove);
                                player1Lives.setText("P1 Lives: " + currentPlayer.getLives());
                            });

                            sim.removeAsteroid(d);
                        }
                    }
                } else if (playerNum == 2) {
                    for (int d = 0; d < asteroidsInScene.size(); d++) {
                        if (player2Pane.getBoundsInParent().intersects(asteroidShapes.get(d).getBoundsInParent())) {
                            Circle tempAsteroidToRemove = asteroidShapes.get(d);

                            currentPlayer.hit();

                            Platform.runLater(() -> {
                                mainPane.getChildren().remove(tempAsteroidToRemove);
                                player2Lives.setText("P2 Lives: " + currentPlayer.getLives());
                            });

                            sim.removeAsteroid(d);
                        }
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
