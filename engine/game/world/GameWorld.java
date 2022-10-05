package engine.game.world;

import engine.display.Viewport;
import engine.game.objects.GameObject;
import engine.game.systems.*;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.TreeSet;

public class GameWorld {

    protected Viewport viewport;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected ArrayList<GameObject> additionQueue = new ArrayList<>();
    protected ArrayList<GameObject> removalQueue = new ArrayList<>();
    protected ArrayList<GameSystem> systems = new ArrayList<>();
    protected TreeSet<GameObject> drawOrder = new TreeSet<>(new DrawOrderHelper());
    protected Vec2d size;
    protected String name;

    public GameWorld(String name){
        this.name = name;
    }

    public void add(GameObject obj){
        if(this.gameObjects.contains(obj)){ return; }
        this.gameObjects.add(obj);
        drawOrder.add(obj);
        for(GameSystem system : systems){
            system.attemptAdd(obj);
        }
    }

    public void addToAdditionQueue(GameObject obj){
        this.additionQueue.add(obj);
    }

    protected void addQueue(){
        for(GameObject obj : this.additionQueue){
            this.add(obj);
        }
        this.additionQueue.clear();
    }

    public void addToRemovalQueue(GameObject obj){
        System.out.println("adding to removal queue: "+obj);
        this.removalQueue.add(obj);
    }

    protected void removeQueue(){
        System.out.println("about to remove "+this.removalQueue.size()+" objects from world");
        for(GameObject obj : this.removalQueue){
            this.remove(obj);
        }
        this.removalQueue.clear();
    }

    public void remove(GameObject obj){
        System.out.println("gameObjects before remove: "+this.gameObjects);
        this.gameObjects.remove(obj);
        System.out.println("gameObjects after remove: "+this.gameObjects);
        System.out.println("drawOrder before remove: "+this.drawOrder);
        this.drawOrder.remove(obj);
        System.out.println("drawOrder after remove: "+this.drawOrder);
        for(GameSystem system : systems){
            system.remove(obj);
        }
    }

    public void addSystem(GameSystem sys){
        this.systems.add(sys);
    }
    
    public void reset(){
        for(GameObject obj : gameObjects){
            obj.reset();
        }
    }

    public Vec2d getSize(){
        return this.size;
    }

    public void onTick(long nanosSinceLastTick){
        for(GameSystem sys : systems){
            if(sys.isTickable() || sys.isCollidable()){
                sys.onTick(nanosSinceLastTick);
            }
        }
        this.addQueue();
        this.removeQueue();
    }

    public void onDraw(GraphicsContext g){
        for(GameSystem sys : systems){
            if(sys.isDrawable()){
                sys.onDraw(g, this.drawOrder);
            }
        }
    }

    public void onMousePressed(double x, double y) {
        for (GameSystem sys : systems) {
            if (sys.takesInput()) {
                sys.onMousePressed(x, y);
            }
        }
    }

    public void onMouseReleased(double x, double y){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onMouseReleased(x, y);
            }
        }
    }

    public void onMouseDragged(double x, double y){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onMouseDragged(x, y);
            }
        }
    }

}
