package asteroids;

import java.io.Serializable;

public class ShipModel implements Serializable{

    private int lives;
    private String imageFileName;
    private int playerNum;
    private double rotation = 0; 
    
    public ShipModel(int playerNum, int lives, String imageFileName) {
        this.lives = lives;
        this.imageFileName = imageFileName;
        this.playerNum = playerNum;
    }
    
    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return rotation;
    }
    
    public int getLives() {
        return lives;
    }
    
    public void hit(){
        lives--;
    }

    public int getPlayerNum(){
        return playerNum;
    }

    public String getImageFileName(){
        return imageFileName;
    }

}
