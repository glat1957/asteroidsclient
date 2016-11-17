package physics;

import java.io.Serializable;


public class Ray implements Serializable{
  public Point origin;
  public Vector v;
  public double speed;
  
  public Ray(Point origin,Vector v,double speed)
  {
      this.origin = origin;
      this.v = v;
      this.v.normalize();
      this.speed = speed;
  }

  public Line toSegment(double time)
  {
      return new Line(origin,this.endPoint(time));
  }

  public Point endPoint(double time)
  {
      double destX = origin.x + v.dX*time*speed;
      double destY = origin.y + v.dY*time*speed;
      return new Point(destX,destY);
  }

  public double getTime(Point p)
  {
      if(Math.abs(v.dX) > Math.abs(v.dY))
          return (p.x - origin.x)/(v.dX*speed);
      return (p.y - origin.y)/(v.dY*speed);
  }
  
  public Point getOrigin(){
      return origin;
  }
}

