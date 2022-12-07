package last.game;

import engine.game.objects.GameObject;
import engine.game.systems.CollisionSystem;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.game.objects.*;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;

public class EditorWorld extends GameWorld {

    public EditorWorld(){
        super("Last");
        this.size = new Vec2d(1920, 1080);
        this.addSystems();
        this.createObjects();
    }

    private void createObjects() {
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(108,768), new Vec2d(1712, 150));
        this.add(unitMenu);

        Trash trash = new Trash(this, new Vec2d(108), new Vec2d(1712, 942));
        this.add(trash);
    }

    private void addSystems() {
        CollisionSystem collisionSystem = new CollisionSystem();
        this.addSystem(collisionSystem);
        GraphicsSystem graphicsSystem = new GraphicsSystem();
        this.addSystem(graphicsSystem);
        InputSystem inputSystem = new InputSystem();
        this.addSystem(inputSystem);
    }

    @Override
    protected Document toXml() {
        return super.toXml("EditorWorld");
    }

    public EditorWorld(String fileName, Map<String, Class<? extends GameObject>> classMap){
        super(fileName, classMap);
    }

}
