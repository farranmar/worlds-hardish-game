package engine.game.components;

import engine.game.objects.GameObject;
import engine.game.objects.shapes.Shape;
import engine.support.Vec2d;

public class Collidable extends GameComponent {

    private Shape shape;

    public Collidable(Shape shape){
        this.shape = shape;
        this.tag = Tag.COLLIDABLE;
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

    public boolean collidesWith(GameObject obj){
        return shape.collidesWith(obj.getCollisionShape());
    }

}
