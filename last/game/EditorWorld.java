package last.game;

import engine.game.objects.GameObject;
import engine.game.systems.CollisionSystem;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.game.objects.Unit;
import last.game.objects.UnitMenu;
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
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(54,384), new Vec2d(1328, 318));
        unitMenu.setDrawPriority(1);
        this.add(unitMenu);
        Unit pink = new Unit(this, Color.rgb(255,184,209));
        unitMenu.add(pink);
        Unit yellow = new Unit(this, Color.rgb(226,235,152));
        unitMenu.add(yellow);
        Unit green = new Unit(this, Color.rgb(186,217,162));
        unitMenu.add(green);
        Unit blue = new Unit(this, Color.rgb(152,193,217));
        unitMenu.add(blue);
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
