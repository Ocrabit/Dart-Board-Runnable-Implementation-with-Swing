package UIComponents;

import Variables.MarkShot;

import javax.swing.*;
import java.awt.*;

public class DartBoard extends JPanel {
    MarkShot[] markShotsP1;
    MarkShot[] markShotsP2;
    AimDart aimDart;
    public DartBoard(AimDart aimDart, MarkShot[] markShotsP1, MarkShot[] markShotsP2){
        setPreferredSize(new Dimension(150,150));
        setDoubleBuffered(true);
        this.aimDart = aimDart;
        this.markShotsP1 = markShotsP1;
        this.markShotsP2 = markShotsP2;
        JLabel image = new JLabel(new ImageIcon("dartBoard.png"));
        add(image);
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D)g;
        aimDart.draw(g2);

        for (int i = 0; i < markShotsP1.length; i++) {
            if(markShotsP1[i].getActive()) {
                g2.setColor(Color.BLACK);
                g2.fillOval(markShotsP1[i].x,markShotsP1[i].y,4,4);
            }
        }

        for (int i = 0; i < markShotsP2.length; i++) {
            if(markShotsP2[i].getActive()) {
                g2.setColor(Color.MAGENTA);
                g2.fillOval(markShotsP2[i].x,markShotsP2[i].y,4,4);
            }
        }

    }
}
