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
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Wall extends GameObject {

    protected Color color;
    protected Resizer resizer;

    public Wall(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position);
        this.color = Color.rgb(126, 162, 170);
        this.resizer = new Resizer(world, this, this.getPosition().plus(this.getSize()));
        this.addComponents();
    }

    public Wall(GameWorld world, Vec2d size, Vec2d position, Color color){
        super(world, size, position);
        this.color = color;
        this.resizer = new Resizer(world, this, this.getPosition().plus(this.getSize()));
        this.addComponents();
    }

    public Wall clone(){
        return new Wall(this.gameWorld, this.getSize(), this.getPosition(), this.color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()), true, 1);
        this.add(collide);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        super.onCollide(obj, mtv);
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
        ele.setAttribute("class", "Wall");
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);
        return ele;
    }

    public Wall(Element ele, GameWorld world){
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);

        Element childrenEle = getTopElementsByTagName(ele, "Children").get(0);
        this.setChildrenXml(childrenEle, EditorScreen.getClassMap());
    }

}
