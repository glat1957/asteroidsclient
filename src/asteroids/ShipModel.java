package asteroids;

import java.io.Serializable;

public class ShipModel implements Serializable{

    private String imageFileName;
    private int playerNum;
    private double rotation = 0; 
    
    public ShipModel(int playerNum, String imageFileName) {
        this.imageFileName = imageFileName;
        this.playerNum = playerNum;
    }
    
    public void setRotation(double rotation){
        this.rotation = rotation;
    }

    public double getRotation(){
        return rotation;
    }
    
    public int getPlayerNum(){
        return playerNum;
    }

    public String getImageFileName(){
        return imageFileName;
    }

}
