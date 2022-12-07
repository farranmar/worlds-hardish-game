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
    public static final Vec2d size = new Vec2d(20);

    public PathPoint(GameWorld world, Path path, Vec2d pos){
        super(world, size, pos.minus(size.sdiv(2)));
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.color = Color.rgb(0, 255, 0);
        this.parent = path;
    }

    public PathPoint(GameWorld world, Path path, Vec2d pos, Color color){
        super(world, size, pos.minus(size.sdiv(2)));
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.color = color;
        this.parent = path;
    }

    public PathPoint(GameWorld world, Resizer resizer, Vec2d pos, Color color){
        super(world, size, pos.minus(size.sdiv(2)));
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

    public Vec2d getCenter(){
        return this.getPosition().plus(this.getSize().sdiv(2));
    }

    public void setCenter(Vec2d newCenter){
        this.setPosition(newCenter.minus(this.getSize().sdiv(2)));
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
        g.fillOval(pos.x, pos.y, size.x, size.y);
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
