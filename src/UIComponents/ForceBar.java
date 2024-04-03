package UIComponents;

import javax.swing.*;
import java.awt.*;

public class ForceBar extends JProgressBar{
    int force = 0;
    boolean increasing = true;
    public boolean spaceBarPressed = false;

    public ForceBar(){
        setOrientation(SwingConstants.VERTICAL);
        setPreferredSize(new Dimension(80,120));
        setMinimum(0);
        setMaximum(100);
        setString("force");
    }

    public void updateProgressbar(){
        if(!spaceBarPressed) {
            if (increasing && force < 100) {
                force += 2;
                setValue(force);
            } else if (!increasing && force > 0) {
                force -= 2;
                setValue(force);
            } else {
                increasing = !increasing;
                force -= 2;
                setValue(force);
            }
        }
    }

    public float getForce() {
        return (float)force/100;
    }
}
