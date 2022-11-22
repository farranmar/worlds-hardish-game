package nin.game.objects;

import engine.game.components.CollideComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.Ray;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import nin.game.NinWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class GravityRay extends GameObject {

    Ray ray;

    public GravityRay(GameWorld world, Block block, Vec2d size, Vec2d position) {
        super(world, size, position);
        this.ray = new Ray(size, position);
        this.parent = block;
        this.add(new CollideComponent(this.ray));
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        super.setPosition(newPosition);
        this.ray.setPosition(newPosition);
    }

    @Override
    public void setParent(GameObject parent) {
        assert(parent instanceof Block);
        super.setParent(parent);
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        if(obj instanceof Player){ return; }
        super.onCollide(obj, mtv);
        if(obj instanceof Platform){
            ((Block)parent).setGravity(false);
        }
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "GravityRay");
        Element ray = this.ray.toXml(doc);
        ele.appendChild(ray);
        return ele;
    }

    public GravityRay(Element ele, GameWorld world){
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("GravityRay")){ return; }
        this.gameWorld = world;
        this.setConstantsXml(ele);

        Element componentsEle = (Element)(getTopElementsByTagName(ele, "Components").item(0));
        this.addComponentsXml(componentsEle);

        Element childrenEle = (Element)(getTopElementsByTagName(ele, "Children").item(0));
        this.setChildrenXml(childrenEle, NinWorld.getClassMap());

        Element rayEle = (Element)(getTopElementsByTagName(ele, "Shape").item(0));
        Ray ray = Ray.fromXml(rayEle);
        this.ray = ray;
    }
}
