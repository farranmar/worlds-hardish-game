package last.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Path extends GameObject {

    private Color color;
    private boolean visible = false;
    private boolean active = false;
    private boolean forward = true;

    public Path(GameWorld world, DeathBall deathBall, Vec2d p1, Vec2d p2){
        super(world, p2.minus(p1), p1.getCenter(p2));
        this.parent = deathBall;
        PathPoint pp1 = new PathPoint(world, this, p1, Color.rgb(0, 255, 0));
        PathPoint pp2 = new PathPoint(world, this, p2, Color.rgb(255, 0, 0));
        this.addChild(pp1);
        this.addChild(pp2);
        this.color = Color.rgb(238, 224, 203);
        this.addComponents();
    }

    private void addComponents(){
        this.add(new DragComponent(this));
        this.add(new DrawComponent());
        this.add(new TickComponent());
    }

    public void setVisible(boolean v){
        this.visible = v;
    }

    public void setActive(boolean a){
        this.active = a;
    }

    public Path clone(){
        assert(this.parent instanceof DeathBall);
        Path newPath = new Path(this.gameWorld, (DeathBall)this.parent, children.get(0).getPosition(), children.get(1).getPosition());
        newPath.setVisible(this.visible);
        newPath.setActive(this.active);
        return newPath;
    }

    public void updateBallPosition(){
        Vec2d center = children.get(0).getPosition().getCenter(children.get(1).getPosition());
        assert(this.parent instanceof DeathBall);
        Vec2d newPos = center.minus(this.parent.getSize().sdiv(2));
        ((DeathBall)this.parent).setPosition(newPos, true);
    }

    public Vec2d getPointOne(){
        return this.children.get(0).getPosition();
    }

    public void setPointOne(Vec2d newPos){
        this.children.get(0).setPosition(newPos);
    }

    public Vec2d getPointTwo(){
        return this.children.get(1).getPosition();
    }

    public void setPointTwo(Vec2d newPos){
        this.children.get(1).setPosition(newPos);
    }

    @Override
    public void translate(Vec2d d) {
        super.translate(d);
        this.children.get(0).translate(d);
        this.children.get(1).translate(d);
    }

    public void onDraw(GraphicsContext g){
        if(!this.visible){ return; }
        g.setStroke(this.color);
        g.setLineWidth(5);
        g.strokeLine(children.get(0).getPosition().x, children.get(0).getPosition().y, children.get(1).getPosition().x, children.get(1).getPosition().y);
        super.onDraw(g);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(active){
            assert(this.parent instanceof DeathBall);
            assert(this.children.get(1) instanceof PathPoint);
            assert(this.children.get(0) instanceof PathPoint);
            Vec2d dbCenter = this.parent.getPosition().plus(this.parent.getSize().sdiv(2));
            Vec2d toPos, fromPos;
            if(forward){
                toPos = this.children.get(1).getPosition();
                fromPos = this.children.get(0).getPosition();
            } else {
                toPos = this.children.get(0).getPosition();
                fromPos = this.children.get(1).getPosition();
            }
            Vec2d direction = toPos.minus(dbCenter).normalize();
            ((DeathBall)this.parent).translate(direction.smult(DeathBall.speed), true);
            Vec2d newDbCenter = this.parent.getPosition().plus(this.parent.getSize().sdiv(2));
            if(toPos.x > fromPos.x && newDbCenter.x >= toPos.x){ forward = !forward; }
            else if(toPos.x < fromPos.x && newDbCenter.x <= toPos.x){ forward = !forward; }
            else if(toPos.y > fromPos.y && newDbCenter.y >= toPos.y){ forward = !forward; }
            else if(toPos.y < fromPos.y && newDbCenter.y <= toPos.y){ forward = !forward; }
        }
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.appendChild(colorToXml(doc, this.color));
        ele.setAttribute("class", "Path");
        ele.setAttribute("visible", this.visible+"");
        ele.setAttribute("active", this.active+"");
        ele.setAttribute("forward", this.forward+"");
        return ele;
    }

    public Path(Element ele, GameWorld world){
        super(ele, world, EditorScreen.classMap);
        this.visible = Boolean.parseBoolean(ele.getAttribute("visible"));
        this.active = Boolean.parseBoolean(ele.getAttribute("active"));
        this.forward = Boolean.parseBoolean(ele.getAttribute("forward"));
        this.color = colorFromXml(GameWorld.getTopElementsByTagName(ele, "Color").get(0));
    }
}
