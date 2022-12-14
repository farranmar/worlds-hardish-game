package engine.game.objects.shapes;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Shape {

    Vec2d collidesWith(Shape shape);

    Vec2d collidesWithAAB(AAB aab);

    Vec2d collidesWithCircle(Circle circle);

    Vec2d collidesWithPoint(Vec2d point);

    Vec2d collidesWithRay(Ray ray);

    Vec2d collidesWithPolygon(Polygon polygon);

    Vec2d getSize();

    void setSize(Vec2d newSize);

    Vec2d getPosition();

    void setPosition(Vec2d newPosition);

    Element toXml(Document doc);

    static Shape fromXml(Element ele){
        if(!ele.getTagName().equals("Shape")){ return null; }
        Shape shape = null;
        if(ele.getAttribute("class").equals("Circle")){
            shape = Circle.fromXml(ele);
        } else if(ele.getAttribute("class").equals("AAB")){
            shape = AAB.fromXml(ele);
        } else if(ele.getAttribute("class").equals("Ray")){
            shape = Ray.fromXml(ele);
        } else if(ele.getAttribute("class").equals("Polygon")){
            shape = Polygon.fromXml(ele);
        }
        return shape;
    }

}
