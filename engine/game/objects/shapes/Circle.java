package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

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
    public Vec2d collidesWith(Shape shape) {
        if(shape == null) { return null; }
        return shape.collidesWithCircle(this);
    }

    @Override
    public Vec2d collidesWithAAB(AAB aab) {
        Vec2d maxes = new Vec2d(aab.getPosition().x + aab.getSize().x, aab.getPosition().y + aab.getSize().y);
        Vec2d nearestPoint = this.clamp(this.position, aab.getPosition(), maxes);
        double dist = this.position.dist2(nearestPoint);
        if(dist > (this.radius * this.radius)){ return null; }
        // if circle center in aab
        if(this.position.x >= aab.getPosition().x && this.position.x <= aab.getPosition().x+aab.getSize().x && this.position.y >= aab.getPosition().y && this.position.y <= aab.getPosition().y+aab.getSize().y){
            double left = this.position.x - aab.getPosition().x;
            double right = aab.getPosition().x+aab.getSize().x - this.position.x;
            double down = aab.getPosition().y+aab.getSize().y - this.position.y;
            double up = this.position.y - aab.getPosition().y;
            double min = Math.min(left, Math.min(right, Math.min(up, down)));
            if(min == left){ return new Vec2d(-1 * (this.radius + left), 0).reflect(); }
            else if(min == right){ return new Vec2d(right + this.radius, 0).reflect(); }
            else if(min == up){ return new Vec2d(0, -1 * (up + this.radius)).reflect(); }
            else { return new Vec2d(0, this.radius + down).reflect(); }
        } else { // circle center not in aab
            double mag = this.radius - this.position.dist(nearestPoint);
            double angle = new Vec2d(this.position.x - nearestPoint.x, this.position.y - nearestPoint.y).angle();
            return Vec2d.fromPolar(angle, mag).reflect();
        }
    }

    @Override
    public Vec2d collidesWithCircle(Circle circle) {
        if(this.position.dist(circle.getPosition()) > this.radius + circle.getSize().x){ return null; }
        double mag = this.radius + circle.getSize().x - this.position.dist(circle.getPosition());
        double angle = new Vec2d(this.position.x - circle.getPosition().x, this.position.y - circle.getPosition().y).angle();
        return Vec2d.fromPolar(angle, mag);
    }

    public Vec2d collidesWithPoint(Vec2d point){
        if(this.getPosition().dist(point) > this.radius){ return null; }
        double distToEdge = this.radius - (this.getPosition().dist(point));
        double angle = new debugger.support.Vec2d(point.x - this.getPosition().x, point.y - this.getPosition().y).angle();
        Vec2d vec = new Vec2d(distToEdge, 0);
        return vec.rotate(angle);
    }

    public Vec2d collidesWithRay(Ray ray){
        Vec2d end = ray.getPosition().plus(ray.getSize());
        boolean containsStart = ray.getPosition().dist(this.position) <= this.radius;
        boolean containsEnd = end.dist(this.position) <= this.radius;
        if(containsStart || containsEnd){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    @Override
    public Vec2d collidesWithPolygon(Polygon polygon) {
        return null;
    }

    private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
        double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
        double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
        return new Vec2d(xClamp, yClamp);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = doc.createElement("Shape");
        ele.setAttribute("class", "Circle");
        Element radius = new Vec2d(this.radius).toXml(doc, "Radius");
        Element pos = this.position.toXml(doc, "Position");
        ele.appendChild(radius);
        ele.appendChild(pos);
        return ele;
    }

    public static Circle fromXml(Element ele){
        if(!ele.getTagName().equals("Shape")){ return null; }
        if(!ele.getAttribute("class").equals("Circle")){ return null; }
        Vec2d radius = Vec2d.fromXml((getTopElementsByTagName(ele, "Radius").get(0)));
        Vec2d position = Vec2d.fromXml((getTopElementsByTagName(ele, "Position").get(0)));
        return new Circle(radius.x, position);
    }
}
