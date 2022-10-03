package engine.game.objects.shapes;

import engine.support.Vec2d;

public interface Shape {

    boolean collidesWith(Shape shape);

    boolean collidesWithAAB(AAB aab);

    boolean collidesWithCircle(Circle circle);

    boolean collidesWithPoint(Vec2d point);

    Vec2d getSize();

    void setSize(Vec2d newSize);

    Vec2d getPosition();

    void setPosition(Vec2d newPosition);

}
