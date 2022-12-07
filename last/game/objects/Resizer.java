package last.game.objects;

import engine.game.components.DragComponent;
import engine.game.components.DrawComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Resizer extends GameObject {

    public Resizer(GameWorld world, GameObject sqObj, Vec2d p1){
        super(world, new Vec2d(0), p1);
        this.parent = sqObj;
        PathPoint pp = new PathPoint(world, this, p1, Color.rgb(255, 0, 0));
        this.addChild(pp);
        this.addComponents();
    }

    private void addComponents(){
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.add(new TickComponent());
    }

    public Resizer clone(){
        return new Resizer(this.gameWorld, this.parent, children.get(0).getPosition());
    }

    public void updateSize(){
        Vec2d pos = this.parent.getPosition();
        Vec2d newSize = this.children.get(0).getPosition().minus(pos);
        if (newSize.x < 30) {
            newSize = new Vec2d(30, newSize.y);
        }
        if (newSize.y < 30) {
            newSize = new Vec2d(newSize.x, 30);
        }
        this.parent.setSize(newSize);
        this.children.get(0).setPosition(pos.plus(newSize));
    }

    public Vec2d getPointOne(){
        return this.children.get(0).getPosition();
    }

    public void setPointOne(Vec2d newPos){
        this.children.get(0).setPosition(newPos);
    }

    @Override
    public void translate(Vec2d d) {
        super.translate(d);
        this.children.get(0).translate(d);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Resizer");
        return ele;
    }

    public Resizer(Element ele, GameWorld world){
        super(ele, world, EditorScreen.classMap);
    }

}
