package main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static main.Initializer.scale;

public class Player implements Entity {

    public static Main main;
    public static Renderer renderer;
    public static float x = 10;
    public static float y = 0;
    public static float width = 20;
    public static float height = 20;
    public static int isDashing = 0;
    public BufferedImage image;
    public static boolean upPressed, downPressed, leftPressed, rightPressed = false;
    static float angle = 0;
    float moveSpeed = 3f;
    float dashSpeed = moveSpeed * 2.5f;
    int targetX, targetY;

    public Player(Main main, Renderer renderer) {
        Player.main = main;
        Player.renderer = renderer;
        initializeTextures();

    }

    public void update() {

        if(isDashing > 0) {
            moveTo(dashSpeed, targetX, targetY);
            isDashing--;
            return;
        }

        if(upPressed || downPressed || leftPressed || rightPressed) {

            if (upPressed && rightPressed) {
                angle = 315;
            } else if (upPressed && leftPressed) {
                angle = 225;
            } else if (downPressed && rightPressed) {
                angle = 45;
            } else if (downPressed && leftPressed) {
                angle = 135;
            } else if (upPressed) {
                angle = 270;
            } else if (downPressed) {
                angle = 90;
            } else if (rightPressed) {
                angle = 0;
            } else {
                angle = 180;
            }

            move(moveSpeed, angle);




        } else {
            return;
        }
    }

    @Override
    public void initializeTextures() {
        image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object from the image
        Graphics2D g2d = image.createGraphics();

        // Set the color to blue and fill the square
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, (int) width, (int) height);

        // Dispose the graphics object
        g2d.dispose();
    }

    @Override
    public void render(GL2 gl) {
        Renderer.renderImage(gl, image, x, y, width, height);
    }

    @Override
    public void move(float speed, float angle) {
        double radians = Math.toRadians(angle);

        x += (float) (Math.cos(radians) * speed);
        y += (float) (Math.sin(radians) * speed);
    }

    @Override
    public void moveTo(float speed, float destinationX, float destinationY) {
        double angle = Math.atan2(destinationY/scale - y, destinationX/scale - x);
        x += (float) (Math.cos(angle) * speed);
        y += (float) (Math.sin(angle) * speed);
    }
}
