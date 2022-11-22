package engine.game.components;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.tree.TreeNode;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class TransformComponent extends GameComponent{

    private Vec2d size;
    private Vec2d position;

    public TransformComponent(Vec2d size, Vec2d position){
        super(ComponentTag.TRANSFORM);
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

    public void translate(double dx, double dy){
        this.position = new Vec2d(this.position.x + dx, this.position.y + dy);
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        Element pos = this.position.toXml(doc, "Position");
        ele.appendChild(pos);
        Element size = this.size.toXml(doc, "Size");
        ele.appendChild(size);
        return ele;
    }

    public static TransformComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("TRANSFORM")){ return null; }
        Vec2d pos = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "Position").item(0)));
        Vec2d size = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "Size").item(0)));
        return new TransformComponent(size, pos);
    }

}
