package asteroids;

import physics.*;

public class Asteroid {

    private int dX;
    private int dY;
    private int asteroidRadius;
    private Ray directionRay;

    public void Asteroid(double x, double y, int radius, int dX, int dY) {
        this.asteroidRadius = radius;

        Vector velocity = new Vector(dX, dY);
        double speed = velocity.length();
        directionRay = new Ray(new Point(x, y), velocity, speed);
    }

    public boolean isHit(Point bulletCenter, int bulletRadius) {
        // Since asteroids and bullets are going to be represented using circles,
        // we can use the origin point and radius of each and the distance formula
        // to determine if they overlap.
        double distance = Math.pow((directionRay.getOrigin().x - bulletCenter.x) * (directionRay.getOrigin().x - bulletCenter.x)
                + (directionRay.getOrigin().y - bulletCenter.y) * (directionRay.getOrigin().y - bulletCenter.y), 0.5);

        if (distance < asteroidRadius + bulletRadius) {
            return true;
        } else if (distance > asteroidRadius + bulletRadius) {
            return false;
        } else {
            return true;
        }
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
}
