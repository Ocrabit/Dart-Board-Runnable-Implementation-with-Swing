package UIComponents;

import javax.swing.*;
import java.util.Random;

public class WindForce {
    float windForce;
    JLabel direction = new JLabel();
    int determineDir;


    Random rand = new Random();

    public WindForce(){
        generateWF();
    }

    public void generateWF(){
        windForce = rand.nextFloat(0, 5f);

        determineDir = rand.nextInt(0,4);
        if(determineDir == 0){          //UP
            direction.setIcon(new ImageIcon("Directions/Up.png"));
        } else if(determineDir == 1){   //LEFT
            direction.setIcon(new ImageIcon("Directions/Left.png"));
        } else if (determineDir == 2) { //DOWN
            direction.setIcon(new ImageIcon("Directions/Down.png"));
        } else {                        //RIGHT
            direction.setIcon(new ImageIcon("Directions/Right.png"));
        }
    }

    public float getWindForce(){
        return windForce;
    }

    public JLabel getDirection(){
        return direction;
    }

    public int getDirectionDir(){
        return determineDir;
    }
}
