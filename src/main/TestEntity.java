package main;

import com.jogamp.opengl.GL2;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Main.player;

public class TestEntity implements  Entity{
    float x = 20;
    float y = 60;
    float angle = 270;
    BufferedImage image;
    float width = 20;
    float height = 20;
    float speed = 2;

    public TestEntity() {
        initializeTextures();
    }


    @Override
    public void render(GL2 gl) {
        Renderer.renderImage(gl, image, x, y, width, height);
    }
    @Override
    public void moveAtAngle(float speed, float angle) {
        double radians = Math.toRadians(angle);

        x += (float) (Math.cos(radians) * speed);
        y += (float) (Math.sin(radians) * speed);
    }

    @Override
    public void moveTo(float speed, float destinationX, float destinationY) {
        double angle = Math.atan2(destinationY - y, destinationX - x);
        x += (float) (Math.cos(angle) * speed);
        y += (float) (Math.sin(angle) * speed);
    }

    @Override
    public void update() {
        moveTo(speed, Player.x, Player.y);
    }

    @Override
    public void initializeTextures() {
        image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object from the image
        Graphics2D g2d = image.createGraphics();

        // Set the color to blue and fill the square
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, (int) width, (int) height);

        // Dispose the graphics object
        g2d.dispose();
    }

    @Override
    public void move(float angle) {

    }
}
