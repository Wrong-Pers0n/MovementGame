package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static main.Initializer.scale;
import static main.Main.player;
import static main.Main.uTool;
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
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true),  "space-released");


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
                if(Player.isDashing == 0) Player.isDashing = 15;
            }
        });
        am.put("space-pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.jump();
            }
        });

        registerAllCases(im);
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

        if(Player.isAttackDashing || uTool.distanceTo(Player.x, Player.y,x/scale - Player.width/2f,y/scale - Player.height/2f) < 125) return;

        Player.isAttackDashing = true;
        Player.isDashing = 0;
        Player.targetX = (int) (x - Player.width/2f);
        Player.targetY = (int) (y - Player.height/2f);
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
    private void registerAllCases(InputMap im) {
        KeyStroke[] keys = im.allKeys();

        if (keys != null) {
            for (KeyStroke ks : keys) {
                Object actionName = im.get(ks);

                // Only handle letter keys
                int keyCode = ks.getKeyCode();
                if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
                    KeyStroke shiftKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.SHIFT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(shiftKS, actionName);
                    KeyStroke ctrlKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.CTRL_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(ctrlKS, actionName);
                    KeyStroke altKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.ALT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(altKS, actionName);
                    KeyStroke ctrlShiftKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(ctrlShiftKS, actionName);
                    KeyStroke ctrlAltKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(ctrlAltKS, actionName);
                    KeyStroke shiftAltKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.SHIFT_DOWN_MASK | InputEvent.ALT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(shiftAltKS, actionName);
                    KeyStroke ctrlShiftAltKS = KeyStroke.getKeyStroke(keyCode, ks.getModifiers() | InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK | InputEvent.ALT_DOWN_MASK, ks.isOnKeyRelease());
                    im.put(ctrlShiftAltKS, actionName);
                }
            }
        }
    }
}
