package engine.game.components;

import engine.game.objects.GameObject;

public class DragComponent extends GameComponent {

    private boolean dragging = false; // whether object is currently being dragged
    private double preX;
    private double preY;
    private GameObject obj;

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
        this.obj.setCollidable(true);
    }

    public void onMouseDragged(double x, double y, GameObject obj){
        if(dragging){
            obj.translate(x-preX, y-preY);
            preX = x;
            preY = y;
        }
    }

}
