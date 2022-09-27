package engine.game.world;

import engine.display.Viewport;
import engine.game.objects.GameObject;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.systems.StaticSystem;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import engine.game.systems.GameSystem;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.TreeSet;

public class GameWorld {

    protected Viewport viewport;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected ArrayList<GameSystem> systems = new ArrayList<>();
    protected TreeSet<GameObject> drawOrder = new TreeSet<>(new DrawOrderHelper());
    protected Vec2d size;
    protected String name;

    public GameWorld(String name){
        this.name = name;
        systems.add(new GraphicsSystem());
        systems.add(new InputSystem());
        systems.add(new StaticSystem());
    }

    public void add(GameObject obj){
        this.gameObjects.add(obj);
        drawOrder.add(obj);
        for(GameSystem system : systems){
            system.attemptAdd(obj);
        }
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
            if(sys.isTickable()){
                sys.onTick(nanosSinceLastTick);
            }
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

}
