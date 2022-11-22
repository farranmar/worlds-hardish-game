package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Ray implements Shape {

    private Vec2d position;
    private Vec2d size;

    // note: rays are only for collision checking, so mtvs are irrelevant - only important thing is whether they are null or not
    public Ray(Vec2d size, Vec2d position){
        this.size = size;
        this.position = position;
    }

    @Override
    public Vec2d collidesWith(Shape shape) {
        return shape.collidesWithRay(this);
    }

    @Override
    public Vec2d collidesWithAAB(AAB aab) {
        Vec2d end = this.position.plus(this.size);
        boolean containsStart = this.position.x >= aab.getPosition().x && this.position.x <= aab.getPosition().x+this.getSize().x && this.position.y >= aab.getPosition().y && this.position.y <= aab.getPosition().y+this.getSize().y;
        boolean containsEnd = end.x >= aab.getPosition().x && end.x <= aab.getPosition().x+this.getSize().x && end.y >= aab.getPosition().y && end.y <= aab.getPosition().y+this.getSize().y;
        if(containsStart || containsEnd){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    @Override
    public Vec2d collidesWithCircle(Circle circle) {
        Vec2d end = this.position.plus(this.size);
        boolean containsStart = this.position.dist(circle.getPosition()) <= circle.getSize().x;
        boolean containsEnd = end.dist(circle.getPosition()) <= circle.getSize().x;
        if(containsStart || containsEnd){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    @Override
    public Vec2d collidesWithPoint(Vec2d point) {
        boolean xInRange = (point.x >= position.x && point.x <= position.x + size.x) || (point.x <= position.x && point.x >= position.x + size.x);
        boolean yInRange = (point.y >= position.y && point.y <= position.y + size.y) || (point.y <= position.y && point.y >= position.y + size.y);
        if(!xInRange || !yInRange){ return null; }
        double rayRatio = this.size.x / this.size.y;
        double pointRatio = (point.x - this.position.x) / (point.y - this.position.y);
        if(rayRatio == pointRatio){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    @Override
    public Vec2d collidesWithRay(Ray ray) {
        double slope1 = size.y / size.x;
        double intercept1 = -1*slope1*position.x + position.y;
        double slope2 = ray.getSize().y / ray.getSize().x;
        double intercept2 = -1*slope2*ray.getPosition().x + ray.getPosition().y;
        double intersectionX = (intercept2 - intercept1)/(slope1/slope2);
        boolean xInRange1 = (intersectionX >= position.x && intersectionX <= position.x + size.x) || (intersectionX <= position.x && intersectionX >= position.x + size.x);
        boolean xInRange2 = (intersectionX >= ray.getPosition().x && intersectionX <= ray.getPosition().x + size.x) || (intersectionX <= ray.getPosition().x && intersectionX >= ray.getPosition().x + size.x);
        if(xInRange1 && xInRange2){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    @Override
    public Vec2d getSize() {
        return this.size;
    }

    @Override
    public void setSize(Vec2d newSize) {
        this.size = newSize;
    }

    @Override
    public Vec2d getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        this.position = newPosition;
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = doc.createElement("Shape");
        ele.setAttribute("class", "Ray");
        Element size = this.size.toXml(doc, "Size");
        Element pos = this.position.toXml(doc, "Position");
        ele.appendChild(size);
        ele.appendChild(pos);
        return ele;
    }

    public static Ray fromXml(Element ele){
        if(!ele.getTagName().equals("Shape")){ return null; }
        if(!ele.getAttribute("class").equals("Ray")){ return null; }
        Vec2d size = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "Size").item(0)));
        Vec2d pos = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "Position").item(0)));
        return new Ray(size, pos);
    }
}
