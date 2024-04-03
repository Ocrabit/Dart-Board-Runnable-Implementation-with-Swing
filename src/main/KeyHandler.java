package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){  //VK_W is the keycode for W
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){  //VK_W is the keycode for W
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S){  //VK_W is the keycode for W
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){  //VK_W is the keycode for W
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){  //VK_W is the keycode for W
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){  //VK_W is the keycode for W
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){  //VK_W is the keycode for W
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){  //VK_W is the keycode for W
            rightPressed = false;
        }
    }
}
