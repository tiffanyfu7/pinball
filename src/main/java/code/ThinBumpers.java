/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author tiffanyfu
 */
public class ThinBumpers 
{
    private final int npoints = 8;
    private final double rad = 12.5;
    private final int leftX = 190;
    private final int rightX = 284;
    private final int y = 125;    
    private final int w = 15;
    private final int h = 100; 
    private Line2D[] lines;
    
    public ThinBumpers() {
        lines = new Line2D[npoints];
    }
    
    public void draw(Graphics g) {
        lines[0] = new Line2D.Double(leftX,y,leftX+w,y);
        lines[1] = new Line2D.Double(leftX+w,y,leftX+w,y+h);
        lines[2] = new Line2D.Double(leftX+w,y+h,leftX,y+h);
        lines[3] = new Line2D.Double(leftX,y+h,leftX,y);
        
        lines[4] = new Line2D.Double(rightX,y,rightX+w,y);
        lines[5] = new Line2D.Double(rightX+w,y,rightX+w,y+h);
        lines[6] = new Line2D.Double(rightX+w,y+h,rightX,y+h);
        lines[7] = new Line2D.Double(rightX,y+h,rightX,y);
        
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.GRAY);
        g.fillRect(leftX,y,w,h); //left rectangle
        g.fillRect(rightX,y,w,h); //right rectangle
        
        g.setColor(Color.BLACK); //outline in black
        for(Line2D x: lines) {
            g2.draw(x);
        }
    }
    
    public int touching(Marble marb) {
        Point2D center = marb.getCenter();
        
        if(lines[1].ptLineDist(center)<=rad && lines[1].intersects(marb.getBounds()) ||
                (lines[7].ptLineDist(center) <= rad && lines[7].intersects(marb.getBounds()))) {
            return 200;
        }
        
        if(lines[3].ptLineDist(center)<=rad && lines[3].intersects(marb.getBounds()) ||
            (lines[5].ptLineDist(center) <= rad && lines[5].intersects(marb.getBounds()))) {
            return 100;
        }
        
        return 0;
    }
    
    public void checkCollision(Marble marb)
    {
        Point2D center = marb.getCenter();
        
        for(int x = 0; x< lines.length ; x++) {
            if(lines[x].ptLineDist(center)<=rad && lines[x].intersects(marb.getBounds())) {
                if(x%2==0) { //collided with bumper sides
                    marb.setYVel(marb.getYVel()*-1);
                }
                else { //collided with bumper top or bottom, ADD BOOST OR NEGATE
                    marb.setXVel(marb.getXVel()*-1);
                }
            }
        }
    }
}
