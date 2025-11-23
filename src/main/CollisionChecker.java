package main;

import com.jogamp.opengl.GL2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Vector2 {
    public double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 scale(double scale) {
        return new Vector2(this.x * scale, this.y * scale);
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2 normalize() {
        double len = length();
        if (len == 0) return new Vector2(0, 0);
        return new Vector2(x / len, y / len);
    }
}

// basically any collision thing, like, for example, the floor or the wall
class RectangleCollider {
    public Vector2 pos;  // top left position
    public Vector2 size; // width and height
    BufferedImage image;
    double x, y, width, height;

    public RectangleCollider(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        pos = new Vector2(x, y);
        size = new Vector2(width, height);

        image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(0, 0, (int) width, (int) height);
        g2d.dispose();
    }

    public void render(GL2 gl) {
        Renderer.renderImage(gl, image, (float) x, (float) y, (float) width, (float) height);
    }
}

// fancy return option
class CollisionResult {
    public RectangleCollider collider; // the collider we hit
    public Vector2 hitPosition;        // the position of the moving this right before it hits the collider

    public CollisionResult(RectangleCollider collider, Vector2 hitPosition) {
        this.collider = collider;
        this.hitPosition = hitPosition;
    }
}

public class CollisionChecker {

    public static CollisionResult moveWithCollision(RectangleCollider hitbox, double destX, double destY, ArrayList<RectangleCollider> colliders) {
        // finds how far it has to move in x and y
        double moveX = destX - hitbox.pos.x;
        double moveY = destY - hitbox.pos.y;
        double finalX = hitbox.pos.x;
        double finalY = hitbox.pos.y;

        RectangleCollider firstCollider = null;

        // Check along the x axis
        if (moveX != 0) {
            double targetX = hitbox.pos.x + moveX;
            for (RectangleCollider other : colliders) {
                if (aabbIntersect(targetX, finalY, hitbox.size.x, hitbox.size.y, other.pos.x, other.pos.y, other.size.x, other.size.y)) {
                    firstCollider = other;
                    if (moveX > 0) targetX = other.pos.x - hitbox.size.x;
                    else           targetX = other.pos.x + other.size.x;
                }
            }
            finalX = targetX;
        }

        // check along the y axis
        if (moveY != 0) {
            double targetY = hitbox.pos.y + moveY;
            for (RectangleCollider other : colliders) {
                if (aabbIntersect(finalX, targetY, hitbox.size.x, hitbox.size.y,
                        other.pos.x, other.pos.y, other.size.x, other.size.y)) {
                    firstCollider = other;
                    if (moveY > 0) targetY = other.pos.y - hitbox.size.y;
                    else           targetY = other.pos.y + other.size.y;
                }
            }
            finalY = targetY;
        }

        return firstCollider != null ? new CollisionResult(firstCollider, new Vector2(finalX, finalY)) : new CollisionResult(null, new Vector2(finalX, finalY));
    }

    // some sort of magic idk
    private static boolean aabbIntersect(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
}
