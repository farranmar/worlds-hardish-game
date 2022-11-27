package engine.display.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RoundRect extends UIElement {

    private Color outlineColor;

    public RoundRect(Vec2d size, Vec2d position){
        super("RoundRect", Color.BLACK, position, size);
        this.outlineColor = Color.BLACK;
    }

    public RoundRect(Color fill, Color outline, Vec2d size, Vec2d position){
        super("RoundRect", fill, position, size);
        this.outlineColor = outline;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setFill(this.color);
        g.fillRoundRect(position.x, position.y, size.x, size.y, 15, 15);
        g.setStroke(this.outlineColor);
        g.strokeRoundRect(position.x, position.y, size.x, size.y, 15, 15);
        super.onDraw(g);
    }
}
