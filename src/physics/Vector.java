package physics;

import java.io.Serializable;

public class Vector implements Serializable{

    public double dX;
    public double dY;

    public Vector(double dX, double dY) {
        this.dX = dX;
        this.dY = dY;
    }

    public void normalize() {
        double length = this.length();
        dX /= length;
        dY /= length;
    }

    public double length() {
        return Math.sqrt(dotProduct(this, this));
    }

    static public double crossProduct(Vector one, Vector two) {
        return one.dX * two.dY - two.dX * one.dY;
    }

    static public double dotProduct(Vector one, Vector two) {
        return one.dX * two.dX + one.dY * two.dY;
    }
}
