package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;

public class Draggable extends GameComponent {

    private boolean dragging = false; // whether object is currently being dragged
    private double preX;
    private double preY;

    public Draggable(){
        this.tag = Tag.DRAGGABLE;
        this.mouseInput = true;
    }

    // assumes mouse is in range of object already
    public void onMousePressed(double x, double y){
        this.preX = x;
        this.preY = y;
        this.dragging = true;
    }

    public void onMouseReleased(double x, double y){
        this.dragging = false;
    }

    public void onMouseDragged(double x, double y, TransformComponent tc){
        if(dragging){

            tc.translate(x-preX, y-preY);
            preX = x;
            preY = y;
        }
    }

}
