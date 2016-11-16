package asteroids;

import physics.*;

public class Bullet {

    private final double bulletRadius;
    private Ray directionRay;

    public Bullet(double startX, double startY, double radius, double dX, double dY) {
        this.bulletRadius = radius;

        Vector v = new Vector(dX, dY);
        double speed = 10;
        directionRay = new Ray(new Point(startX, startY), v, speed);
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
