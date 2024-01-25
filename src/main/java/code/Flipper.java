package code;

import static code.Bumpers.rad;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Flipper Class for Pinball 
 * @author tiffanyfu
 * May 2021
 */
public class Flipper 
{
    //constants
    public static final int npoints = 4;
    
    //variables
    private int[] xpoints;
    private int[] ypoints;
    private int centerX; 
    private int centerY;
    private boolean left;
    private Line2D line;
   // private double ang;
    
    //constructor
    public Flipper(boolean leftInp)
    {
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        if(leftInp)
        {
            left=true;
        }
        centerX = 110;
        centerY = 700;
    }
    
    //accessors
    public boolean getSide() {return left;}
    
    //methods
    public void genPoints(double ang)
    {
        if(getSide())
        {
            xpoints[0]= ((int)((centerX)-(20*Math.cos(ang))));//left
            xpoints[1]= ((int)((centerX)-(20*Math.sin(ang)))); //top
            xpoints[2]= ((int)((centerX)+(100*Math.cos(ang)))); //right
            xpoints[3]= ((int)((centerX)+(20*Math.sin(ang)))); //bottom
            
            ypoints[0]= ((int)((centerY)+(20*Math.sin(ang)))); //left
            ypoints[1]= ((int)((centerY)-(20*Math.cos(ang)))); //top
            ypoints[2]= ((int)((centerY)-(100*Math.sin(ang)))); //right
            ypoints[3]= ((int)((centerY)+(20*Math.cos(ang)))); //bottom
            
            line = new Line2D.Float(xpoints[1],ypoints[1],xpoints[2],ypoints[2]); //top to right
        }
        else
        {
            double angle = 3.14-ang;
            xpoints[0]= 475-((int)((centerX)-(20*Math.cos(angle)))); //right
            xpoints[1]= 475-((int)((centerX)-(20*Math.sin(angle)))); //top
            xpoints[2]= 475-((int)((centerX)+(100*Math.cos(angle)))); //left
            xpoints[3]= 475-((int)((centerX)+(20*Math.sin(angle)))); //bottom
            
            ypoints[0]= ((int)((centerY)+(20*Math.sin(angle)))); //right
            ypoints[1]= ((int)((centerY)-(20*Math.cos(angle)))); //top
            ypoints[2]= ((int)((centerY)-(100*Math.sin(angle)))); //left
            ypoints[3]= ((int)((centerY)+(20*Math.cos(angle)))); //bottom
            
            line = new Line2D.Float(xpoints[1],ypoints[1],xpoints[2],ypoints[2]); //top to left
        }
    }    
    
    public void draw(Graphics g, double angle)
    {             
        g.setColor(Color.BLACK);
        g.fillPolygon(xpoints, ypoints, npoints);
        genPoints(angle);
    }
    
    public int touching(Marble marb)
    {
        Point2D center = marb.getCenter();
        if(line.ptLineDist(center)<=rad && line.intersects(marb.getBounds()))
        {
            return 100;
        }
        return 0;
    }
    
    public void checkCollision(Marble marb)
    {
        Point2D center = marb.getCenter();
        double XVel = marb.getXVel();
        double YVel = marb.getYVel();
        double Vi = Math.sqrt(XVel*XVel + YVel*YVel); //initial velocity
        
        if(line.ptLineDist(center)<=rad)// && line.intersects(marb.getBounds()))
        {
            double dx = line.getX2() - line.getX1();
            double dy = line.getY1() - line.getY2();
            double angI = Math.PI/2+Math.tan(XVel/YVel);
            double angW = Math.PI - Math.atan(dy/dx);
            double angR = 2*angW-angI;
            marb.setXVel(Math.cos(angR)*Vi);
            marb.setYVel(Math.sin(angR)*Vi);
        }
    }
}
