package physics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
  
  public Ray(ObjectInputStream in) throws IOException{
      origin = new Point(in.readDouble(), in.readDouble());
      v = new Vector(in.readDouble(), in.readDouble());
      speed = in.readDouble();
  }
  
  public void writeTo(ObjectOutputStream out) throws IOException{
      out.writeDouble(origin.x);
      out.writeDouble(origin.y);
      out.writeDouble(v.dX);
      out.writeDouble(v.dY);
      out.writeDouble(speed);
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

