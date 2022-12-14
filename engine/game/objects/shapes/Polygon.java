package engine.game.objects.shapes;

import engine.game.world.GameWorld;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;


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
        return shape.collidesWithPolygon(this);
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
        boolean inside = true;
        for(int i = 0; i < this.getNumPoints(); i++){
            Vec2d base = this.getPoint(i);
            Vec2d end = this.getPoint((i+1) % this.getNumPoints());
            Vec2d v = end.minus(base);
            Vec2d p = point.minus(base);
            if(v.cross(p) > 0){
                inside = false;
                break;
            }
        }
        if(!inside){ return null; }
        return new Vec2d(0);
    }

    @Override
    public Vec2d collidesWithRay(Ray ray) {
        double minDist = Double.MAX_VALUE;
        Vec2d p = ray.getPosition();
        Vec2d d = ray.getDirection();
        for(int i = 0; i < this.getNumPoints(); i++){
            Vec2d a = this.getPoint(i);
            Vec2d b = this.getPoint((i+1) % this.getNumPoints());
            if(ray.straddles(a, b)){
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
    public Vec2d collidesWithPolygon(Polygon polygon) {
        return null;
    }

    @Override
    public Vec2d getSize() {
        return null;
    }

    @Override
    public void setSize(Vec2d newSize) { }

    @Override
    public Vec2d getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vec2d newPosition) { }

    @Override
    public Element toXml(Document doc) {
        Element ele = doc.createElement("Shape");
        ele.setAttribute("class", "Polygon");
        Element points = doc.createElement("Points");
        points.setAttribute("length", this.getNumPoints()+"");
        for(Vec2d point : this.points){
            points.appendChild(point.toXml(doc, "Point"));
        }
        ele.appendChild(points);
        return ele;
    }

    public static Polygon fromXml(Element ele){
        int length = Integer.parseInt(ele.getAttribute("length"));
        Vec2d[] points = new Vec2d[length];
        int next = 0;
        Element pointsEle = GameWorld.getTopElementsByTagName(ele, "Points").get(0);
        List<Element> pointsList = GameWorld.getTopElementsByTagName(pointsEle, "Point");
        for(Element point : pointsList){
            points[next] = Vec2d.fromXml(point);
            next++;
        }
        return new Polygon(points);
    }
}
