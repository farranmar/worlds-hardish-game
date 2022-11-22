package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

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
    public Vec2d collidesWith(Shape shape) {
        if(shape == null) { return null; }
        return shape.collidesWithAAB(this);
    }

    @Override
    public Vec2d collidesWithAAB(AAB aab) {
        boolean projX = (this.position.x <= aab.getPosition().x+aab.getSize().x) && (this.position.x+this.size.x >= aab.getPosition().x);
        boolean projY = (this.position.y <= aab.getPosition().y+aab.getSize().y) && (this.position.y+this.size.y >= aab.getPosition().y);
        if(!projX || !projY){
            return null;
        }
        Vec2d up = new Vec2d(0, this.position.y+this.getSize().y - aab.getPosition().y);
        Vec2d down = new Vec2d(0, this.position.y - (aab.getPosition().y+aab.getSize().y));
        Vec2d left = new Vec2d(this.position.x+this.getSize().x - aab.getPosition().x, 0);
        Vec2d right = new Vec2d(this.position.x - (aab.getPosition().x+aab.getSize().x), 0);
        return Vec2d.min(up, Vec2d.min(down, Vec2d.min(left, right))).reflect();
    }

    @Override
    public Vec2d collidesWithCircle(Circle circle) {
        Vec2d maxes = new Vec2d(this.position.x + this.getSize().x, this.position.y + this.getSize().y);
        Vec2d nearestPoint = this.clamp(circle.getPosition(), this.position, maxes);
        double dist = circle.getPosition().dist2(nearestPoint);
        if(dist > (circle.getSize().x * circle.getSize().x)){ return null; }
        // if circle center in aab
        if(circle.getPosition().x >= this.position.x && circle.getPosition().x <= this.position.x+this.getSize().x && circle.getPosition().y >= this.position.y && circle.getPosition().y <= this.position.y+this.getSize().y){
            double left = circle.getPosition().x - this.position.x;
            double right = this.position.x+this.getSize().x - circle.getPosition().x;
            double down = this.position.y+this.getSize().y - circle.getPosition().y;
            double up = circle.getPosition().y - this.position.y;
            double min = Math.min(left, Math.min(right, Math.min(up, down)));
            if(min == left){ return new Vec2d(-1 * (circle.getSize().x + left), 0).reflect(); }
            else if(min == right){ return new Vec2d(right + circle.getSize().x, 0).reflect(); }
            else if(min == up){ return new Vec2d(0, -1 * (up + circle.getSize().x)).reflect(); }
            else { return new Vec2d(0, circle.getSize().x + down).reflect(); }
        } else { // circle center not in aab
            double mag = circle.getSize().x - circle.getPosition().dist(nearestPoint);
            double angle = new Vec2d(circle.getPosition().x - nearestPoint.x, circle.getPosition().y - nearestPoint.y).angle();
            return Vec2d.fromPolar(angle, mag).reflect();
        }
    }

    public Vec2d collidesWithPoint(Vec2d point){
        if(point.x < this.position.x || point.x > this.position.x+this.getSize().x || point.y < this.position.y || point.y > this.position.y+this.getSize().y){
            return null;
        }
        double right = this.position.x + this.getSize().x - point.x;
        double left = point.x - this.position.x;
        double up = point.y - this.position.y;
        double down = this.position.y + this.getSize().y - point.y;
        double min = Math.min(right, Math.min(left, Math.min(up, down)));
        if(min == right){ return new Vec2d(-1 * right, 0); }
        else if(min == left){ return new Vec2d(left, 0); }
        else if(min == up){ return new Vec2d(0, -1 * up); }
        else { return new Vec2d(0, down); }
    }

    // mtv irrelevant
    public Vec2d collidesWithRay(Ray ray){
        Vec2d end = ray.getPosition().plus(ray.getSize());
        boolean containsStart = ray.getPosition().x >= this.position.x && ray.getPosition().x <= this.position.x+this.getSize().x && ray.getPosition().y >= this.position.y && ray.getPosition().y <= this.position.y+this.getSize().y;
        boolean containsEnd = end.x >= this.position.x && end.x <= this.position.x+this.getSize().x && end.y >= this.position.y && end.y <= this.position.y+this.getSize().y;
        if(containsStart || containsEnd){
            return new Vec2d(0);
        } else {
            return null;
        }
    }

    private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
        double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
        double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
        return new Vec2d(xClamp, yClamp);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = doc.createElement("Shape");
        ele.setAttribute("class", "AAB");
        Element size = this.size.toXml(doc, "Size");
        Element pos = this.position.toXml(doc, "Position");
        ele.appendChild(size);
        ele.appendChild(pos);
        return ele;
    }

    public static AAB fromXml(Element ele){
        if(!ele.getTagName().equals("Shape")){ return null; }
        if(!ele.getAttribute("class").equals("AAB")){ return null; }
        Vec2d size = Vec2d.fromXml((Element)getTopElementsByTagName(ele, "Size").item(0));
        Vec2d position = Vec2d.fromXml((Element)getTopElementsByTagName(ele, "Position").item(0));
        return new AAB(size, position);
    }
}
