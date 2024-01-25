package code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Launcher Class for Pinball
 * @author tiffanyfu
 * May 2021
 */
public class Launcher 
{
    //variables
    private int x;
    private int y;
    private int initialY;
    private int width;
    private int length;
    // private double yVel;
    
    //constructor
    public Launcher()
    {
        x = 475;
        y = 705;
        initialY = 705;
        width= 50;
        length = 10;
        // yVel = 0.0;
    }
    
    //accessors
    public double getX() {return x;}
    public double getY() {return y;}
    public Rectangle getBounds() { return new Rectangle((int)x,(int)y, width, length); }
    
    //methods
    //animate (move down) when mouse clicked in started position
    public void lower(int yCoor) {
        if(yCoor < 795 && yCoor > initialY) {
            y = yCoor;
        }
    }

    //move up when mouse released
    public void shoot() {
        while(y > initialY) {
            y--;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x,y,width,length);
    }
}
