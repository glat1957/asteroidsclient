package asteroids;

import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Ship implements Serializable{

    private int lives;
    private String imageFileName;
    private int playerNum;
    //private Rectangle shipShape;

    public Ship(int playerNum, int lives, String imageFileName) {
        this.lives = lives;
        this.imageFileName = imageFileName;
        this.playerNum = playerNum;
        //shipShape = new Rectangle(70, 70);
    }

    public int getLives() {
        return lives;
    }

    public int getPlayerNum(){
        return playerNum;
    }
    
    //public Rectangle getShipShape(){
    //    return shipShape;
    //}
    
    public String getImageFileName(){
        return imageFileName;
    }

}
