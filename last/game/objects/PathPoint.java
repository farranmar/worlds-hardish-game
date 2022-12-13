package last.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DragComponent;
import engine.game.components.DrawComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.Circle;
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
        this.addComponents();
        this.color = Color.rgb(0, 255, 0);
        this.parent = path;
        world.addToSystems(this);
    }

    public PathPoint(GameWorld world, Path path, Vec2d pos, Color color){
        super(world, size, pos.minus(size.sdiv(2)));
        this.addComponents();
        this.color = color;
        this.parent = path;
        world.addToSystems(this);
    }

    public PathPoint(GameWorld world, Resizer resizer, Vec2d pos, Color color){
        super(world, size, pos.minus(size.sdiv(2)));
        this.addComponents();
        this.color = color;
        this.parent = resizer;
        world.addToSystems(this);
    }

    private void addComponents(){
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.add(new CollideComponent(new Circle(size.x/2, this.getPosition().plus(size.sdiv(2)))));
        this.setCollidable(false);
    }

    public PathPoint clone(){
        if(this.parent instanceof Path){
            PathPoint clone = new PathPoint(this.gameWorld, (Path)this.parent, this.getPosition(), this.color);
            clone.setCollidable(this.isCollidable());
            return clone;
        } else if(this.parent instanceof Resizer){
            PathPoint clone = new PathPoint(this.gameWorld, (Resizer)this.parent, this.getPosition(), this.color);
            clone.setCollidable(this.isCollidable());
            return clone;
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

    public void delete(){
        super.delete();
        if(this.parent instanceof Path){
            ((Path)this.parent).delete(true);
            assert(this.parent.getParent() instanceof DeathBall);
            ((DeathBall)this.parent.getParent()).delete(true);
        } else if(this.parent instanceof Resizer){
            ((Resizer)this.parent).delete(true);
            assert(this.parent.getParent() instanceof Wall);
            ((Wall)this.parent.getParent()).delete(true);
        }
    }

    public void delete(boolean justThis){
        super.delete();
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
    public void onCollide(GameObject obj, Vec2d mtv) { }

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
        world.addToSystems(this);
    }
}
