package nin.game;

import engine.game.systems.*;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import nin.game.objects.Platform;
import nin.game.objects.Player;

public class NinWorld extends GameWorld {

    private Player player;
    private static final Color platformColor = Color.rgb(99, 176, 205);

    public NinWorld(){
        super("Nin");
        this.size = new Vec2d(1920, 1080);
        this.addSystems();
        this.createObjects();
    }

    private void createObjects(){
        Player p = new Player(this, new Vec2d(30), new Vec2d(540, 490));
        this.add(p);
        this.player = p;
        Platform upper = new Platform(this, new Vec2d(420, 30), new Vec2d(520), platformColor);
        this.add(upper);
        Platform lower = new Platform(this, new Vec2d(420, 30), new Vec2d(980, 560), platformColor);
        this.add(lower);
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new StaticSystem());
        this.addSystem(new CollisionSystem());
        this.addSystem(new TickingSystem());
    }

}
