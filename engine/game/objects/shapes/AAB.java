package engine.game.objects.shapes;

import engine.support.Vec2d;

public class AAB implements Shape {

    private Vec2d size;
    private Vec2d position;

    public AAB(Vec2d size, Vec2d position){
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

    @Override
    public boolean collidesWith(Shape shape) {
        if(shape == null) { return false; }
        return shape.collidesWithAAB(this);
    }

    @Override
    public boolean collidesWithAAB(AAB aab) {
        boolean projX = (this.position.x <= aab.getPosition().x+aab.getSize().x) && (this.position.x+this.size.x >= aab.position.x);
        boolean projY = (this.position.y <= aab.getPosition().y+aab.getSize().y) && (this.position.y+this.size.y >= aab.position.y);
        return projX && projY;
    }

    @Override
    public boolean collidesWithCircle(Circle circle) {
        Vec2d maxes = new Vec2d(this.position.x + this.size.x, this.position.y + this.size.y);
        Vec2d nearestPoint = this.clamp(circle.getPosition(), this.position, maxes);
        double dist = circle.getPosition().dist2(nearestPoint);
        return dist < (circle.getSize().x * circle.getSize().x);
    }

    public boolean collidesWithPoint(Vec2d point){
        boolean inX = this.position.x <= point.x && this.position.x+this.size.x >= point.x;
        boolean inY = this.position.y <= point.y && this.position.y+this.size.y >= point.y;
        return inX && inY;
    }

    private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
        double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
        double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
        return new Vec2d(xClamp, yClamp);
    }
}
