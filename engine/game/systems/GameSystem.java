package engine.game.systems;

import engine.game.components.Tag;
import engine.game.objects.GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.TreeSet;

public class GameSystem {

    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean takesInput = false;

    public GameSystem(){}

    // adds if the obj is relevant to the system; returns whether obj was added or not
    public boolean attemptAdd(GameObject obj){
        if(this.tickable && obj.get(Tag.TICKABLE) != null){
            gameObjects.add(obj);
            return true;
        } else if(this.drawable && obj.get(Tag.DRAWABLE) != null){
            gameObjects.add(obj);
            return true;
        } else if(this.takesInput && (obj.get(Tag.DRAGGABLE) != null || obj.get(Tag.CLICKABLE) != null)){
            gameObjects.add(obj);
            return true;
        }
        return false;
    }

    public boolean isTickable() {
        return tickable;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public boolean takesInput() {
        return takesInput;
    }

    public void onTick(long nanosSinceLastTick){
        for(GameObject obj : gameObjects){
            obj.onTick(nanosSinceLastTick);
        }
    }

    public void onDraw(GraphicsContext g, TreeSet<GameObject> drawOrder){
        for(GameObject obj : drawOrder){
            obj.onDraw(g);
        }
    }

    public void onMousePressed(double x, double y){
        for(GameObject obj : gameObjects){
            obj.onMousePressed(x, y);
        }
    }

    public void onMouseReleased(double x, double y){
        for(GameObject obj : gameObjects){
            obj.onMouseReleased(x, y);
        }
    }

    public void onMouseDragged(double x, double y){
        for(GameObject obj : gameObjects){
            obj.onMouseDragged(x, y);
        }
    }

}