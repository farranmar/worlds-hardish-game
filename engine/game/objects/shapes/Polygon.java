package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Polygon implements Shape {

    protected Vec2d[] points;

    public Polygon(Vec2d... points) {
        this.points = points;
    }

    public int getNumPoints() {
        return points.length;
    }

    public Vec2d getPoint(int i) {
        return points[i];
    }

    @Override
    public Vec2d collidesWith(Shape shape) {
        return null;
    }

    @Override
    public Vec2d collidesWithAAB(AAB aab) {
        return null;
    }

    @Override
    public Vec2d collidesWithCircle(Circle circle) {
        return null;
    }

    @Override
    public Vec2d collidesWithPoint(Vec2d point) {
        return null;
    }

    @Override
    public Vec2d collidesWithRay(Ray ray) {
        return null;
    }

    @Override
    public Vec2d collidesWithPolygon(Polygon polygon) {
        return null;
    }

    @Override
    public Vec2d getSize() {
        return null;
    }

    @Override
    public void setSize(Vec2d newSize) {

    }

    @Override
    public Vec2d getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vec2d newPosition) {

    }

    @Override
    public Element toXml(Document doc) {
        return null;
    }
}
