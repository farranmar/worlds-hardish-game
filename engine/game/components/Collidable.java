package engine.game.components;

import engine.game.objects.GameObject;
import engine.game.objects.shapes.Shape;
import engine.support.Vec2d;

public class Collidable extends GameComponent {

    private Shape shape;
    private boolean collidable = true; // whether it can actually collide at the moment (eg is false when actively dragging)

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

    public void setCollidable(boolean collidable){
        this.collidable = collidable;
    }

    public boolean getCollidable(){
        return this.collidable;
    }

    public boolean collidesWith(GameObject obj){
        if(collidable){
            return shape.collidesWith(obj.getCollisionShape());
        }
        return false;
    }

}
