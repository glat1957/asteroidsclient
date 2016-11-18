package asteroids;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import physics.*;

public class Bullet {

    private final double bulletRadius;
    private Ray directionRay;

    public Bullet(double startX, double startY, double radius, int spe, double dX, double dY) {
        this.bulletRadius = radius;

        Vector v = new Vector(dX, dY);
        double speed = spe;
        directionRay = new Ray(new Point(startX, startY), v, speed);
    }
    
    public Bullet(ObjectInputStream in) throws IOException{
        bulletRadius = in.readDouble();
        directionRay = new Ray(in);
    }
    
    public void writeTo(ObjectOutputStream out) throws IOException{
        out.writeDouble(bulletRadius);
        directionRay.writeTo(out);
    }

    public Ray getRay() {
        return directionRay;
    }

    public void setRay(Ray r) {
        this.directionRay = r;
    }

    public void move(double time) {
        directionRay = new Ray(directionRay.endPoint(time), directionRay.v, directionRay.speed);
    }
    
    public double getRadius(){
        return bulletRadius;
    }

}
