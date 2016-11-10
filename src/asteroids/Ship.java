package asteroids;

import java.io.Serializable;
import javafx.scene.image.Image;

public class Ship implements Serializable{

    private int lives;
    private String imageFileName;
    private int playerNum;

    public Ship(int playerNum, int lives, String imageFileName) {
        this.lives = lives;
        this.imageFileName = imageFileName;
        this.playerNum = playerNum;
    }

    public int getLives() {
        return lives;
    }

    public int getPlayerNum(){
        return playerNum;
    }
    
    public String getImageFileName(){
        return imageFileName;
    }

}
