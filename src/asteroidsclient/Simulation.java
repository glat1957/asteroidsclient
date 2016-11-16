package asteroidsclient;

import asteroids.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Simulation {
    
    private Lock lock;
    private AsteroidsGateway gateway;
    private List<Bullet> bulletsInScene = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Circle> bulletShapes = new ArrayList<>();
    @FXML
    private AnchorPane mainPane;
    
    public Simulation(AsteroidsGateway gateway)
    {
        this.gateway = gateway;
        lock = new ReentrantLock();
    }
    
    public void evolve(double time)
    {
        lock.lock();
        for(int i = 0; i < bulletsInScene.size(); i++){
            bulletsInScene.get(i).move(time);
            boolean negativeOutOfBounds = bulletsInScene.get(i).getRay().origin.x < 0 || bulletsInScene.get(i).getRay().origin.y < 0;
            boolean positiveOutOfBounds = bulletsInScene.get(i).getRay().origin.x > mainPane.getWidth() || bulletsInScene.get(i).getRay().origin.y > mainPane.getHeight();
            if(positiveOutOfBounds || negativeOutOfBounds){
               removeBullet(i);
            }
        }
        lock.unlock();
    }
    
    public List<Bullet> getBullets(){
        return bulletsInScene;
    }
    
    public void setPane(AnchorPane pane){
        this.mainPane = pane;
    }
    
    private void removeBullet(int i){
        bulletsInScene.remove(i);
        bulletShapes.remove(i);
    }
    
    public ArrayList<Circle> getBulletShapes(){
        return bulletShapes;
    }
    
    public void updateShapes()
    {
        for(int i = 0; i < bulletsInScene.size(); i++){
            bulletShapes.get(i).setCenterX(bulletsInScene.get(i).getRay().origin.x);
            bulletShapes.get(i).setCenterY(bulletsInScene.get(i).getRay().origin.y);
        }
    }
}
