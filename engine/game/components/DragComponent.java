package engine.game.components;

import engine.game.objects.GameObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DragComponent extends GameComponent {

    protected boolean dragging = false; // whether object is currently being dragged
    protected double preX;
    protected double preY;
    protected GameObject obj;

    public DragComponent(GameObject obj){
        super(ComponentTag.DRAG);
        this.mouseInput = true;
        this.obj = obj;
    }

    // assumes mouse is in range of object already
    public void onMousePressed(double x, double y){
        this.preX = x;
        this.preY = y;
        this.dragging = true;
        this.obj.setCollidable(false);
    }

    public void onMouseReleased(double x, double y){
        this.dragging = false;
    }

    public void onMouseDragged(double x, double y, GameObject obj){
        if(dragging){
            obj.translate(x-preX, y-preY);
            preX = x;
            preY = y;
        }
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public double getPreX() {
        return preX;
    }

    public void setPreX(double preX) {
        this.preX = preX;
    }

    public double getPreY() {
        return preY;
    }

    public void setPreY(double preY) {
        this.preY = preY;
    }

    @Override
    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        ele.setAttribute("dragging", this.dragging+"");
        ele.setAttribute("preX", this.preX+"");
        ele.setAttribute("preY", this.preY+"");
        return ele;
    }

    public static DragComponent fromXml(Element ele, GameObject obj){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("DRAG")){ return null; }
        DragComponent dragComponent = new DragComponent(obj);
        dragComponent.setConstants(ele);
        dragComponent.setDragging(Boolean.parseBoolean(ele.getAttribute("dragging")));
        dragComponent.setPreX(Double.parseDouble(ele.getAttribute("preX")));
        dragComponent.setPreY(Double.parseDouble(ele.getAttribute("preY")));
        return dragComponent;
    }
}
