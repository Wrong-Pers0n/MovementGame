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
    public static UtilityTool uTool = new UtilityTool();
    BufferedImage image;
    Boolean debug = false;
    long startTime;
    int currentLogicFps;
    static Player player;
    InputHandler input = new InputHandler(this);
    TestEntity test;
    public static ArrayList<RectangleCollider> colliders = new ArrayList<>();

    public static final float gravitySpeed = 0.5f;


    private void Update() {
        player.update();
        test.update();


    }





    public Main(Renderer renderer) {
        System.out.println("Initializing main...");
        this.renderer = renderer;
        renderer.main = this;


        System.out.println("Main initialized");
    }

    public void start() {

        player = new Player(this, renderer);
        test = new TestEntity();
        RectangleCollider collider = new RectangleCollider(100, 350, 200, 30);
        RectangleCollider collider2 = new RectangleCollider(300, 330, 200, 20);
        colliders.add(collider);
        colliders.add(collider2);
        for(int i = 1; i < 100; i++) {
            colliders.add(new RectangleCollider(-25*i, 900, 20, 20));
        }

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
                    double duration = endTime - startTime;

                    duration = duration / 1000000; // Convert it to ms cuz fuck ns
                    //System.out.println(duration);
                    try {
                        currentLogicFps = (int) Math.floor((double) 1000 / duration); // now converts it to fps
                    }catch(Exception e) {
                        currentLogicFps = -1;
                    }
                    System.out.println(currentLogicFps);

                }
            }
        };
        int logicFramerate = 60;
        logicTimer.scheduleAtFixedRate(logicTimerLoop, 0, 1000 / logicFramerate);


    }


}