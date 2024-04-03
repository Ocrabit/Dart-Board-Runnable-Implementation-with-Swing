package Variables;

public class MarkShot {
    boolean active = false;
    public int x,y;

    public void setPoint(int x, int y){
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public boolean getActive(){
        return active;
    }

    public void empty(){
        active = false;
        x = 0;
        y = 0;
    }
}
