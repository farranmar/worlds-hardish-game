package last.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.game.LastWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Platform extends GameObject {

    private Color color;

    public Platform(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position);
        this.color = Color.BLACK;
        this.addComponents();
    }

    public Platform(GameWorld world, Vec2d size, Vec2d position, Color color){
        super(world, size, position);
        this.color = color;
        this.addComponents();
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()), true, 1);
        this.add(collide);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
    }

    public void onDraw(GraphicsContext g){
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Platform");
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);
        return ele;
    }

    public Platform(Element ele, GameWorld world){
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Platform")){ return; }
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);

        Element childrenEle = getTopElementsByTagName(ele, "Children").get(0);
        this.setChildrenXml(childrenEle, LastWorld.getClassMap());
    }

}
