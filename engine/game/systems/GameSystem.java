package engine.game.systems;

import engine.game.components.ComponentTag;
import engine.game.objects.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Affine;
import last.game.objects.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.TreeSet;

public class GameSystem {

    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean takesInput = false;
    protected boolean collidable = false;

    public GameSystem(){}

    // adds if the obj is relevant to the system; returns whether obj was added or not
    public boolean attemptAdd(GameObject obj){
        if(this.collidable && obj.get(ComponentTag.COLLIDE) != null){
            gameObjects.add(obj);
            return true;
        }
        if(this.tickable && !this.collidable && obj.get(ComponentTag.TICK) != null){
            gameObjects.add(obj);
            return true;
        }
        if(this.drawable && obj.getWorldDraw() && obj.get(ComponentTag.DRAW) != null){
            gameObjects.add(obj);
            return true;
        }
        if(this.takesInput && (obj.get(ComponentTag.DRAG) != null || obj.get(ComponentTag.SLIDE) != null || obj.get(ComponentTag.CLICK) != null || obj.get(ComponentTag.KEY) != null)){
            gameObjects.add(obj);
            return true;
        }
        return false;
    }

    public void remove(GameObject obj){
        this.gameObjects.remove(obj);
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

    public boolean isCollidable() {
        return collidable;
    }

    public void setTickable(boolean tickable) {
        this.tickable = tickable;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public void setTakesInput(boolean takesInput) {
        this.takesInput = takesInput;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void onTick(long nanosSinceLastTick){
        if(!tickable){ return; }
        for(GameObject obj : gameObjects){
            obj.onTick(nanosSinceLastTick);
        }
    }

    public void onDraw(GraphicsContext g, TreeSet<GameObject> drawOrder){
        for(GameObject obj : drawOrder){
            if(obj.isFloating()){
                Affine ogTransform = g.getTransform();
                Affine identity = new Affine();
                identity.setToIdentity();
                g.setTransform(identity);
                obj.onDraw(g);
                g.setTransform(ogTransform);
            } else {
                obj.onDraw(g);
            }
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

    public void onKeyPressed(KeyEvent e){
        for(GameObject obj : gameObjects){
            obj.onKeyPressed(e);
        }
    }

    public void onKeyReleased(KeyEvent e){
        for(GameObject obj : gameObjects){
            obj.onKeyReleased(e);
        }
    }

    public void setConstants(Element ele){
        this.setTickable(Boolean.parseBoolean(ele.getAttribute("tickable")));
        this.setDrawable(Boolean.parseBoolean(ele.getAttribute("drawable")));
        this.setTakesInput(Boolean.parseBoolean(ele.getAttribute("takesInput")));
        this.setCollidable(Boolean.parseBoolean(ele.getAttribute("collidable")));
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("System");
        ele.setAttribute("collidable", this.collidable+"");
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("takesInput", this.takesInput+"");
        return ele;
    }

    public static GameSystem fromXml(Element ele){
        GameSystem sys;
        if(Boolean.parseBoolean(ele.getAttribute("collidable"))){
            sys = new CollisionSystem();
        } else if(Boolean.parseBoolean(ele.getAttribute("drawable"))){
            sys = new GraphicsSystem();
        } else if(Boolean.parseBoolean(ele.getAttribute("takesInput"))){
            sys = new InputSystem();
        } else if(Boolean.parseBoolean(ele.getAttribute("tickable"))){
            sys = new TickingSystem();
        } else {
            sys = new GameSystem();
        }
        sys.setConstants(ele);
        return sys;
    }

}
