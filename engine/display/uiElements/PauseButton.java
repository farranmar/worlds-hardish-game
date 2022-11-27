package engine.display.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PauseButton extends Button {

    public PauseButton(Color c, Vec2d p, Vec2d s){
        super("Pause Button", c, p, s);
        this.outline = false;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        double width = this.size.x/3;
        if(this.highlighted){ g.setFill(Color.WHITE); }
        else { g.setFill(this.color); }
        g.fillRect(position.x, position.y, width, size.y);
        g.fillRect(position.x+width*2, position.y, width, size.y);
        super.onDraw(g);
    }
}
