package asteroids;

import physics.*;

public class Bullet {

    private Point bulletCenter;
    private int bulletRadius;
    private Ray r;

    public Bullet(double startX, double startY, int radius, int dX, int dY) {
        this.bulletCenter.x = startX;
        this.bulletCenter.y = startY;
        this.bulletRadius = radius;

        Vector v = new Vector(dX, dY);
        double speed = v.length();
        r = new Ray(new Point(startX, startY), v, speed);
    }

    public Ray getRay() {
        return r;
    }

    public void setRay(Ray r) {
        this.r = r;
    }

    public void move(double time) {
        r = new Ray(r.endPoint(time), r.v, r.speed);
    }

}
