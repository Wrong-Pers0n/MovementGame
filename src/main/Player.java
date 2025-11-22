package main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static main.Initializer.scale;
import static main.Main.colliders;

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
    public static int targetX, targetY;
    RectangleCollider hitbox;

    public Player(Main main, Renderer renderer) {
        Player.main = main;
        Player.renderer = renderer;
        initializeTextures();
        this.hitbox = new RectangleCollider(x,y,width,height);
        //colliders.add(hitbox);

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

        float destX = x + (float) (Math.cos(radians) * speed);
        float destY = y + (float) (Math.sin(radians) * speed);
        if(checkForCollision(destX, destY)) {
            destX = x + (float) (Math.cos(radians) * speed / 3);
            destY = y + (float) (Math.sin(radians) * speed / 3);
            if(checkForCollision(destX, destY)) { return; }
        }
        x = destX;
        y = destY;
        hitbox.x = destX;
        hitbox.y = destY;

    }

    @Override
    public void moveTo(float speed, float destinationX, float destinationY) {
        double radians = Math.atan2(destinationY/scale - y, destinationX/scale - x);


        float destX = x + (float) (Math.cos(radians) * speed);
        float destY = y + (float) (Math.sin(radians) * speed);
        if(checkForCollision(destX, destY)) {
            destX = x + (float) (Math.cos(radians) * speed / 3);
            destY = y + (float) (Math.sin(radians) * speed / 3);
            if(checkForCollision(destX, destY)) { return; }
        }
        x = destX;
        y = destY;
        hitbox.x = destX;
        hitbox.y = destY;


    }

    private boolean checkForCollision(float destX, float destY) {
        CollisionResult result = CollisionChecker.moveWithCollision(hitbox, destX, destY, colliders);

        if (result.collider != null) {
            //System.out.println("Collided with object at: " + result.collider.pos.x + ", " + result.collider.pos.y);
            //System.out.println("Hitbox would stop at: " + result.hitPosition.x + ", " + result.hitPosition.y);
            return true;
        } else {
            return false;
        }
    }
}
