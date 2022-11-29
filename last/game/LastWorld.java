package last.game;

import engine.game.objects.GameObject;
import engine.game.systems.*;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.game.objects.Platform;
import last.game.objects.Unit;
import last.game.objects.UnitMenu;

import java.util.HashMap;
import java.util.Map;

public class LastWorld extends GameWorld {

    private static final Map<String, Class<? extends GameObject>> classMap = initializeClassMap();
    private static final Color platformColor = Color.rgb(99, 176, 205);

    public LastWorld(){
        this(false);
    }

    public LastWorld(boolean empty){
        super("Last");
        this.size = new Vec2d(1920, 1080);
        if(!empty){
            this.addSystems();
            this.createObjects();
        }
    }

    public LastWorld(EditorWorld editorWorld){
        super("Last");
        this.size = new Vec2d(1920, 1080);
        this.addSystems();
        for(GameObject obj : editorWorld.getGameObjects()){
            if(obj instanceof UnitMenu){ continue; }
            GameObject clone = obj.clone();
            this.add(clone);
        }
    }

    private void addSystems(){
        this.addSystem(new CollisionSystem());
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new TickingSystem());
    }

    private static Map<String, Class<? extends GameObject>> initializeClassMap(){
        Map<String, Class<? extends GameObject>> ret = new HashMap<>();
        ret.put("Unit", Unit.class);
        ret.put("UnitMenu", UnitMenu.class);
        return ret;
    }

    private void createObjects(){
        Platform upper = new Platform(this, new Vec2d(420, 30), new Vec2d(520), platformColor);
        this.add(upper);
        Platform lower = new Platform(this, new Vec2d(420, 30), new Vec2d(980, 560), platformColor);
        this.add(lower);
    }

    public static Map<String, Class<? extends GameObject>> getClassMap(){
        return classMap;
    }

    @Override
    public void onMousePressed(double x, double y) {
        super.onMousePressed(x, y);
        Platform newPlatform = new Platform(this, new Vec2d(20), new Vec2d(x,y), Color.CORAL);
        this.add(newPlatform);
    }

    public LastWorld(String fileName, Map<String, Class<? extends GameObject>> classMap){
        super(fileName, classMap);
    }
}
