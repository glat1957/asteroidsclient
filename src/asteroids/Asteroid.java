package asteroids;

import physics.*;
import java.util.Random;

public class Asteroid {

    Random random = new Random();
    private int x;
    private int y;
    private int dX;
    private int dY;
    private int asteroidRadius;
    private Ray directionRay;
    Asteroid temp = null;

    public Asteroid() {
        int candidateX = random.nextInt(550)+1;
        int candidateY = random.nextInt(430)+1;
        
        if (candidateX>275 && candidateX<425){
            x = candidateX+150;
        }else{
            x = candidateX;
        }
        
        if (candidateY>220 && candidateY<280){
            y = candidateY+60;
        }else{
            y = candidateY;
        }
        
        asteroidRadius = 20;
        dX = random.nextInt(20) - 10;
        dY = random.nextInt(20) - 10;

        Vector velocity = new Vector(dX, dY);
        double speed = 3;
        directionRay = new Ray(new Point(x, y), velocity, speed);
    }
    
    public void move(double time) {
        directionRay = new Ray(directionRay.endPoint(time), directionRay.v, directionRay.speed);
    }

    public Ray getRay() {
        return directionRay;
    }

    public void setRay(Ray ray) {
        this.directionRay = ray;
    }
    
    public int returnX(){
        return x;
    }
    
    public int returnY(){
        return y;
    }
    
    public int returnRadius(){
        return asteroidRadius;
    }

}
