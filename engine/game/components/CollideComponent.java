package engine.game.components;

import engine.game.objects.GameObject;
import engine.game.objects.shapes.Shape;
import engine.support.Vec2d;

public class CollideComponent extends GameComponent {

    private Shape shape;
    private boolean collidable = true; // whether it can actually collide at the moment (eg is false when actively dragging)
    private boolean isStatic = false;
    private double restitution = 1;

    public CollideComponent(Shape shape){
        super(ComponentTag.COLLIDE);
        this.shape = shape;
    }

    public CollideComponent(Shape shape, double restitution){
        super(ComponentTag.COLLIDE);
        this.shape = shape;
        this.restitution = restitution;
    }

    public CollideComponent(Shape shape, boolean isStatic){
        super(ComponentTag.COLLIDE);
        this.shape = shape;
        this.isStatic = isStatic;
    }

    public CollideComponent(Shape shape, boolean isStatic, double restitution){
        super(ComponentTag.COLLIDE);
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

}
