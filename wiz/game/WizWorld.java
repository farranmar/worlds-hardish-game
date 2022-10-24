package wiz.game;

import wiz.game.objects.Map;
import engine.game.systems.CollisionSystem;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.systems.StaticSystem;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import wiz.game.objects.Player;

public class WizWorld extends GameWorld {

    public WizWorld(String name) {
        super(name);
        this.size = new Vec2d(1920,960);
        this.addSystems();
        Map map = new Map(this, new Vec2i(20), new Vec2d(40));
        this.add(map);
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new StaticSystem());
        this.addSystem(new CollisionSystem());
    }
}
