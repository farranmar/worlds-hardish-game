package engine.game.components;

import engine.support.Vec2d;

public class TransformComponent extends GameComponent{

    private Vec2d size;
    private Vec2d position;

    public TransformComponent(Vec2d size, Vec2d position){
        super(ComponentTag.TRANSFORM);
        this.size = size;
        this.position = position;
    }

    public Vec2d getSize(){
        return this.size;
    }

    public void setSize(Vec2d newSize){
        this.size = newSize;
    }

    public Vec2d getPosition(){
        return this.position;
    }

    public void setPosition(Vec2d newPosition){
        this.position = newPosition;
    }

    public void translate(double dx, double dy){
        this.position = new Vec2d(this.position.x + dx, this.position.y + dy);
    }

}
