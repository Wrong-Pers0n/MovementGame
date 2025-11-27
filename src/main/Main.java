package main;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class Main {

    private static Renderer renderer;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static UtilityTool uTool = new UtilityTool();
    BufferedImage image;
    Boolean debug = true;
    long startTime;
    double currentLogicFps;
    static Player player;
    InputHandler input = new InputHandler(this);
    TestEntity test;
    public static ArrayList<RectangleCollider> colliders = new ArrayList<>();
    int fps = 0;
    long end = 0;

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
        for(int i = 1; i < 1; i++) {
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
                    long duration = endTime - startTime;

                    end += duration; // Convert it to ms cuz fuck ns
                    //System.out.println(end);
                }
            }
        };
        int logicFramerate = 60;
        logicTimer.scheduleAtFixedRate(logicTimerLoop, 0, 1000 / logicFramerate);


        Timer debugTimer = new Timer();
        TimerTask debugTimerLoop = new TimerTask() {
            @Override
            public void run() {
                //System.out.println(fps);
                try {
                    currentLogicFps = 1000 / (end / 1000000.0 /60.0); // now converts it to fps
                }catch(Exception e) {
                    //currentLogicFps = -1;
                }

                fps = (int) (currentLogicFps);

                System.out.println("Current FPS: "+fps+", Average time to compute frame: "+Math.round(end / 1000000.0 /60.0 * 1e7) / 1e7+"ms");
                currentLogicFps = 0;
                end = 0;
            }
        };
        debugTimer.scheduleAtFixedRate(debugTimerLoop, 0, 1000);


    }


}