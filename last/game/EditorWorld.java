package last.game;

import engine.game.objects.GameObject;
import engine.game.objects.Grid;
import engine.game.systems.CollisionSystem;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import last.game.objects.*;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;

public class EditorWorld extends GameWorld {

    public EditorWorld(){
        this(false);
    }

    public EditorWorld(boolean empty){
        super("Last");
        this.size = new Vec2d(1920, 1080);
        this.addSystems();
        if(!empty){ this.createObjects(); }
    }

    private void createObjects() {
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(108,768), new Vec2d(1712, 150));
        this.add(unitMenu);

        Trash trash = new Trash(this, new Vec2d(108), new Vec2d(1712, 942));
        this.add(trash);

        Grid grid = new Grid(this, this.size, new Vec2d(0), 108, 192);
        this.add(grid);
    }

    private void addSystems() {
        CollisionSystem collisionSystem = new CollisionSystem();
        this.addSystem(collisionSystem);
        GraphicsSystem graphicsSystem = new GraphicsSystem();
        this.addSystem(graphicsSystem);
        InputSystem inputSystem = new InputSystem();
        this.addSystem(inputSystem);
    }

    private void snapToGrid(){
        for(GameObject obj : this.gameObjects){
            if(obj instanceof UnitMenu || obj instanceof Trash){ continue; }
            if(obj instanceof DeathBall){
                ((DeathBall)obj).snapToGrid();
                continue;
            }
            if(obj instanceof Wall){
                ((Wall)obj).snapToGrid();
                continue;
            }
            double newX = Math.round(obj.getPosition().x/10) * 10;
            double newY = Math.round(obj.getPosition().y/10) * 10;
            obj.setPosition(new Vec2d(newX, newY));
        }
    }

    public void onKeyPressed(KeyEvent e){
        super.onKeyPressed(e);
        if(e.getCode() == KeyCode.TAB){
            this.snapToGrid();
        }
    }

    @Override
    protected Document toXml() {
        return super.toXml("EditorWorld");
    }

    public EditorWorld(String fileName, Map<String, Class<? extends GameObject>> classMap){
        super(fileName, classMap);
    }

}
