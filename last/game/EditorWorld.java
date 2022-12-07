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
        Player player = new Player(this, new Vec2d(30), new Vec2d(100));
        player.setActive(false);
        unitMenu.add(player, "player spawn");
        Wall wall = new Wall(this, new Vec2d(100), new Vec2d(100));
        unitMenu.add(wall, "wall");
        DeathBall deathBall = new DeathBall(this, new Vec2d(100));
        unitMenu.add(deathBall, "deathball");
        Checkpoint checkpoint = new Checkpoint(this, new Vec2d(50), new Vec2d(100), new Vec2d(110));
        unitMenu.add(checkpoint, "checkpoint");
    }

    private void addSystems() {
        CollisionSystem collisionSystem = new CollisionSystem();
//        this.addSystem(collisionSystem);
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
