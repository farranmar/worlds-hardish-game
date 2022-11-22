package nin.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.components.PhysicsComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.objects.shapes.Circle;
import engine.game.objects.shapes.Ray;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nin.game.NinWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;
import static nin.game.objects.Projectile.Direction.LEFT;
import static nin.game.objects.Projectile.Direction.RIGHT;

public class Projectile extends GameObject {

    private Direction direction;
    private static final double speed = 5;
    private Color color;

    public enum Direction {
        RIGHT("RIGHT"),
        LEFT("LEFT"),
        UP("UP"),
        DOWN("DOWN");

        public String string;
        Direction(String s){ this.string = s; }
    }

    public Projectile(GameWorld gameWorld, Block b, Vec2d size, Vec2d position, Direction direction, Color color) {
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.direction = direction;
        this.parent = b;
        this.color = color;
        this.addComponents();
    }

    @Override
    public void setParent(GameObject parent) {
        assert(parent instanceof Block);
        super.setParent(parent);
    }

    private void addComponents(){
        this.add(new CollideComponent(new Circle(this.getSize().x, this.getPosition().plus(this.getSize().sdiv(4)))));
        this.add(new DrawComponent());
        this.add(new TickComponent());
        this.add(new PhysicsComponent(this, 1));
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(obj == this.parent || (!(obj instanceof Block) && !(obj instanceof Platform))) { return; }
        super.onCollide(obj, mtv);
        this.delete();
    }

    public void delete(){
        ((Block)parent).removeProjectile(this);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        g.setFill(this.color);
        Vec2d pos = this.getPosition();
        Vec2d size = this.getSize();
        g.fillOval(pos.x, pos.y, size.x, size.y);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Projectile");
        ele.setAttribute("direction", this.direction.string);
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);
        return ele;
    }

    public Projectile(Element ele, GameWorld world){
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Projectile")){ return; }
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.color = colorFromXml((Element)(getTopElementsByTagName(ele, "Color").item(0)));
        this.direction = Direction.valueOf(ele.getAttribute("direction"));

        Element componentsEle = (Element)(getTopElementsByTagName(ele, "Components").item(0));
        this.addComponentsXml(componentsEle);

        Element childrenEle = (Element)(getTopElementsByTagName(ele, "Children").item(0));
        this.setChildrenXml(childrenEle, NinWorld.getClassMap());
    }
}
