package engine.game.objects;

import alc.game.units.Square;
import engine.game.components.Draggable;
import engine.game.components.GameComponent;
import engine.game.components.Tag;
import engine.game.components.TransformComponent;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class GameObject {

    protected ArrayList<GameComponent> components = new ArrayList<>();
    protected TransformComponent transformComponent;
    protected int drawPriority;
    protected GameObject parent = null;
    protected ArrayList<GameObject> children = new ArrayList<>();

    public GameObject(){
        this.transformComponent = new TransformComponent(new Vec2d(0), new Vec2d(0));
    }

    public void add(GameComponent component){
        this.components.add(component);
    }

    public void remove(GameComponent component){
        this.components.remove(component);
    }

    public void remove(String tagToRemove){
        for(GameComponent component : components){
            if(component.getTag().equals(tagToRemove)){
                this.components.remove(component);
            }
        }
    }

    public GameComponent get(Tag tagToGet){
        for(GameComponent component : components){
            if(component.getTag() == tagToGet){
                return component;
            }
        }
        return null;
    }

    public void reset(){
        for(GameObject child : children){
            child.reset();
        }
    }

    // does NOT set parent or children
    public GameObject clone(){
        GameObject clone = new GameObject();
        for(GameComponent component : components){
            clone.add(component);
        }
        clone.setSize(this.getSize());
        clone.setPosition(this.getPosition());
        clone.setDrawPriority(this.drawPriority);
        return clone;
    }

    public int getDrawPriority(){
        return this.drawPriority;
    }

    public void setDrawPriority(int dp){
        this.drawPriority = dp;
    }

    public TransformComponent getTransform(){
        return this.transformComponent;
    }

    public Vec2d getSize(){
        return this.transformComponent.getSize();
    }

    public void setSize(Vec2d newSize){
        this.transformComponent.setSize(newSize);
    }

    public Vec2d getPosition(){
        return this.transformComponent.getPosition();
    }

    public void setPosition(Vec2d newPosition){
        this.transformComponent.setPosition(newPosition);
    }

    public GameObject getParent(){
        return this.parent;
    }

    public void setParent(GameObject parent){
        this.parent = parent;
    }

    public void addChild(GameObject child){
        this.children.add(child);
    }

    public void setChildren(ArrayList<GameObject> children){
        this.children = children;
    }

    public ArrayList<GameObject> getChildren(){
        return this.children;
    }

    public void onTick(long nanosSincePreviousTick){
        for(GameComponent component : components){
            if(component.isTickable()){
                component.onTick(nanosSincePreviousTick);
            }
        }
        for(GameObject child : children){
            child.onTick(nanosSincePreviousTick);
        }
    }

    public void onDraw(GraphicsContext g){
        for(GameComponent component : components){
            if(component.isDrawable()){
                component.onDraw(g);
            }
        }
        for(GameObject child : children){
            child.onDraw(g);
        }
    }

    public boolean inRange(double x, double y){
        boolean inX = this.getPosition().x <= x && x <= this.getPosition().x + this.getSize().x;
        boolean inY = this.getPosition().y <= y && y <= this.getPosition().y + this.getSize().y;
        return inX && inY;
    }

    public void onMousePressed(double x, double y){
        if(inRange(x, y)){
            for(GameComponent component : components){
                if(component.takesMouseInput()){
                    component.onMousePressed(x, y);
                }
            }
        }
        for(GameObject child : children){
            child.onMousePressed(x, y);
        }
    }

    public void onMouseReleased(double x, double y){
        // don't check whether it's in range so if you're off when you release, it still counts as a release
        for(GameComponent component : components){
            if(component.takesMouseInput()){
                component.onMouseReleased(x, y);
            }
        }
        for(GameObject child : children){
            child.onMouseReleased(x, y);
        }
    }

    public void onMouseDragged(double x, double y){
        // don't check for in range bc if they move the mouse super fast, you want to be able to catch up
        for(GameComponent component : components){
            if(component.takesMouseInput()){
                component.onMouseDragged(x, y, this.transformComponent);
            }
        }
        for(GameObject child : children){
            child.onMouseDragged(x, y);
        }
    }

}
