package main;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;

public class Initializer extends JPanel {

    public static float scale;
    public static final float originalWidth  = 640;
    public static final float originalHeight = 360;

    static void main (String args[]) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Starting initialization...");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            float windowWidth    = (float) screenSize.getWidth();
            float windowHeight   = (float) screenSize.getHeight();

            scale = windowHeight / originalHeight;
            System.out.println("Window size calculated");

            Renderer renderer = new Renderer("Test engine", (int) Math.ceil(windowWidth), (int) Math.ceil(originalHeight*scale), scale);
            Main main = new Main(renderer);
            System.out.println("Successfully initialized engine!");

            System.out.println("Starting game loop...");
            main.start();
            System.out.println("Game loop started");
        });
    }



}
