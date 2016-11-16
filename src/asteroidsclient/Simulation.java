package asteroidsclient;

import asteroids.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Simulation {

    private Lock lock;
    private AsteroidsGateway gateway;
    private List<Bullet> bulletsInScene = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Circle> bulletShapes = new ArrayList<>();
    private List<Asteroid> asteroidsInScene = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Circle> asteroidShapes = new ArrayList<>();
    @FXML
    private AnchorPane mainPane;
    private boolean negativeOutOfBounds;
    private boolean positiveOutOfBounds;

    public Simulation(AsteroidsGateway gateway) {
        this.gateway = gateway;
        lock = new ReentrantLock();
    }

    public void evolve(double time) {
        lock.lock();
        for (int i = 0; i < bulletsInScene.size(); i++) {
            bulletsInScene.get(i).move(time);
            negativeOutOfBounds = bulletsInScene.get(i).getRay().origin.x < 0 || bulletsInScene.get(i).getRay().origin.y < 0;
            positiveOutOfBounds = bulletsInScene.get(i).getRay().origin.x > mainPane.getWidth() || bulletsInScene.get(i).getRay().origin.y > mainPane.getHeight();
            if (positiveOutOfBounds || negativeOutOfBounds) {
                Circle tempBulletToRemove = bulletShapes.get(i);

                Platform.runLater(() -> {
                    mainPane.getChildren().remove(tempBulletToRemove);

                });
                removeBullet(i);
            }
        }
        for (int j = 0; j < asteroidsInScene.size(); j++) {
            asteroidsInScene.get(j).move(time);
            negativeOutOfBounds = asteroidsInScene.get(j).getRay().origin.x < 0 || asteroidsInScene.get(j).getRay().origin.y < 0;
            positiveOutOfBounds = asteroidsInScene.get(j).getRay().origin.x > mainPane.getWidth() || asteroidsInScene.get(j).getRay().origin.y > mainPane.getHeight();
            if (positiveOutOfBounds || negativeOutOfBounds) {
                Circle tempAsteroidToRemove = asteroidShapes.get(j);

                Platform.runLater(() -> {
                    mainPane.getChildren().remove(tempAsteroidToRemove);

                });
                removeAsteroid(j);
            }
        }
        lock.unlock();
    }

    public List<Bullet> getBullets() {
        return bulletsInScene;
    }

    public List<Asteroid> getAsteroids() {
        return asteroidsInScene;
    }

    public void setPane(AnchorPane pane) {
        this.mainPane = pane;
    }

    public void removeBullet(int i) {
        bulletShapes.remove(i);
        bulletsInScene.remove(i);
    }

    public void removeAsteroid(int i) {
        asteroidShapes.remove(i);
        asteroidsInScene.remove(i);
    }

    public ArrayList<Circle> getBulletShapes() {
        return bulletShapes;
    }

    public ArrayList<Circle> getAsteroidShapes() {
        return asteroidShapes;
    }

    public void updateShapes() {
        for (int i = 0; i < bulletsInScene.size(); i++) {
            bulletShapes.get(i).setCenterX(bulletsInScene.get(i).getRay().origin.x);
            bulletShapes.get(i).setCenterY(bulletsInScene.get(i).getRay().origin.y);
        }
        for (int i = 0; i < asteroidsInScene.size(); i++) {
            asteroidShapes.get(i).setCenterX(asteroidsInScene.get(i).getRay().origin.x);
            asteroidShapes.get(i).setCenterY(asteroidsInScene.get(i).getRay().origin.y);
        }
    }

    public boolean isHit(Bullet bullet, Asteroid asteroid) {
        // Since asteroids and bullets are going to be represented using circles,
        // we can use the origin point and radius of each and the distance formula
        // to determine if they overlap.
        double distance = Math.pow((bullet.getRay().origin.x - asteroid.getRay().origin.x) * (bullet.getRay().origin.x - asteroid.getRay().origin.x)
                + (bullet.getRay().origin.y - asteroid.getRay().origin.y) * (bullet.getRay().origin.y - asteroid.getRay().origin.y), 0.5);

        if (distance < bullet.getRadius() + asteroid.returnRadius()) {
            return true;
        } else if (distance > bullet.getRadius() + asteroid.returnRadius()) {
            return false;
        } else {
            return true;
        }
    }
}
