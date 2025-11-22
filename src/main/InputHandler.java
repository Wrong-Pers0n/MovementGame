package main;

import javax.swing.*;
import java.awt.event.*;

import static main.Main.player;
import static main.Renderer.glPanel;


public class InputHandler implements ActionListener, MouseListener, KeyListener {

    Main main;
    Initializer initializer = new Initializer();
    InputHandler(Main main) {
        this.main = main;

        InputMap im = glPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = glPanel.getActionMap();
        glPanel.addMouseListener(this);

        // All inputs that are used:
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "up-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true),  "up-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "down-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true),  "down-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "left-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true),  "left-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "right-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true),  "right-released");
        im.put(KeyStroke.getKeyStroke("shift pressed SHIFT"), "shift-pressed");
        im.put(KeyStroke.getKeyStroke("shift released SHIFT"), "shift-released");



        // Declarations for what inputs do

        am.put("up-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.upPressed = true;
            }
        });
        am.put("up-released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.upPressed = false;
            }
        });
        am.put("down-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.downPressed = true;
            }
        });
        am.put("down-released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.downPressed = false;
            }
        });
        am.put("left-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.leftPressed = true;
            }
        });
        am.put("left-released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.leftPressed = false;
            }
        });
        am.put("right-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.rightPressed = true;
            }
        });
        am.put("right-released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.rightPressed = false;
            }
        });
        am.put("shift-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.isDashing = 15;
            }
        });
        am.put("shift-released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Player.rightPressed = false;
            }
        });

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        player.isDashing = 20;
        player.targetX = x;
        player.targetY = y;
        System.out.println("X: "+x+" Y:"+y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
