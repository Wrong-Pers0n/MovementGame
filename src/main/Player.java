package main;

import com.jogamp.opengl.GL2;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Initializer.scale;
import static main.Main.*;

public class Player implements Entity {

    public static Main main;
    public static Renderer renderer;
    public BufferedImage image;
    RectangleCollider hitbox;

    static float moveSpeed = 3f;
    static float dashSpeed = moveSpeed * 3f;
    static float attackDashSpeed = 20f;
    public static float xMomentum = 0;
    public static float maxXMomentum = moveSpeed;
    public static float yMomentum = 0;
    public static float maxFallSpeed = moveSpeed*2f;
    static float angle = 0;
    public static int targetX, targetY;

    public static float x = 10;
    public static float y = 0;
    public static float width = 20;
    public static float height = 20;


    public static int isDashing = 0;
    public static boolean isAttackDashing = false;
    public static boolean onGround;
    public static boolean upPressed, downPressed, leftPressed, rightPressed = false;

    public Player(Main main, Renderer renderer) {
        Player.main = main;
        Player.renderer = renderer;
        initializeTextures();
        this.hitbox = new RectangleCollider(x,y,width,height);
    }

    public static void jump() {
        if(!onGround) return;
        yMomentum = -gravitySpeed*20f;
    }

    public void update() {

        if(!isAttackDashing) applyGravity();

        if(isAttackDashing) {
            if(UtilityTool.distanceTo(x, y, targetX/scale, targetY/scale) < 10) {
                isAttackDashing = false;
            }
            moveTo(attackDashSpeed, targetX,targetY);
            xMomentum = 0;
            yMomentum = 0;
        }
        else if(isDashing > 0) {
            moveAtAngle(dashSpeed, angle);
            isDashing--;
        }
        else if(leftPressed || rightPressed) {
            angle = leftPressed ? 180 : 0;
            if(Math.abs(xMomentum) < maxXMomentum) xMomentum += 1f;
            else if(Math.abs(xMomentum) > maxXMomentum) xMomentum = maxXMomentum;
            move(angle);
        }
        else {
            if(xMomentum != 0) {
                float dir = xMomentum < 0 ? 1 : -1;
                xMomentum += 0.3f * dir;
                if(Math.abs(xMomentum) < 0.3f) xMomentum = 0;
                move(angle);
            }
        }
    }

    @Override
    public void initializeTextures() {
        image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, (int) width, (int) height);
        g2d.dispose();
    }

    @Override
    public void render(GL2 gl) {
        Renderer.renderImage(gl, image, x, y, width, height);
    }

    @Override
    public void move(float angle) {
        float dir = angle == 0 ? 1f : -1f;

        float destX = x + xMomentum*dir;

        if(checkForCollision(destX, y)) {
            xMomentum /= 2f;
            destX = x + xMomentum*dir;
            if(checkForCollision(destX, y)) return;
        }
        x = destX;
        hitbox.x = destX;
    }

    @Override
    public void moveAtAngle(float speed, float angle) {
        double radians = Math.toRadians(angle);

        float destX = x + (float) (Math.cos(radians) * speed);
        float destY = y + (float) (Math.sin(radians) * speed);

        int limit = 6;
        if(speed > moveSpeed) limit = 11;
        for(int i = 1; i < limit; i++) {
            if(checkForCollision(destX, destY)) {
                destX = x + (float) (Math.cos(radians) * speed / i);
                destY = y + (float) (Math.sin(radians) * speed / i);

                if(i == limit-2 && radians != 0) {
                    destX = x + (float) (Math.cos(radians) / Math.abs(Math.cos(radians)));
                    destY = y + (float) (Math.sin(radians) / Math.abs(Math.sin(radians)));
                }
                if(!checkForCollision(destX, destY)) { break; }
                if(i == limit-1) return;

            }
        }

        x = destX;
        y = destY;
        hitbox.x = destX;
        hitbox.y = destY;

    }

    public void applyGravity() {
        if(yMomentum < maxFallSpeed) yMomentum += gravitySpeed;
        float destY = y + yMomentum;


        if(checkForCollision(x, destY)) {

            yMomentum /= 2;

            destY = y + yMomentum;
            if(checkForCollision(x, destY)) {
                yMomentum = 0;
                onGround = true;
            } else {
                y = destY;
                hitbox.y = destY;
            }
        } else {
            onGround = false;
            y = destY;
            hitbox.y = destY;
        }
    }

    @Override
    public void moveTo(float speed, float destinationX, float destinationY) {
        double radians = Math.atan2(destinationY/scale - y, destinationX/scale - x);

        float destX = x + (float) (Math.cos(radians) * speed);
        float destY = y + (float) (Math.sin(radians) * speed);
        int limit = 6;
        if(speed > moveSpeed) limit = 11;
        for(int i = 1; i < limit; i++) {
            if(checkForCollision(destX, destY)) {
                destX = x + (float) (Math.cos(radians) * speed / i);
                destY = y + (float) (Math.sin(radians) * speed / i);

                if(i == limit-2 && radians != 0) {
                    destX = x + (float) (Math.cos(radians) / Math.abs(Math.cos(radians)));
                    destY = y + (float) (Math.sin(radians) / Math.abs(Math.sin(radians)));
                }
                if(!checkForCollision(destX, destY)) { break; }
                if(i == limit-1)  {
                    isAttackDashing = false;
                    return;
                }

            }
        }
        x = destX;
        y = destY;
        hitbox.x = destX;
        hitbox.y = destY;

    }

    public boolean checkForCollision(float destX, float destY) {
        CollisionResult result = CollisionChecker.moveWithCollision(hitbox, destX, destY, colliders);

        if (result.collider != null) {
            System.out.println("Collided with object at: " + result.collider.pos.x + ", " + result.collider.pos.y);
            System.out.println("Hitbox would stop at: " + result.hitPosition.x + ", " + result.hitPosition.y);
            System.out.println("X: " + x + ", Y:" + y);
            return true;
        } else {
            return false;
        }
    }
}
