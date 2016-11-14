package asteroids;

import java.io.Serializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import asteroids.*;

public class Bullet implements Serializable {
    
    private Ray r;
    private Circle c;
    
    public Bullet(int startX,int startY,int dX,int dY){
        Vector v = new Vector(dX,dY);
        double speed = v.length();
        r = new Ray(new Point(startX,startY),v,speed);
    }
    
    public Ray getRay(){
        return r;
       
    }
    
    public void setRay(Ray r)
    {
        this.r = r;
    }
    
    public void move(double time)
    {
        r = new Ray(r.endPoint(time),r.v,r.speed);
    }
    
    public Shape getShape()
    {
        c = new Circle(r.origin.x,r.origin.y,4);
        c.setFill(Color.RED);
        return c;
    }
    
    public void updateShape()
    {
        c.setCenterX(r.origin.x);
        c.setCenterY(r.origin.y);
    }
}

