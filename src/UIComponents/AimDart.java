package UIComponents;

import main.KeyHandler;

import java.awt.*;


public class AimDart {
    KeyHandler keyH;
    public int x,y = 0;
    int speed = 2;

    public AimDart(KeyHandler keyH) {
        this.keyH = keyH;

    }

    public void update(){
        if(keyH.upPressed) {
            if(y>0) {
                y -= speed;
            }
        }  if (keyH.leftPressed){
            if(x>0) {
                x -= speed;
            }
        } if (keyH.downPressed){
            if(y<150) {
                y += speed;
            }
        } if (keyH.rightPressed){
            if(x<146) {
                x += speed;
            }
        }
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.RED);
        g2.fillOval(x,y,4,4);
    }
}
