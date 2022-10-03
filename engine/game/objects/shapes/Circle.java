package engine.game.objects.shapes;

import engine.support.Vec2d;

public class Circle implements Shape {

    private Vec2d position;
    private double radius;

    public Circle(double radius, Vec2d position){
        this.radius = radius;
        this.position = position;
    }

    public Vec2d getSize(){
        return new Vec2d(this.radius);
    }

    public void setSize(Vec2d newSize){
        this.radius = newSize.x;
    }

    public Vec2d getPosition(){
        return this.position;
    }

    public void setPosition(Vec2d newPosition){
        this.position = newPosition;
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.collidesWithCircle(this);
    }

    @Override
    public boolean collidesWithAAB(AAB aab) {
        Vec2d maxes = new Vec2d(aab.getPosition().x + aab.getSize().x, aab.getPosition().y + aab.getSize().y);
        Vec2d nearestPoint = this.clamp(this.position, aab.getPosition(), maxes);
        double dist = this.position.dist2(nearestPoint);
        return dist < (this.radius * this.radius);
    }

    @Override
    public boolean collidesWithCircle(Circle circle) {
        double distCenters = this.position.dist2(circle.getPosition());
        double sum = this.radius + circle.getSize().x;
        double distRadius = sum*sum;
        return distCenters <= distRadius;
    }

    public boolean collidesWithPoint(Vec2d point){
        double dist = this.position.dist2(point);
        return dist < (this.radius * this.radius);
    }

    private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
        double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
        double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
        return new Vec2d(xClamp, yClamp);
    }
}
