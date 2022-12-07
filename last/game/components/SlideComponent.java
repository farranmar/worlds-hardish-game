package last.game.components;

import engine.game.components.ComponentTag;
import engine.game.components.DragComponent;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import last.game.objects.DeathBall;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SlideComponent extends DragComponent {

    Vec2d axisStart;
    Vec2d axisEnd;
    double slideRatio;

    public SlideComponent(GameObject gameObject, Vec2d axisStart, Vec2d axisEnd, double ratio) {
        super(gameObject);
        this.tag = ComponentTag.SLIDE;
        this.axisStart = axisStart;
        this.axisEnd = axisEnd;
        this.slideRatio = ratio;
    }

    @Override
    public void onMouseDragged(double x, double y, GameObject obj) {
        if (dragging) {
            Vec2d ourAxis = axisEnd.minus(axisStart).normalize();
            Vec2d startOnAxis = axisStart.projectOnto(ourAxis);
            Vec2d endOnAxis = axisEnd.projectOnto(ourAxis);
            Vec2d destination = new Vec2d(x,y).projectOnto(ourAxis);
            double newRatio = destination.minus(startOnAxis).x / endOnAxis.minus(startOnAxis).x;
            if (newRatio < 0) {
                slideRatio = 0;
            } else if (newRatio > 1) {
                slideRatio = 1;
            } else {
                slideRatio = newRatio;
            }
            assert(obj instanceof DeathBall);
            ((DeathBall)obj).setStartRatio(this.slideRatio);
            preX = x;
            preY = y;
        }
    }

    public void updateEndpoint(Vec2d startPos, Vec2d endPos) {
        axisStart = startPos;
        axisEnd = endPos;
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("slideRatio", slideRatio+"");
        ele.appendChild(axisStart.toXml(doc, "axisStart"));
        ele.appendChild(axisEnd.toXml(doc, "axisEnd"));
        return ele;
    }

    public static SlideComponent fromXml(Element ele, GameObject obj){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("SLIDE")){ return null; }
        Vec2d axisStart = Vec2d.fromXml(GameWorld.getTopElementsByTagName(ele, "axisStart").get(0));
        Vec2d axisEnd = Vec2d.fromXml(GameWorld.getTopElementsByTagName(ele, "axisEnd").get(0));
        double slideRatio = Double.parseDouble(ele.getAttribute("slideRatio"));
        SlideComponent slideComponent = new SlideComponent(obj, axisStart, axisEnd, slideRatio);
        slideComponent.setConstants(ele);
        slideComponent.setDragging(Boolean.parseBoolean(ele.getAttribute("dragging")));
        slideComponent.setPreX(Double.parseDouble(ele.getAttribute("preX")));
        slideComponent.setPreY(Double.parseDouble(ele.getAttribute("preY")));
        return slideComponent;
    }
}
