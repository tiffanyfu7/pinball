package code;

/**Author @tiffanyfu7
 * Class ArcadeDemo
 * This class contains demos of many of the things you might
 * want to use to make an animated arcade game.
 * 
 * Adapted from the AppletAE demo from years past. 
 * Used by Tiffany Fu for Pinball
 * May 2021
 */
import static code.Bumpers.rad;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class PinballDemo extends AnimationPanel {
    //Constants
    //-------------------------------------------------------
    public final double gravity = 0.15;

    //Instance Variables
    //-------------------------------------------------------
    Marble marble = new Marble();
    Launcher launcher = new Launcher();
    Flipper flipperL = new Flipper(true); //left flipper
    Flipper flipperR = new Flipper(false); //right flipper
    Bumpers bumpers = new Bumpers();
    ThinBumpers thBumpers = new ThinBumpers();
    Arc2D arc = new Arc2D.Double(0, 0, 500, 500, 0, 180, Arc2D.OPEN); //top arc
    Line2D line = new Line2D.Double(475, 800, 475, 250); //right wall
    Line2D lineL = new Line2D.Double(113, 680.4, 0, 624); //left bottom slant wall (0.2 rad)
    Line2D lineR = new Line2D.Double(362, 680.4, 475, 624); // right bottom slant wall
    Line2D lineC = new Line2D.Double(475, 250, 500, 250); //cover launch //possibly remove...
    public int SCORE = 0;
    private double angL = 0;
    private double angR = 3.14;
    private double angC = 0;
    private int MARB = 0;
    private int flipR = 0;
    private int flipL = 0;
    private int LIVES = 3;
    private int WAIT = 0;
    private int GAME_MODE = 0; //0:start screen  1:play  2:game over

    //Constructor
    //-------------------------------------------------------
    public PinballDemo() { //Enter the name and width and height.  
        super("Pinball", 500, 800);
    }

    public int getScore() {
        return SCORE;
    }

    public void setScore(int in) {
        SCORE = in;
    }

    //The renderFrame method is the one which is called each time a frame is drawn.
    //-------------------------------------------------------
    protected void renderFrame(Graphics g) {
        if (GAME_MODE == 0)
            renderStartScreen(g);
        else if (GAME_MODE == 1) 
            renderGameScreen(g);
        else if (GAME_MODE == 2)
            renderEndScreen(g);
    }

    protected void renderStartScreen(Graphics g) {
        g.setFont(new Font("Arial Black", Font.PLAIN, 115));
        g.drawString("Pinball!", 15, 300);

        g.setFont(new Font("Verdana", Font.PLAIN, 17));
        g.drawString("Click and drag to launch the marble", 20, 350);
        g.drawString("Use the F and J keys to move the left and right", 20, 390);
        g.drawString("flipper respectively", 20, 415);

        g.drawString("You have three lives! Score by hitting bumpers", 20, 455);
        g.drawString("and flipping!", 20, 480);

        g.setFont(new Font("Futura", Font.ITALIC, 30));
        g.drawString("Press S to start", 20, 540);
    }

    protected void renderGameScreen(Graphics g) {
        if (LIVES == 0)
            GAME_MODE = 2; //game over screen... leaderboard

        Graphics2D g2 = (Graphics2D) g;

        //different motion of marbles depending on different phases (see marblePhases())
        if (MARB == 0) {
            marble.setYVel(0);
        } //resting on launcher

        else {
            marble.setYVel(marble.getYVel() + gravity);
        }

        if (MARB == 4 && marble.getY() >= 750) //lose one life if marble reaches bottom
        {
            WAIT = frameNumber;
            MARB = 5;
        }
        if (WAIT != 0 && frameNumber < (WAIT + 60)) //pause for one sec before resetting
        {
            marble.setYVel(0);
            marble.setXVel(0);
            marble.setY(755);
        } else if (WAIT != 0) //reset, minus one life
        {
            MARB = 0;
            LIVES--;
            marble.setX(475);
            marble.setY(680);
            WAIT = 0;
            angC = 0;
        }

        marblePhases();

        //move and draw flippers
        moveFlips();
        flipperL.draw(g, angL);
        flipperR.draw(g, angR);

        //draw objects (initiated above)
        g2.draw(line);
        g2.draw(lineL);
        g2.draw(lineR);
        g2.draw(arc);
        launcher.draw(g);
        marble.draw(g);
        bumpers.draw(g);
        thBumpers.draw(g);
        if (MARB == 4) {
            g2.draw(lineC);
        } // cover launcher while marble in play

        //General Text (Draw this last to make sure it's on top.) 
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial Black", Font.PLAIN, 20));
        g.drawString("LIVES: ", 420, 30);
        g.drawString(String.valueOf(LIVES), 475, 50);
        g.drawString("SCORE: ", 12, 30);
        g.drawString(String.valueOf(SCORE), 12, 50);
    }

    protected void renderEndScreen(Graphics g) {
        g.setFont(new Font("Arial Black", Font.PLAIN, 115));
        g.drawString("GAME", 15, 300);
        g.drawString("OVER!", 15, 410);

        g.setFont(new Font("Verdana", Font.PLAIN, 17));
        g.drawString("Great Game!", 20, 460);
        g.drawString("Your Score was: " + String.valueOf(SCORE), 20, 490);

        g.setFont(new Font("Futura", Font.ITALIC, 30));
        g.drawString("Press R to play again!", 20, 540);
    }
    //--end of renderFrame method--

    //marble hits different stages of motion
    public void marblePhases() {
        if (MARB == 1 && marble.getY() <= 250) {
            MARB = 2;
        }
        if (MARB == 1) { // 1: launching phase 
            marble.setYVel(-20);
        }
        if (MARB == 2) { //2: following curve
            marble.followCurve(angC);
            angC += 0.1;
        }
        if (MARB == 2 && marble.getY() > 260) {
            MARB = 3;
        }
        if (MARB == 3) { //3: following down from curve
            marble.setYVel(15);
        }
        if (MARB == 3 && marble.getY() >= 400) {
            MARB = 4;
        }
        if (MARB == 4) {
            checkCollisions();
            updateScore();
        }
    }

    public void updateScore() {
        SCORE += flipperL.touching(marble) + flipperR.touching(marble) + bumpers.touching(marble)
                + thBumpers.touching(marble);
    }

    //check all marble collisions with all o/ objects
    public void checkCollisions() {
        flipperL.checkCollision(marble);
        flipperR.checkCollision(marble);
        marble.checkWallCollisions();
        bumpers.checkCollisions(marble);
        thBumpers.checkCollision(marble);
        marbOffWalls();
        if (marbTouchArc(marble))
            marbOffArc(marble);
    }

    //rotate flippers... DON'T REPEAT CODE
    public void moveFlips() {
        if (flipL == 1 && angL < 1)
            angL = angL + 0.2;
        else if (flipL == 1 && angL == 1)
            flipL = 2;
        else if (flipL == 2 && angL > 0)
            angL = angL - 0.2;
        
        if (flipR == 1 && angR > 2.14)
            angR = angR - 0.2;
        else if (flipR == 1 && angR < 2.14)
            flipR = 2;
        else if (flipR == 2 && angR < 3.34)
            angR = angR + 0.2;
    }

    //is the marble hitting the arc
    public boolean marbTouchArc(Marble marb) {
        double dy = 250 - marb.getCentY();
        double dx = 250 - marb.getCentX();
        double hyp = Math.hypot(dx, dy);
        if (marb.getY() <= 250 && hyp >= 237.5)
            return true;
        return false;
    }

    //bounce the marble off the arc
    public void marbOffArc(Marble marb) {
        //marble constants
        double XVel = marb.getXVel();
        double YVel = marb.getYVel();
        double dy = 250 - marb.getY();
        double dx = 250 - marb.getX();
        double Vi = Math.sqrt(XVel * XVel + YVel * YVel);

        if (marb.getCentX() <= 12.5) {
            marb.setXVel(marb.getXVel() * -1);
        } 
        else if (marb.getX() > 250) { 
            double angW = Math.PI / 2 - Math.atan(dy / dx); 
            double angI = Math.PI / 2 + Math.tan(XVel / YVel);
            double angF = 2 * angW - angI;
            marb.setXVel(Math.cos(angF) * Vi);
            marb.setYVel(Math.sin(angF) * Vi * -1);
        } 
        else if (marb.getX() < 250) {
            double angW = 3 * Math.PI / 2 - Math.atan(dy / dx);
            double angI = Math.PI / 2 + Math.tan(XVel / YVel);
            double angF = 2 * angW - angI;
            marb.setXVel(Math.cos(angF) * Vi);
            marb.setYVel(Math.sin(angF) * Vi * -1);
        } 
        else {
            marb.setYVel(marb.getYVel() * -1);
        }
    }

    //marble off other three walls
    public void marbOffWalls() {
        Point2D center = marble.getCenter();
        double XVel = marble.getXVel();
        double YVel = marble.getYVel();
        double Vi = Math.sqrt(XVel * XVel + YVel * YVel); //initial velocity

        if (lineL.ptLineDist(center) <= rad && lineL.intersects(marble.getBounds())) { // roll down slanted walls
            marble.setXVel(Math.cos(0.4) * Vi);
            marble.setYVel(Math.sin(0.4) * Vi);
        }
        if (lineR.ptLineDist(center) <= rad && lineR.intersects(marble.getBounds())) {
            marble.setXVel(Math.cos(0.4) * Vi * -1);
            marble.setYVel(Math.sin(0.4) * Vi);
        }
        if (lineC.ptLineDist(center) <= rad && lineC.intersects(marble.getBounds())) {
            marble.setYVel(marble.getYVel() * -1);
        }
    }

    //-------------------------------------------------------
    //Responses to Mouse Events
    //-------------------------------------------------------
    public void mouseDragged(MouseEvent e) {
        if (MARB == 0 && launcher.getBounds().contains(e.getPoint()))
        //pressed in region of launcher the launcher will animate
        {
            launcher.lower(e.getY());
            marble.lower(launcher);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (MARB == 0 && e.getY() > 705) { //705 is initial launch, don't launch marble unless launcher is dragged down
            launcher.shoot();
            MARB = 1;
        }
    }

    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'f' || c == 'F')
            flipL = 1;
        if (c == 'j' || c == 'J')
            flipR = 1;
        if (c == 's' || c == 'S') {
            GAME_MODE = 1;
            MARB = 0;
        }
        if (c == 'r' || c == 'R') {
            GAME_MODE = 0;
            SCORE = 0;
            LIVES = 3;
        }
    }
}
//--end of ArcadeDemo class--