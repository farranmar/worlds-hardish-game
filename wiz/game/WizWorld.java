package wiz.game;

import engine.game.systems.*;
import wiz.display.EndScreen;
import wiz.game.objects.Map;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class WizWorld extends GameWorld {

    private Map map;
    private static final Vec2i mapDims = new Vec2i(30);
    private static final int depth = 5;
    private static final Vec2d tileSize = new Vec2d(40);

    public WizWorld(String name, long seed) {
        super(name);
        this.size = new Vec2d(mapDims.x*tileSize.x,mapDims.y*tileSize.y);
        this.addSystems();
        Map map = new Map(this, mapDims, depth, tileSize, seed);
        this.map = map;
        this.centerObj = map.getPlayer();
        map.setNumEnemies(10);
        this.add(map);
    }

    public GameWorld.Result getResult(){
        return this.map.getResult();
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new CollisionSystem());
        this.addSystem(new TickingSystem());
    }
}
