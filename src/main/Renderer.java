package main;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.logging.*;

import static main.Initializer.scale;
import static main.Main.colliders;
import static main.Main.player;

public class Renderer {
    static public GLJPanel glPanel = null;
    static Main main;
    public static float globalXOffset = 0;
    public static float globalYOffset = 0;

    //private static final Logger logger = Logger.getLogger(Renderer.class.getName());


    private static void Render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1f); // black background
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //renderImage(gl, main.getImg(), 0, 0, 50, 50);
        player.render(gl);
        main.test.render(gl);

        for(RectangleCollider collider : colliders) {
            collider.render(gl);
        }
    }



    private static void init(String title, int width, int height) {

        System.out.println("Initializing renderer...");

            JFrame frame = new JFrame(title);
            frame.setSize(width, height);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setVisible(true);

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities caps = new GLCapabilities(glProfile);
            glPanel = new GLJPanel(caps);
            frame.add(glPanel);
        glPanel.setFocusable(true);
        glPanel.requestFocusInWindow();


        glPanel.addGLEventListener(new GLEventListener() {
                @Override
                public void init(GLAutoDrawable drawable) {
                    System.out.println("OpenGL initialized!");
                    System.out.println("Renderer: " + drawable.getGL().glGetString(GL2.GL_RENDERER));
                    System.out.println("Version: " + drawable.getGL().glGetString(GL2.GL_VERSION));
                }

                @Override
                public void dispose(GLAutoDrawable drawable) {
                    System.out.println("OpenGL context disposed");
                }

                @Override
                public void display(GLAutoDrawable drawable) {
                   Render(drawable);
                }


                @Override
                public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                    GL2 gl = drawable.getGL().getGL2();
                    gl.glViewport(0, 0, width, height);

                    gl.glMatrixMode(GL2.GL_PROJECTION);
                    gl.glLoadIdentity();
                    gl.glOrtho(0, width, height, 0, -1, 1); // top-left is (0,0)
                    gl.glMatrixMode(GL2.GL_MODELVIEW);
                    gl.glLoadIdentity();
                }

            });

            if (SwingUtilities.isEventDispatchThread()) {
                System.out.println("Rendering on Swing EDT");
            } else {
                System.err.println("Not on EDT!");
            }
            System.out.println("Renderer initialized!");
    }

    public void repaint() {
        if (glPanel != null) {
            glPanel.repaint();
        }
    }


    public static void renderImage(GL gl, BufferedImage image, float x, float y, float width, float height, float opacity) {
        if (image == null) return;

        GL2 gl2 = gl.getGL2();
        Texture texture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true);

        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        texture.enable(gl2);
        texture.bind(gl2);

        gl2.glColor4f(1f, 1f, 1f, opacity);

        gl2.glBegin(GL2.GL_QUADS);
        gl2.glTexCoord2f(0f, 0f); gl2.glVertex2f((x - globalXOffset)*scale, (y - globalYOffset)*scale);
        gl2.glTexCoord2f(1f, 0f); gl2.glVertex2f((x - globalXOffset + width)*scale, (y - globalYOffset)*scale);
        gl2.glTexCoord2f(1f, 1f); gl2.glVertex2f((x - globalXOffset + width)*scale, (y - globalYOffset + height)*scale);
        gl2.glTexCoord2f(0f, 1f); gl2.glVertex2f((x - globalXOffset)*scale, (y - globalYOffset + height)*scale);
        gl2.glEnd();

        texture.disable(gl2);
        gl2.glDisable(GL2.GL_BLEND);
        gl2.glDisable(GL2.GL_TEXTURE_2D);
    }

    public static void renderImage(GL gl, BufferedImage image, float x, float y, float width, float height) {
        if (image == null || Math.abs(x - globalXOffset) > Initializer.originalWidth*1.2 || Math.abs(y - globalYOffset) > Initializer.originalHeight*1.2) return;

        GL2 gl2 = gl.getGL2();
        Texture texture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true);

        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        texture.enable(gl2);
        texture.bind(gl2);

        gl2.glBegin(GL2.GL_QUADS);
        gl2.glTexCoord2f(0f, 0f); gl2.glVertex2f((x - globalXOffset)*scale, (y - globalYOffset)*scale);
        gl2.glTexCoord2f(1f, 0f); gl2.glVertex2f((x - globalXOffset + width)*scale, (y - globalYOffset)*scale);
        gl2.glTexCoord2f(1f, 1f); gl2.glVertex2f((x - globalXOffset + width)*scale, (y - globalYOffset + height)*scale);
        gl2.glTexCoord2f(0f, 1f); gl2.glVertex2f((x - globalXOffset)*scale, (y - globalYOffset + height)*scale);
        gl2.glEnd();

        texture.disable(gl2);
        gl2.glDisable(GL2.GL_BLEND);
        gl2.glDisable(GL2.GL_TEXTURE_2D);
    }

    public Renderer(String title, int width, int height, float scale) {
        init(title, width, height);
    }
}
