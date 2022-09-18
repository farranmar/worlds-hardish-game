package engine.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Timer extends UIElement{

    private boolean running = false;
    private double timeLeft;
    private boolean vertical;
    private int maxTime;

    public Timer(Color c, Vec2d p, Vec2d s, boolean v, int sec) {
        super("Timer", c, p, s);
        this.vertical = v;
        this.maxTime = sec;
        this.timeLeft = sec;
    }

    public Timer(Color c, double y, Vec2d s, boolean v, int sec) {
        super("Timer", c, y, s);
        this.vertical = v;
        this.maxTime = sec;
        this.timeLeft = sec;
    }

    public void start(){
        this.running = true;
    }

    public void stop(){
        this.running = false;
    }

    public void restart() {
        this.timeLeft = this.maxTime;
        this.running = true;
    }

    public void reset(){
        this.timeLeft = this.maxTime;
        this.running = false;
    }

    public boolean done(){
        return this.timeLeft <= 0;
    }

    public void onTick(long nanosSincePreviousTick){
        if(running && nanosSincePreviousTick < 1000000000){
            double secs = nanosSincePreviousTick * 0.000000001;
            this.timeLeft -= secs;
            if(this.timeLeft <= 0) { this.running = false; }
        }
        super.onTick(nanosSincePreviousTick);
    }

    public void onDraw(GraphicsContext g){
        g.setStroke(this.color);
        g.setFill(this.color);
        g.setLineWidth(2);
        g.strokeRect(this.position.x,this.position.y,this.size.x,this.size.y);
        if(this.timeLeft > 0){
            double percentLeft = this.timeLeft / this.maxTime;
            if(this.vertical){
                double upperY = this.position.y + (1-percentLeft)*this.size.y;
                g.fillRect(this.position.x,upperY,this.size.x,this.size.y - (1-percentLeft)*this.size.y);
            } else {
                double remainingLength =  this.size.x*percentLeft;
                g.fillRect(this.position.x,this.position.y,remainingLength,this.size.y);
            }
        }
        super.onDraw(g);
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        super.onResize(newWindowSize, newScreenSize);
    }
}
