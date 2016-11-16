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
        x = random.nextInt(700) + 1;
        y = random.nextInt(500) + 1;
        asteroidRadius = 20;
        dX = random.nextInt(20) - 10;
        dY = random.nextInt(20) - 10;

        Vector velocity = new Vector(dX, dY);
        double speed = 1;
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
