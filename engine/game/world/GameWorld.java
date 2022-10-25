package engine.game.world;

import alc.game.units.Unit;
import engine.display.Viewport;
import engine.game.objects.GameObject;
import engine.game.systems.*;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.TreeSet;

public class GameWorld {

    protected Viewport viewport;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected GameObject centerObj;
    protected ArrayList<GameObject> additionQueue = new ArrayList<>();
    protected ArrayList<GameObject> removalQueue = new ArrayList<>();
    protected ArrayList<GameSystem> systems = new ArrayList<>();
    protected TreeSet<GameObject> drawOrder = new TreeSet<>(new DrawOrderHelper());
    protected Vec2d size;
    protected String name;
    protected Result result = Result.PLAYING;

    public enum Result {
        VICTORY,
        DEFEAT,
        PLAYING
    }

    public GameWorld(String name){
        this.name = name;
    }

    public void add(GameObject obj){
        if(this.gameObjects.contains(obj)){ return; }
        this.gameObjects.add(obj);
        if(obj.getWorldDraw()){ drawOrder.add(obj); }
        for(GameSystem system : systems){
            boolean added = system.attemptAdd(obj);
        }
    }

    public void centerOn(GameObject obj){
        this.centerObj = obj;
        this.add(obj);
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
        this.removalQueue.add(obj);
    }

    protected void removeQueue(){
        for(GameObject obj : this.removalQueue){
            this.remove(obj);
        }
        this.removalQueue.clear();
    }

    public void remove(GameObject obj){
        this.gameObjects.remove(obj);
        this.drawOrder.remove(obj);
        for(GameSystem system : systems){
            system.remove(obj);
        }
    }

    public void addSystem(GameSystem sys){
        this.systems.add(sys);
    }

    public void setViewport(Viewport vp){
        this.viewport = vp;
    }
    
    public void reset(){
        for(GameObject obj : gameObjects){
            obj.reset();
        }
    }

    public Vec2d getSize(){
        return this.size;
    }

    public Result getResult(){
        return this.result;
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

    public void onLateTick(){
        if(centerObj != null){
            Vec2d centerPos = new Vec2d(this.centerObj.getPosition().x + this.centerObj.getSize().x/2, this.centerObj.getPosition().y + this.centerObj.getSize().y/2);
            Vec2d displaySize = this.viewport.getDisplaySize();
            double newX = 0;
            double newY = 0;
            if(centerPos.x - displaySize.x/2 < 0){
                newX = 0;
            } else if(centerPos.x + displaySize.x/2 > this.size.x){
                newX = this.size.x - displaySize.x;
            } else {
                newX = centerPos.x - displaySize.x/2;
            }
            if(centerPos.y - displaySize.y/2 < 0){
                newY = 0;
            } else if(centerPos.y + displaySize.y/2 > this.size.y){
                newY = this.size.y - displaySize.y;
            } else {
                newY = centerPos.y - displaySize.y/2;
            }
            this.viewport.setDisplayPosition(new Vec2d(newX, newY));
        }
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

    public void onKeyPressed(KeyEvent e){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onKeyPressed(e);
            }
        }
    }

}
