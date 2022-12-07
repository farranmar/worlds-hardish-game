package last.game;

import engine.game.objects.GameObject;
import engine.game.systems.*;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.game.objects.*;

import java.util.Map;

public class LastWorld extends GameWorld {

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
            if(obj instanceof DeathBall){
                ((DeathBall)clone).setDrawPath(false);
                ((DeathBall)clone).setMoving(true);
            }
            if(obj instanceof Player){
                ((Player)clone).setActive(true);
                ((Player)clone).setCheckpoint(clone.getPosition());
            }
            if(obj instanceof Checkpoint){
                ((Checkpoint)clone).centerSaveSpot(new Vec2d(30));
            }
            this.add(clone);
        }
    }

    private void addSystems(){
        this.addSystem(new CollisionSystem());
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new TickingSystem());
    }

    private void createObjects(){
        Wall upper = new Wall(this, new Vec2d(1320, 30), new Vec2d(300, 200));
        this.add(upper);
        Wall lower = new Wall(this, new Vec2d(1320, 30), new Vec2d(300, 880));
        this.add(lower);
        Wall left = new Wall(this, new Vec2d(30, 710), new Vec2d(300, 200));
        this.add(left);
        Wall right = new Wall(this, new Vec2d(30, 710), new Vec2d(1620, 200));
        this.add(right);
        for (DeathBall deathball : DeathBall.deathBallWall(this, new Vec2d(720, 240), new Vec2d(0, 40), 10)) {
            this.add(deathball);
        }
        for (DeathBall deathball : DeathBall.deathBallWall(this, new Vec2d(720, 680), new Vec2d(0, 40), 5)) {
            this.add(deathball);
        }
        for (DeathBall deathball : DeathBall.deathBallWall(this, new Vec2d(1180, 250), new Vec2d(0, 40), 5)) {
            this.add(deathball);
        }
        for (DeathBall deathball : DeathBall.deathBallWall(this, new Vec2d(1180, 475), new Vec2d(0, 40), 10)) {
            this.add(deathball);
        }
        Checkpoint checkpoint = new Checkpoint(this, new Vec2d(100), new Vec2d(910, 490), new Vec2d(945, 525));
        this.add(checkpoint);
        Checkpoint start = new Checkpoint(this, new Vec2d(100, 650), new Vec2d(330, 230), new Vec2d(365, 540));
        this.add(start);
        EndPoint endPoint = new EndPoint(this, new Vec2d(100, 650), new Vec2d(1520, 230));
        this.add(endPoint);

        Player player = new Player(this, new Vec2d(30), new Vec2d(365, 540));
        this.add(player);
    }

    public LastWorld(String fileName, Map<String, Class<? extends GameObject>> classMap){
        super(fileName, classMap);
    }
}
