package code;

/**
 * Class ArcadeRunner
 * Runs and animates subclasses of MotionPanel
 * 
 * @author Travis Rother 
 * @version 2-25-2008
 */
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import audio.AudioUtility;

public class PinballRunner {
    int FPS = 60; // Frames per second (animation speed)
    AnimationPanel world = new PinballDemo();

    // ==============================================================================
    // --- Typically you will never need to edit any of the code below this line.
    // ==============================================================================

    JFrame myFrame;

    public PinballRunner() {
        myFrame = new JFrame();
        myFrame.addWindowListener(new Closer());
        addFrameComponents();
        startAnimation();
        myFrame.setSize(world.getPreferredSize());
        myFrame.setVisible(true);
    }

    public void addFrameComponents() {
        myFrame.setTitle("Pinball!");
        myFrame.add(world);
    }

    public void startAnimation() {
        javax.swing.Timer t = new javax.swing.Timer(1000 / FPS, new ActionListener() {
            // coding a method within the ActionListener object during it's construction!
            public void actionPerformed(ActionEvent e) {
                myFrame.getComponent(0).repaint();
                myFrame.setSize(myFrame.getComponent(0).getPreferredSize());
            }
        });
        t.start();
    }

    Clip themeMusic;

    public void initMusic() {
        themeMusic = AudioUtility.loadClip("src/main/java/audio/pinballMusic.wav");
        if (themeMusic != null) {
            themeMusic.loop(20); //shouldn't be playing for more than 10 minutes
        }
    }

    public static void main(String[] args) {
        PinballRunner runner = new PinballRunner();
        runner.startAnimation();
        runner.initMusic();
    }

    private static class Closer extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent e) {
            System.exit(0);
        }
    }
}
