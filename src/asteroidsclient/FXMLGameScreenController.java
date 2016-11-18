package asteroidsclient;

import asteroids.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import java.util.List;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    private AsteroidsGateway gateway;
    private Lock lock = new ReentrantLock();

    @FXML
    public AnchorPane mainPane;
    private ShipModel currentPlayer;

    @FXML
    public Text score;
    public int scorecount;
    @FXML
    public Text player1Lives;
    @FXML
    public Text player2Lives;

    private double playerRotation = 0.0;

    @FXML
    private ImageView player1Ship;
    @FXML
    private Pane player1Pane;
    private Point player1Loc = new Point(305, 250);

    @FXML
    private ImageView player2Ship;
    @FXML
    private Pane player2Pane;
    private Point player2Loc = new Point(395, 250);

    private boolean pressL, pressR, pressS;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            try {
                long lastShoot = 0;
                while (true) {
                    if (pressL && !pressR) {
                        setPlayerRotation(-5.0);
                        rotateCurrentPlayer(playerRotation);
                    }
                    if (!pressL && pressR) {
                        setPlayerRotation(5.0);
                        rotateCurrentPlayer(playerRotation);
                    }
                    if (pressS) {
                        // Use player location to set spawn point for bullet and use the rotation
                        // to set the velocity so that the bullet will be directed in the
                        // direction of the nose.
                        if (lastShoot + 30 < System.currentTimeMillis()) {
                            gateway.playerNewBullet(getPlayerLoc().x, getPlayerLoc().y, playerRotation);
                            lastShoot = System.currentTimeMillis();
                        }
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
            }
        }).start();
    }

    public void setGateway(AsteroidsGateway gateway) {
        this.gateway = gateway;
    }

    public void setScore() {
        scorecount = gateway.getScore();
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

    public Pane getPlayer1Pane() {
        return player1Pane;
    }

    public Pane getPlayer2Pane() {
        return player2Pane;
    }

    // This method converts any negative rotations to positive.
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

    public void keyEvent(boolean pressed, KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
                pressL = pressed;
                break;
            case RIGHT:
                pressR = pressed;
                break;
            case SPACE:
                pressS = pressed;
                break;
        }
    }
}

// Thread to update players' screens every few milliseconds.
class UpdatePlayer implements Runnable, asteroids.AsteroidsConstants {

    private final int playerNum;
    private final AsteroidsGateway gateway;
    private final Pane player1Pane;
    private final Pane player2Pane;
    private Text player1Lives;
    private Text player2Lives;
    private final ShipModel currentPlayer;
    private Text score;
    private AnchorPane mainPane;
    private Group bulletGroup;

    public UpdatePlayer(int playerNum, AsteroidsGateway gateway, Pane player1Pane, Pane player2Pane,
            Text player1Lives, Text player2Lives, ShipModel currentPlayer, Text score, AnchorPane mainPane) {
        this.playerNum = playerNum;
        this.gateway = gateway;
        this.player1Pane = player1Pane;
        this.player2Pane = player2Pane;
        this.player1Lives = player1Lives;
        this.player2Lives = player2Lives;
        this.currentPlayer = currentPlayer;
        this.score = score;
        this.mainPane = mainPane;
    }

    @Override
    public void run() {
        try {
            final ArrayList<Circle> previous = new ArrayList<>();
            while (true) {
                if (playerNum == 1) {
                    double tempPlayer2Rot = gateway.getPlayer2Rot();
                    Platform.runLater(() -> player2Pane.setRotate(tempPlayer2Rot));
                } else {
                    double tempPlayer1Rot = gateway.getPlayer1Rot();
                    Platform.runLater(() -> player1Pane.setRotate(tempPlayer1Rot));
                }

                int l1 = gateway.getPlayer1Lives();
                int l2 = gateway.getPlayer2Lives();
                int s = gateway.getScore();
                Platform.runLater(() -> {
                    score.setText("Score: " + s);
                    player1Lives.setText("P1 Lives: " + l1);
                    player2Lives.setText("P2 Lives: " + l2);
                });

                // Clear old shapes and execute update.
                List<Bullet> tempBullets = gateway.playerGetBullets();
                List<Asteroid> tempAsteroids = gateway.playerGetAsteroid();

                List<Circle> playerShapes = new ArrayList<>();

                synchronized (previous) {
                    for (Bullet b : tempBullets) {
                        playerShapes.add(new Circle(b.getRay().origin.x, b.getRay().origin.y, b.getRadius(), Color.RED));
                    }
                    for (Asteroid a : tempAsteroids) {
                        playerShapes.add(new Circle(a.returnX(), a.returnY(), a.returnRadius(), Color.GREY));
                    }

                    Platform.runLater(() -> {
                        synchronized (previous) {
                            mainPane.getChildren().removeAll(previous);
                            mainPane.getChildren().addAll(playerShapes);
                            previous.clear();
                            previous.addAll(playerShapes);
                            previous.notify();
                        }
                    });
                    previous.wait();
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
