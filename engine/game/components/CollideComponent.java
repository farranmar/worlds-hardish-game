package engine.game.components;

import engine.game.objects.GameObject;
import engine.game.objects.shapes.Shape;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class CollideComponent extends GameComponent {

    private Shape shape;
    private boolean collidable = true; // whether it can actually collide at the moment (eg is false when actively dragging)
    private boolean isStatic = false;
    private double restitution = 1;

    public CollideComponent(Shape shape){
        super(ComponentTag.COLLIDE);
        assert(shape != null);
        this.shape = shape;
    }

    public CollideComponent(Shape shape, double restitution){
        super(ComponentTag.COLLIDE);
        assert(shape != null);
        this.shape = shape;
        this.restitution = restitution;
    }

    public CollideComponent(Shape shape, boolean isStatic){
        super(ComponentTag.COLLIDE);
        assert(shape != null);
        this.shape = shape;
        this.isStatic = isStatic;
    }

    public CollideComponent(Shape shape, boolean isStatic, double restitution){
        super(ComponentTag.COLLIDE);
        assert(shape != null);
        this.shape = shape;
        this.isStatic = isStatic;
        this.restitution = restitution;
    }

    public Shape getShape() {
        return shape;
    }

    public void setSize(Vec2d newSize){
        this.shape.setSize(newSize);
    }

    public void setPosition(Vec2d newPosition){
        this.shape.setPosition(newPosition);
    }

    public boolean isStatic(){
        return isStatic;
    }

    public void setStatic(boolean s){
        this.isStatic = s;
    }

    public void setCollidable(boolean collidable){
        this.collidable = collidable;
    }

    public boolean getCollidable(){
        return this.collidable;
    }

    public double getRestitution(){
        return this.restitution;
    }

    public void setRestitution(double restitution){
        this.restitution = restitution;
    }

    public Vec2d collidesWith(GameObject obj){
        if(collidable){
            return shape.collidesWith(obj.getCollisionShape());
        }
        return null;
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        ele.setAttribute("collidable", this.collidable+"");
        ele.setAttribute("isStatic", this.isStatic+"");
        ele.setAttribute("restitution", this.restitution+"");
        Element shape = this.shape.toXml(doc);
        ele.appendChild(shape);
        return ele;
    }

    public static CollideComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("COLLIDE")){ return null; }
        Shape shape = Shape.fromXml((Element)(getTopElementsByTagName(ele, "Shape").item(0)));
        CollideComponent collideComponent = new CollideComponent(shape);
        collideComponent.setConstants(ele);
        collideComponent.setCollidable(Boolean.parseBoolean(ele.getAttribute("collidable")));
        collideComponent.setStatic(Boolean.parseBoolean(ele.getAttribute("isStatic")));
        collideComponent.setRestitution(Double.parseDouble(ele.getAttribute("restitution")));
        return collideComponent;
    }

}
