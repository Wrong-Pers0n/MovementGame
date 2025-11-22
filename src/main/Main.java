package main;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class Main {

    private static Renderer renderer;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    BufferedImage image;
    Boolean debug = true;
    long startTime;
    int currentLogicFps;
    static Player player;
    InputHandler input = new InputHandler(this);
    TestEntity test;
    public static ArrayList<RectangleCollider> colliders = new ArrayList<>();


    private void Update() {
        player.update();
        test.update();


    }





    public Main(Renderer renderer) {
        System.out.println("Initializing main...");
        this.renderer = renderer;
        renderer.main = this;


        image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object from the image
        Graphics2D g2d = image.createGraphics();

        // Set the color to blue and fill the square
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 25, 25);

        // Dispose the graphics object
        g2d.dispose();

        System.out.println("Main initialized");
    }

    public void start() {

        player = new Player(this, renderer);
        test = new TestEntity();
        RectangleCollider collider = new RectangleCollider(100, 100, 20, 160);
        colliders.add(collider);

        Timer renderTimer = new Timer();
        TimerTask renderTimerLoop = new TimerTask() {
            @Override
            public void run() {
                renderer.repaint();
            }
        };
        int framerate = 60;
        renderTimer.scheduleAtFixedRate(renderTimerLoop, 0, 1000 / framerate);

        Timer logicTimer = new Timer();
        TimerTask logicTimerLoop = new TimerTask() {
            @Override
            public void run() {
                if(debug) { startTime = System.nanoTime(); }

                Update();

                if(debug) {
                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;
                    duration = duration / 1000000; // Convert it to ms cuz fuck ns
                    try {
                        currentLogicFps = (int) Math.floor((double) 1000 / duration); // now converts it to fps
                    }catch(Exception e) {
                        currentLogicFps = -1;
                    }

                }
            }
        };
        int logicFramerate = 60;
        logicTimer.scheduleAtFixedRate(logicTimerLoop, 0, 1000 / logicFramerate);


    }


    public BufferedImage getImg() {
        return image;
    }


}