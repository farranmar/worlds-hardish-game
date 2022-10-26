package engine.game.objects.shapes;

import engine.support.Vec2d;

public interface Shape {

    Vec2d collidesWith(Shape shape);

    Vec2d collidesWithAAB(AAB aab);

    Vec2d collidesWithCircle(Circle circle);

    Vec2d collidesWithPoint(Vec2d point);

    Vec2d getSize();

    void setSize(Vec2d newSize);

    Vec2d getPosition();

    void setPosition(Vec2d newPosition);

}
