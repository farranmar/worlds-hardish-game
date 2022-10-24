package wiz.game;

import engine.game.systems.*;
import wiz.display.EndScreen;
import wiz.game.objects.Map;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class WizWorld extends GameWorld {

    private Map map;

    public WizWorld(String name) {
        super(name);
        this.size = new Vec2d(1920,960);
        this.addSystems();
        Map map = new Map(this, new Vec2i(20), new Vec2d(40));
        this.map = map;
        this.centerObj = map.getPlayer();
        this.add(map);
    }

    public GameWorld.Result getResult(){
        return this.map.getResult();
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new StaticSystem());
        this.addSystem(new CollisionSystem());
        this.addSystem(new TickingSystem());
    }
}
