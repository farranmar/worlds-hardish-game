package engine.game.objects;

import engine.game.components.DrawComponent;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Grid extends GameObject {

    private int rows;
    private int cols;
    private Color color;

    public Grid(GameWorld world, Vec2d size, Vec2d pos, int rows, int cols, Color color){
        super(world, size, pos);
        this.rows = rows;
        this.cols = cols;
        this.color = color;
        this.add(new DrawComponent());
        this.drawPriority = -10;
    }

    public Grid(GameWorld world, Vec2d size, Vec2d pos, int rows, int cols){
        this(world, size, pos, rows, cols, Color.rgb(58, 65, 61));
    }

    @Override
    public void onDraw(GraphicsContext g) {
        super.onDraw(g);
        g.setStroke(this.color);
        g.setLineWidth(1);
        Vec2d pos = this.getPosition();
        Vec2d size = this.getSize();
        double rowSpacing = this.getSize().y / this.rows;
        for(int i = 0; i <= this.rows; i++){
            Vec2d left = new Vec2d(pos.x, pos.y + rowSpacing*i);
            Vec2d right = new Vec2d(pos.x + size.x, pos.y + rowSpacing*i);
            g.strokeLine(left.x, left.y, right.x, right.y);
        }
        double colSpacing = this.getSize().x / this.cols;
        for(int i = 0; i <= this.cols; i++){
            Vec2d top = new Vec2d(pos.x + colSpacing*i, pos.y);
            Vec2d bot = new Vec2d(pos.x + colSpacing*i, pos.y + size.y);
            g.strokeLine(top.x, top.y, bot.x, bot.y);
        }
    }
}
