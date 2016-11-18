package asteroids;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import physics.*;
import java.util.Random;

public class Asteroid {

    Random random = new Random();
    private int asteroidRadius;
    private Ray directionRay;
    Asteroid temp = null;

    public void move(double time) {
        directionRay = new Ray(directionRay.endPoint(time), directionRay.v, directionRay.speed);
    }
    
    public Asteroid(ObjectInputStream in) throws IOException{
        asteroidRadius = in.readInt();
        directionRay = new Ray(in);
    }
    
    public void writeTo(ObjectOutputStream out) throws IOException{
        out.writeInt(asteroidRadius);
        directionRay.writeTo(out);
    }

    public Ray getRay() {
        return directionRay;
    }

    public void setRay(Ray ray) {
        this.directionRay = ray;
    }
    
    public int returnX(){
        return (int) directionRay.origin.x;
    }
    
    public int returnY(){
        return (int) directionRay.origin.y;
    }
    
    public int returnRadius(){
        return asteroidRadius;
    }

}
