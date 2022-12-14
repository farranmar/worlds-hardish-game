package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Ray implements Shape {

    private Vec2d position;
    private Vec2d direction;

     /*
        Vec2d's returned by collision functions contains distance to collision in both x and y coord
        (null if no collision)
     */

    public Ray(Vec2d direction, Vec2d position){
        this.direction = direction;
        this.position = position;
    }

    @Override
    public Vec2d collidesWith(Shape shape) {
        return shape.collidesWithRay(this);
    }

    @Override
    public Vec2d collidesWithAAB(AAB aab) {
        Vec2d topLeft = aab.getPosition();
        Vec2d botLeft = aab.getPosition().plus(new Vec2d(0, aab.getSize().y));
        Vec2d botRight = aab.getPosition().plus(aab.getSize());
        Vec2d topRight = aab.getPosition().plus(new Vec2d(aab.getSize().x, 0));
        Polygon polyAab = new Polygon(topLeft, botLeft, botRight, topRight);
        return this.collidesWithPolygon(polyAab);
    }

    @Override
    public Vec2d collidesWithCircle(Circle circle) {
        Vec2d proj = circle.getPosition().projectOntoLine(this.position, this.position.plus(this.direction));
        if(this.position.dist(circle.getPosition()) <= circle.getSize().x){
            double x = proj.dist(circle.getPosition());
            double r = circle.getSize().x;
            double L = proj.dist(this.position);
            if((proj.x-this.position.x)*this.direction.x < 0 || (proj.y-this.position.y)*this.direction.y < 0){ L = -1*L; }
            Vec2d intersection = this.position.plus(this.direction.smult(Math.sqrt(r*r - x*x) + L));
            return new Vec2d(this.position.dist(intersection));
        } else {
            // if projection negative, no collision
            if((proj.x-this.position.x)*this.direction.x < 0 || (proj.y-this.position.y)*this.direction.y < 0){ return null; }
            // if projection outside of circle, no collision
            if(proj.dist(circle.getPosition()) > circle.getSize().x){ return null; }
            double x = proj.dist(circle.getPosition());
            double r = circle.getSize().x;
            double L = proj.dist(this.position);
            Vec2d intersection = this.position.plus(this.direction.smult(L - Math.sqrt(r*r - x*x)));
            return new Vec2d(this.position.dist(intersection));
        }
    }

    @Override
    public Vec2d collidesWithPoint(Vec2d point) {
        double slope1 = direction.y / direction.x;
        double intercept1 = -1*slope1*position.x + position.y;
        if(slope1 * point.x + intercept1 == point.y){
            double dist = this.position.dist(point);
            return new Vec2d(dist);
        }
        return null;
    }

    @Override
    public Vec2d collidesWithRay(Ray ray) {
        double slope1 = direction.y / direction.x;
        double intercept1 = -1*slope1*position.x + position.y;
        double slope2 = ray.getSize().y / ray.getSize().x;
        double intercept2 = -1*slope2*ray.getPosition().x + ray.getPosition().y;
        double intersectionX = (intercept2 - intercept1)/(slope1/slope2);
        double intersectionY = slope1*intersectionX+intercept1;
        Vec2d intersection = new Vec2d(intersectionX, intersectionY);
        // if intersection in forward direction for each ray
        if((intersectionX-position.x)*direction.x > 0 && (intersectionX-ray.getPosition().x)*ray.getDirection().x > 0){
            return new Vec2d(this.position.dist(intersection));
        }
        return null;
    }

    @Override
    public Vec2d collidesWithPolygon(Polygon polygon) {
        double minDist = Double.MAX_VALUE;
        Vec2d p = this.position;
        Vec2d d = this.direction;
        for(int i = 0; i < polygon.getNumPoints(); i++){
            Vec2d a = polygon.getPoint(i);
            Vec2d b = polygon.getPoint((i+1) % polygon.getNumPoints());
            if(this.straddles(a, b)){
                Vec2d m = b.minus(a).normalize();
                Vec2d n = m.perpendicular().normalize();
                double t = b.minus(p).dot(n) / d.dot(n);
                if(t < 0){ continue; }
                Vec2d q = p.plus(d.smult(t));
                double dist = p.dist(q);
                if(dist < minDist){
                    minDist = dist;
                }
            }
        }
        if(minDist == Double.MAX_VALUE){ minDist = -1; }
        return new Vec2d(minDist);
    }

    @Override
    public Vec2d getSize() {
        return this.direction;
    }

    @Override
    public void setSize(Vec2d newSize) {
        this.direction = newSize;
    }

    public Vec2d getDirection() {
        return this.direction;
    }

    public void setDirection(Vec2d newDir) {
        this.direction = newDir;
    }

    @Override
    public Vec2d getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        this.position = newPosition;
    }

    public boolean straddles(Vec2d a, Vec2d b){
        Vec2d p = this.position;
        Vec2d d = this.direction;
        double leftCross = a.minus(p).cross(d);
        double rightCross = b.minus(p).cross(d);
        if(leftCross * rightCross > 0){
            return false;
        }
        return true;
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = doc.createElement("Shape");
        ele.setAttribute("class", "Ray");
        Element dir = this.direction.toXml(doc, "Direction");
        Element pos = this.position.toXml(doc, "Position");
        ele.appendChild(dir);
        ele.appendChild(pos);
        return ele;
    }

    public static Ray fromXml(Element ele){
        if(!ele.getTagName().equals("Shape")){ return null; }
        if(!ele.getAttribute("class").equals("Ray")){ return null; }
        Vec2d dir = Vec2d.fromXml(getTopElementsByTagName(ele, "Direction").get(0));
        Vec2d pos = Vec2d.fromXml(getTopElementsByTagName(ele, "Position").get(0));
        return new Ray(dir, pos);
    }
}
