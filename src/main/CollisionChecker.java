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

    public Vector2 scale(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
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

class RectangleCollider {
    public Vector2 pos;  // top-left
    public Vector2 size; // width & height
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

class CollisionResult {
    public RectangleCollider collider; // object hit
    public Vector2 hitPosition;        // final position before collision

    public CollisionResult(RectangleCollider collider, Vector2 hitPosition) {
        this.collider = collider;
        this.hitPosition = hitPosition;
    }
}

public class CollisionChecker {

    /**
     * Moves a rectangle toward a destination, detecting collisions along each axis.
     *
     * @param hitbox    The moving rectangle
     * @param destX     Destination X
     * @param destY     Destination Y
     * @param colliders List of possible colliders
     * @return CollisionResult with first collider hit (if any) and stop position, or null if no collision
     */
    public static CollisionResult moveWithCollision(RectangleCollider hitbox, double destX, double destY,
                                                    ArrayList<RectangleCollider> colliders) {
        double moveX = destX - hitbox.pos.x;
        double moveY = destY - hitbox.pos.y;

        double finalX = hitbox.pos.x;
        double finalY = hitbox.pos.y;

        RectangleCollider firstCollider = null;

        // Step 1: Move along X
        if (moveX != 0) {
            double targetX = hitbox.pos.x + moveX;
            for (RectangleCollider other : colliders) {
                if (aabbIntersect(targetX, finalY, hitbox.size.x, hitbox.size.y,
                        other.pos.x, other.pos.y, other.size.x, other.size.y)) {
                    firstCollider = other;
                    if (moveX > 0) targetX = other.pos.x - hitbox.size.x;
                    else           targetX = other.pos.x + other.size.x;
                }
            }
            finalX = targetX;
        }

        // Step 2: Move along Y
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

        return firstCollider != null
                ? new CollisionResult(firstCollider, new Vector2(finalX, finalY))
                : new CollisionResult(null, new Vector2(finalX, finalY));
    }

    /**
     * Simple AABB collision check.
     */
    private static boolean aabbIntersect(double x1, double y1, double w1, double h1,
                                         double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 &&
                y1 < y2 + h2 && y1 + h1 > y2;
    }
}
