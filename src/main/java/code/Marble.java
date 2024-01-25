package code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

/**
 * Marble Class for Pinball
 * @author tiffanyfu
 * May 2021
 */
public class Marble
{
    public static final int gravity = 2;
    
    //variables
    private double x;
    private double y;
    private double xVel;
    private double yVel;
    private int diameter;

    //constructor
    public Marble()
    {
        //initial position on launcher
        x = 475;
        y = 680;
        xVel = 0;
        yVel = 0;
        diameter = 25;
    }

    //accesors
    public double getX() {return x;}
    public double getY() {return y;}
    public double getXVel() {return xVel;}
    public double getYVel() {return yVel;}
    public Point2D getCenter() {return new Point2D.Float((int)x+diameter/2,(int)y+diameter/2);}
    public double getCentX() {return x+diameter/2;}
    public double getCentY() {return y+diameter/2;}
    public int getSize() {return diameter; }
    public Rectangle getBounds() { return new Rectangle((int)x,(int)y,diameter,diameter); }
    
    //modifiers
    public void setX(double in) {x=in;}
    public void setY(double in) {y=in;}
    public void setXVel(double in) {xVel=in;}
    public void setYVel(double in) {yVel=in;}
    
    //other methods
    public void draw(Graphics g)
    {
        setX(getX()+getXVel());
        setY(getY()+getYVel());
        g.setColor(Color.lightGray);
        g.fillOval((int)x, (int)y, diameter, diameter);
    }     
    
    //lowers marble with launcher
    public void lower(Launcher l)
    {
        y = l.getY() - 25;
    }
    
    //follow arc 
       //factor in potential energy... currently follows constant path
    public void followCurve(double angC)
    {
        double dy = Math.sin(angC)*(237.5-angC*25);
        double dx = Math.cos(angC)*(237.5-angC*25);
        x = 250+dx;
        y = 250-dy;        
    }     
    
    //checks collisions with edge of screen
    public void checkWallCollisions()
    {
        if(y>=250 && y<=624) {
            if(x<=0) {
                x=0;
                xVel*=-1;
            }
            if(x>=450) {
                x=450;
                xVel*=-1;
            }
        }
    }
}