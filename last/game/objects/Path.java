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
    private double startRatio = 0.5;

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
        this.add(new DrawComponent());
        this.add(new TickComponent());
    }

    public void setVisible(boolean v){
        this.visible = v;
    }

    public void setActive(boolean a){
        this.active = a;
    }

    public double getStartRatio(){
        return this.startRatio;
    }

    public void setStartRatio(double newRatio){
        this.startRatio = newRatio;
        this.updateBallPosition();
    }

    public Path clone(){
        assert(this.parent instanceof DeathBall);
        Path newPath = new Path(this.gameWorld, (DeathBall)this.parent, ((PathPoint)children.get(0)).getCenter(), ((PathPoint)children.get(1)).getCenter());
        newPath.setVisible(this.visible);
        newPath.setActive(this.active);
        return newPath;
    }

    public void delete(){
        super.delete();
        assert(this.parent instanceof DeathBall);
        ((DeathBall)this.parent).delete(true);
        for(GameObject child : children){
            assert(child instanceof PathPoint);
            ((PathPoint)child).delete(true);
        }
    }

    public void delete(boolean justThis){
        super.delete();
    }

    public void updateBallPosition(){
        Vec2d center = ((PathPoint)children.get(0)).getCenter().getCenter(((PathPoint)children.get(1)).getCenter(), this.startRatio);
        assert(this.parent instanceof DeathBall);
        ((DeathBall)this.parent).updateSlidePositions();
        ((DeathBall)this.parent).setCenter(center, true);
    }

    public Vec2d getPointOne(){
        return ((PathPoint)children.get(0)).getCenter();
    }

    public void setPointOne(Vec2d newPos){
        ((PathPoint)this.children.get(0)).setCenter(newPos);
    }

    public Vec2d getPointTwo(){
        return ((PathPoint)children.get(1)).getCenter();
    }

    public void setPointTwo(Vec2d newPos){
        ((PathPoint)this.children.get(1)).setCenter(newPos);
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
        g.strokeLine(((PathPoint)children.get(0)).getCenter().x, ((PathPoint)children.get(0)).getCenter().y,
                ((PathPoint)children.get(1)).getCenter().x, ((PathPoint)children.get(1)).getCenter().y);
        super.onDraw(g);
    }

    @Override
    public void onMousePressed(double x, double y) {
        if(!visible){ return; }
        super.onMousePressed(x, y);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(active){
            assert(this.parent instanceof DeathBall);
            assert(this.children.get(1) instanceof PathPoint);
            assert(this.children.get(0) instanceof PathPoint);
            Vec2d dbCenter = ((DeathBall)this.parent).getCenter();
            Vec2d toPos, fromPos;
            if(forward){
                toPos = ((PathPoint)children.get(1)).getCenter();
                fromPos = ((PathPoint)children.get(0)).getCenter();
            } else {
                toPos = ((PathPoint)children.get(0)).getCenter();
                fromPos = ((PathPoint)children.get(1)).getCenter();
            }
            Vec2d direction = toPos.minus(dbCenter).normalize();
            ((DeathBall)this.parent).translate(direction.smult(DeathBall.speed), true);
            Vec2d newDbCenter = ((DeathBall)this.parent).getCenter();
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
        ele.setAttribute("startRatio", this.startRatio+"");
        return ele;
    }

    public Path(Element ele, GameWorld world){
        super(ele, world, EditorScreen.classMap);
        this.visible = Boolean.parseBoolean(ele.getAttribute("visible"));
        this.active = Boolean.parseBoolean(ele.getAttribute("active"));
        this.forward = Boolean.parseBoolean(ele.getAttribute("forward"));
        this.color = colorFromXml(GameWorld.getTopElementsByTagName(ele, "Color").get(0));
        this.startRatio = Double.parseDouble(ele.getAttribute("startRatio"));
    }
}
