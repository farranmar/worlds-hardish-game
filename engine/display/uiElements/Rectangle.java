package engine.display.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends UIElement {

    private Color outlineColor;

    public Rectangle(Vec2d size, Vec2d position){
        super("Rectangle", Color.BLACK, position, size);
        this.outlineColor = Color.BLACK;
    }

    public Rectangle(Color fill, Color outline, Vec2d size, Vec2d position){
        super("Rectangle", fill, position, size);
        this.outlineColor = outline;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setFill(this.color);
        g.fillRect(position.x, position.y, size.x, size.y);
        g.setStroke(this.outlineColor);
        g.setLineWidth(2);
        g.strokeRect(position.x, position.y, size.x, size.y);
        super.onDraw(g);
    }

}
