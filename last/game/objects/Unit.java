package last.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;
import static last.screens.EditorScreen.classMap;

public class Unit extends GameObject {

    Color color;

    public Unit(GameWorld gameWorld, Color color){
        super(gameWorld);
        this.color = color;
        this.transformComponent = new TransformComponent(new Vec2d(0), new Vec2d(0));
        this.addComponents();
    }

    public Unit(GameWorld gameWorld, Vec2d size, Vec2d position, Color color){
        super(gameWorld);
        this.color = color;
        this.transformComponent = new TransformComponent(size, position);
        this.addComponents();
    }

    public Unit(Unit block) {
        this(block.gameWorld, block.getSize(), block.getPosition(), block.color);
    }

    private void addComponents(){
        this.components.add(new DrawComponent());
        this.components.add(new ClickComponent());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Unit clone(){
        Unit clone = new Unit(this);
        clone.setSize(this.getSize());
        clone.setPosition(this.getPosition());
        clone.add(new CollideComponent(new AAB(this.getSize(), this.getPosition())));
        return clone;
    }

    public void onDraw(GraphicsContext g){
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
        super.onDraw(g);
    }

    public Element toXml(Document doc){
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Unit");
        Element colorEle = colorToXml(doc, this.color);
        ele.appendChild(colorEle);
        return ele;
    }

    public Unit(Element ele, GameWorld world){
        super(ele, world, classMap);
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Unit")){ return; }
        Color color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        this.setColor(color);
    }

}
