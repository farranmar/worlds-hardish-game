package last.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.Circle;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class DeathBall extends GameObject {

    private static final double speed = 5;
    private Color color;

    public DeathBall(GameWorld gameWorld, Vec2d position) {
        super(gameWorld, new Vec2d(30), position);
        this.color = Color.rgb(136, 141, 167);
        this.addComponents();
    }

    public DeathBall(GameWorld gameWorld, Vec2d position, Color color) {
        super(gameWorld, new Vec2d(30), position);
        this.color = color;
        this.addComponents();
    }

    private void addComponents(){
        this.add(new CollideComponent(new Circle(this.getSize().x/2, this.getPosition().plus(this.getSize().sdiv(2)))));
        this.add(new DrawComponent());
        this.add(new TickComponent());
    }

    public static ArrayList<DeathBall> deathBallWall(GameWorld gameWorld, Vec2d position, Vec2d separation, int number) {
        ArrayList<DeathBall> returnList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            returnList.add(new DeathBall(gameWorld, position.plus(separation.smult(i))));
        }
        return returnList;
    }

    public void onCollide(GameObject obj, Vec2d mtv){
//        super.onCollide(obj, mtv);
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
        ele.setAttribute("class", "DeathBall");
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);
        return ele;
    }

    public DeathBall(Element ele, GameWorld world){
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);

        Element childrenEle = getTopElementsByTagName(ele, "Children").get(0);
        this.setChildrenXml(childrenEle, EditorScreen.getClassMap());
    }

}
