package code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 * Bumper Class for Pinball
 * @author tiffanyfu7
 * May 2021
 */

public class Bumpers 
{
    public static final int npoints = 3;
    public static final double rad = 12.5;
            
    private int[] xpoints;
    private int[] ypoints;
    private int[] x2points;
    private int[] y2points;
    private int[] x3points;
    private int[] y3points;
    private Line2D line1; 
    private Line2D line2;
    private Line2D line3;
    private Line2D line4;
    private Line2D line5;
    private Line2D line6;
    private Line2D line7;
    private Line2D line8;
    private Line2D line9;
    private Line2D line10;
    
    public Bumpers()
    {
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        x2points = new int[npoints];
        y2points = new int[npoints];
        x3points = new int[npoints+1];
        y3points = new int[npoints+1];
    }
    
    public void draw(Graphics g)
    {             
        xpoints[0]= 50; // bottom
        xpoints[1]= 125; // right
        xpoints[2]= 50; //top
            
        ypoints[0]= 532;//575-43; //bottom
        ypoints[1]= 575; // right
        ypoints[2]= 445;//575-130; //top

        line1 = new Line2D.Float(xpoints[0],ypoints[0],xpoints[1],ypoints[1]);//bottom to right
        line2 = new Line2D.Float(xpoints[1],ypoints[1],xpoints[2],ypoints[2]);//right to top \
        line3 = new Line2D.Float(xpoints[0],ypoints[0],xpoints[2],ypoints[2]); //top to bottom |
        
        x2points[0]= 425; // bottom
        x2points[1]= 350; //left
        x2points[2]= 425; //top
            
        y2points[0]= 532;//575-43; //bottom
        y2points[1]= 575; //left
        y2points[2]= 445;//575-130; //top
        
        line4 = new Line2D.Float(x2points[0],y2points[0],x2points[1],y2points[1]); //bottom to left
        line5 = new Line2D.Float(x2points[1],y2points[1],x2points[2],y2points[2]); //left to top /
        line6 = new Line2D.Float(x2points[2],y2points[2],x2points[0],y2points[0]); //top to bottom
        
        x3points[0]= 187; //left
        x3points[1]= 237; //top
        x3points[2]= 287; //right
        x3points[3]= 237; //bottom
            
        y3points[0]= 370; //left 
        y3points[1]= 345; //top
        y3points[2]= 370; //right
        y3points[3]= 395; //bottom
        
        line7 = new Line2D.Float(x3points[0],y3points[0],x3points[1],y3points[1]); //left to top
        line8 = new Line2D.Float(x3points[1],y3points[1],x3points[2],y3points[2]); //top to right
        line9 = new Line2D.Float(x3points[2],y3points[2],x3points[3],y3points[3]); //right to bottom
        line10 = new Line2D.Float(x3points[3],y3points[3],x3points[0],y3points[0]); //bottom to left
        
        g.setColor(Color.GRAY);
        g.fillPolygon(xpoints, ypoints, npoints);
        g.fillPolygon(x2points, y2points, npoints);
        g.fillPolygon(x3points, y3points, npoints+1);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g2.draw(line1); g2.draw(line2); g2.draw(line3); g2.draw(line4); g2.draw(line5); 
        g2.draw(line6); g2.draw(line7); g2.draw(line8); g2.draw(line9); g2.draw(line10);
    }
    
    public int touching(Marble marb)
    {
        Point2D center = marb.getCenter();

        if(line1.ptLineDist(center)<=rad && line1.intersects(marb.getBounds()) ||
            (line3.ptLineDist(center)<=rad && line3.intersects(marb.getBounds())) ||
            (line6.ptLineDist(center)<=rad && line6.intersects(marb.getBounds())) ||
            (line4.ptLineDist(center)<=rad && line4.intersects(marb.getBounds())) ||
            (line7.ptLineDist(center)<=rad && line7.intersects(marb.getBounds())) ||
            (line8.ptLineDist(center)<=rad && line8.intersects(marb.getBounds())) ||
            (line9.ptLineDist(center)<=rad && line9.intersects(marb.getBounds())) ||
                (line10.ptLineDist(center) <= rad && line10.intersects(marb.getBounds()))) {
            return 25;
        }
        
        else if(line2.ptLineDist(center)<=rad && line2.intersects(marb.getBounds()) ||
            (line5.ptLineDist(center)<=rad && line5.intersects(marb.getBounds()))) {
            return 50;
        }
        return 0;
    }
    
    public void checkCollisions(Marble marb)
    {
        Point2D center = marb.getCenter();
        double XVel = marb.getXVel();
        double YVel = marb.getYVel();
        double Vi = Math.sqrt(XVel*XVel + YVel*YVel); //initial velocity
        
        if(line1.ptLineDist(center)<=rad && line1.intersects(marb.getBounds()) ||
                line10.ptLineDist(center)<=rad && line10.intersects(marb.getBounds())) {
            double angI = Math.PI/2 +Math.atan(XVel/YVel);
            double angW = 5*Math.PI/6;
            double angF = 2*angW-angI;
            marb.setXVel(Math.cos(angF)*Vi);
            marb.setYVel(Math.sin(angF)*Vi*-1);
        }
        if(line2.ptLineDist(center)<=rad && line2.intersects(marb.getBounds())) {
            double angI = 3*Math.PI/2+Math.atan(XVel/YVel);
            double angW = 10*Math.PI/6;
            double angR = 2*angW-angI;
            marb.setXVel(Math.cos(angR)*Vi);
            marb.setYVel(Math.sin(angR)*Vi);
        }
        if(line4.ptLineDist(center)<=rad && line4.intersects(marb.getBounds()) ||
                line9.ptLineDist(center)<=rad && line9.intersects(marb.getBounds())) {
            double angI = Math.PI/2 +Math.atan(XVel/YVel);
            double angW = 7* Math.PI/6;
            double angR = 2*angW-angI;
            marb.setXVel(Math.cos(angR)*Vi);
            marb.setYVel(Math.sin(angR)*Vi*-1);
        }
        if(line5.ptLineDist(center)<=rad && line5.intersects(marb.getBounds())) {
            double angI = Math.PI/2+Math.atan(XVel/YVel); 
            double angW = 4*Math.PI/3;
            double angR = 2*angW-angI;
            marb.setXVel(Math.cos(angR)*Vi*-1);
            marb.setYVel(Math.sin(angR)*Vi*-1);
        }
        if((line3.ptLineDist(center)<=rad && line3.intersects(marb.getBounds())) ||
                (line6.ptLineDist(center) <= rad && line6.intersects(marb.getBounds()))) {
            marb.setXVel(-1 * marb.getXVel());
        }
        
        if (line7.ptLineDist(center) <= rad && line7.intersects(marb.getBounds())) {
            double angI = Math.PI / 2 + Math.atan(XVel / YVel);
            double angW = 7 * Math.PI / 6;
            double angR = 2 * angW - angI;
            marb.setXVel(Math.cos(angR) * Vi);
            marb.setYVel(Math.sin(angR) * Vi);
        }
        
        if(line8.ptLineDist(center)<=rad && line8.intersects(marb.getBounds())) {
            double angI =Math.PI/2+Math.atan(XVel/YVel);
            double angW = 5*Math.PI/6;
            double angR = 2*angW-angI;
            marb.setXVel(Math.cos(angR)*Vi);
            marb.setYVel(Math.sin(angR)*Vi);
        }
    }
}
