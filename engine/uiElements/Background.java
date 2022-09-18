package engine.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Background extends UIElement {

    private boolean fullWindow = false;

    public Background(Color c, Vec2d p, Vec2d s) {
        super("Background", c, p, s);
    }

    public Background(Color c, Vec2d s){
        super("Background", c, new Vec2d(0,0), s);
    }

    public Background(Color c){
        super("Background", c, new Vec2d(0,0), new Vec2d(0,0));
        this.setSize(new Vec2d(this.windowSize.x, this.windowSize.y));
        this.fullWindow = true;
    }

    public void onTick(long nanosSincePreviousTick){
        super.onTick(nanosSincePreviousTick);
    }

    public void onDraw(GraphicsContext g){
        g.setFill(this.color);
        g.fillRect(this.position.x, this.position.y, this.size.x, this.size.y);
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        if(this.fullWindow){
            this.size = new Vec2d(newWindowSize.x, newWindowSize.y);
        } else {
            super.onResize(newWindowSize, newScreenSize);
        }
    }
}
