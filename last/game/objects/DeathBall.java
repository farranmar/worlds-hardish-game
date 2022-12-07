package last.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.ComponentTag;
import engine.game.components.DrawComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.Circle;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.game.components.SlideComponent;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class DeathBall extends GameObject {

    public static final double speed = 5;
    private static final Vec2d size = new Vec2d(30);
    private Color color;
    private boolean drawPath = false;
    private boolean moving = true;

    public DeathBall(GameWorld gameWorld, Vec2d position) {
        super(gameWorld, size, position);
        this.color = Color.rgb(136, 141, 167);
        Vec2d p1 = this.getCenter().plus(new Vec2d(-50, 0));
        Vec2d p2 = this.getCenter().plus(new Vec2d(50, 0));
        Path path = new Path(gameWorld, this, p1, p2);
        this.addChild(path);
        this.addComponents();
    }

    public DeathBall(GameWorld gameWorld, Vec2d position, Color color) {
        super(gameWorld, new Vec2d(30), position);
        this.color = color;
        Vec2d p1 = position.plus(size.sdiv(2)).plus(new Vec2d(-50, 0));
        Vec2d p2 = position.plus(size.sdiv(2)).plus(new Vec2d(50, 0));
        Path path = new Path(gameWorld, this, p1, p2);
        this.addChild(path);
        this.addComponents();
    }

    private void addComponents(){
        this.add(new CollideComponent(new Circle(this.getSize().x/2, this.getPosition().plus(this.getSize().sdiv(2)))));
        this.add(new DrawComponent());
        this.add(new TickComponent());
    }

    public void setDrawPath(boolean drawPath) {
        this.drawPath = drawPath;
        assert(this.children.get(0) instanceof Path);
        ((Path)this.children.get(0)).setVisible(drawPath);
    }

    public void addSlideComponent(){
        this.remove(ComponentTag.DRAG);
        if(this.get(ComponentTag.SLIDE) == null){
            assert(this.children.get(0).getChildren().get(0) instanceof PathPoint);
            assert(this.children.get(0).getChildren().get(1) instanceof PathPoint);
            Vec2d p1 = this.children.get(0).getChildren().get(0).getPosition();
            Vec2d p2 = this.children.get(0).getChildren().get(1).getPosition();
            this.add(new SlideComponent(this, p1, p2, ((Path)this.children.get(0)).getStartRatio()));
        }
    }

    public void updateSlidePositions() {
        SlideComponent slideComponent = (SlideComponent) this.get(ComponentTag.SLIDE);
        if(slideComponent == null){ return; }
        slideComponent.updateEndpoint(((PathPoint)this.children.get(0).getChildren().get(0)).getCenter(),
                ((PathPoint)this.children.get(0).getChildren().get(1)).getCenter());
    }

    public void setMoving(boolean moving){
        this.moving = moving;
        assert(this.children.get(0) instanceof Path);
        ((Path)this.children.get(0)).setActive(moving);
    }

    public void setPath(Path path){
        this.children.set(0, path);
        path.setParent(this);
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        assert(this.children.get(0) instanceof Path);
        Path path = (Path)this.children.get(0);
        Vec2d d1 = path.getPointOne().minus(this.getPosition());
        Vec2d d2 = path.getPointTwo().minus(this.getPosition());
        path.setPointOne(newPosition.plus(d1));
        path.setPointTwo(newPosition.plus(d2));
        super.setPosition(newPosition);
    }

    public void setPosition(Vec2d newPosition, boolean onlyBall) {
        super.setPosition(newPosition);
    }

    public Vec2d getCenter(){
        return this.getPosition().plus(this.getSize().sdiv(2));
    }

    public void setCenter(Vec2d newCenter){
        this.setPosition(newCenter.minus(this.getSize().sdiv(2)));
    }

    public void setCenter(Vec2d newCenter, boolean onlyBall){
        this.setPosition(newCenter.minus(this.getSize().sdiv(2)), onlyBall);
    }

    public void setStartRatio(double newRatio){
        ((Path)this.children.get(0)).setStartRatio(newRatio);
    }

    public void translate(Vec2d d, boolean onlyBall) {
        this.setPosition(this.getPosition().plus(d), onlyBall);
    }

    public DeathBall clone(){
        DeathBall clone = new DeathBall(this.gameWorld, this.getPosition(), this.color);
        assert(this.children.get(0) instanceof Path);
        clone.setPath(((Path)this.children.get(0)).clone());
        clone.setDrawPath(true);
        clone.setMoving(false);
        return clone;
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
        ele.setAttribute("drawPath", this.drawPath+"");
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
