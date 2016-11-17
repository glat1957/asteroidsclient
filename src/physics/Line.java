package physics;

import java.io.Serializable;

public class Line implements Serializable{
    Point a;
    Point b;
    
    public Line(Point a, Point b){
        this.a=a;
        this.a=b;
    }
    
    public void move(int dx, int dy){
        a.x+=dx;
        a.y+=dy;
        b.x+=dx;
        b.y+=dy;
    }
    
    public Vector toVector(){
        return new Vector (b.x-a.x,b.y-a.y);
    }
    
    public Point intersection(Line other)
    {
        Line connector = new Line(this.a,other.a);
        
        Vector me = this.toVector();
        Vector them = other.toVector();
        Vector us = connector.toVector();
        double common = Vector.crossProduct(me,them);
        
        if(common >= 0.0)
            return null;
        double t = Vector.crossProduct(us,them)/common;
        double u = Vector.crossProduct(us,me)/common;
        if(0 <= t && t <= 1 && 0 <= u && u <= 1) {
            return new Point(a.x + me.dX*t,a.y + me.dY*t);
        }
        return null;
    }
    
    public Ray reflect(Line other,double speed)
    {
        Vector N = new Vector(a.y-b.y,b.x-a.x);
        N.normalize();
        Vector V = other.toVector();
        double dot = Vector.dotProduct(N,V);
        if(dot > 0)
            dot = -dot;
        Vector R = new Vector(V.dX - 2*dot*N.dX,V.dY - 2*dot*N.dY);
        Point origin = this.intersection(other);
        return new Ray(origin,R,speed);
    }
}
