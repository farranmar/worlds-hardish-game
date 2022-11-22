package engine.game.components;

import engine.game.objects.GameObject;
import engine.game.objects.shapes.Shape;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class DrawComponent extends GameComponent {

    protected Color color;

    public DrawComponent(){
        super(ComponentTag.DRAW);
        this.drawable = true;
        this.color = Color.rgb(0,0,0);
    }

    public DrawComponent(Color color){
        super(ComponentTag.DRAW);
        this.drawable = true;
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color newColor){
        this.color = newColor;
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        Element color = GameObject.colorToXml(doc, this.color);
        ele.appendChild(color);
        return ele;
    }

    public static DrawComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("DRAW")){ return null; }
        Color color = GameObject.colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        DrawComponent drawComponent = new DrawComponent(color);
        drawComponent.setConstants(ele);
        return drawComponent;
    }

}
