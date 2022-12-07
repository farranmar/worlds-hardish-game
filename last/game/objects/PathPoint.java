package last.game.objects;

import engine.game.components.DragComponent;
import engine.game.components.DrawComponent;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PathPoint extends GameObject {

    private Color color;

    public PathPoint(GameWorld world, Path path, Vec2d pos){
        super(world, new Vec2d(20), pos);
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.color = Color.rgb(0, 255, 0);
        this.parent = path;
    }

    public PathPoint(GameWorld world, Path path, Vec2d pos, Color color){
        super(world, new Vec2d(20), pos);
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.color = color;
        this.parent = path;
    }

    public PathPoint(GameWorld world, Resizer resizer, Vec2d pos, Color color){
        super(world, new Vec2d(20), pos);
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.color = color;
        this.parent = resizer;
    }

    public PathPoint clone(){
        if(this.parent instanceof Path){
            return new PathPoint(this.gameWorld, (Path)this.parent, this.getPosition(), this.color);
        } else if(this.parent instanceof Resizer){
            return new PathPoint(this.gameWorld, (Resizer)this.parent, this.getPosition(), this.color);
        } else {
            return null;
        }
    }

    @Override
    public void translate(Vec2d d) {
        super.translate(d);
        if(this.parent instanceof Path){
            ((Path)this.parent).updateBallPosition();
        } else if(this.parent instanceof Resizer){
            ((Resizer)this.parent).updateSize();
        }
    }

    @Override
    public void onDraw(GraphicsContext g) {
        super.onDraw(g);
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillOval(pos.minus(size.sdiv(2)).x, pos.minus(size.sdiv(2)).y, size.x, size.y);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "PathPoint");
        ele.appendChild(colorToXml(doc, this.color));
        return ele;
    }

    public PathPoint(Element ele, GameWorld world){
        super(ele, world, EditorScreen.classMap);
        this.color = colorFromXml(GameWorld.getTopElementsByTagName(ele, "Color").get(0));
    }
}
