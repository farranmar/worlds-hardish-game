package wiz.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.components.SpriteComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import wiz.resources.Resource;

public class Projectile extends GameObject {

    private String spriteFileName;
    private Direction direction;
    private double speed = 5;
    private static final Vec2d defaultSize = new Vec2d(5, 20);
    private MoveableUnit moveableUnit;

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    public Projectile(GameWorld gameWorld, MoveableUnit mU, Vec2d size, Vec2d position, Direction direction) {
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.direction = direction;
        this.spriteFileName = "projectile.png";
        this.moveableUnit = mU;
        this.addComponents();
    }

    public Projectile(GameWorld gameWorld, MoveableUnit mU, Vec2d position, Direction direction, String file) {
        super(gameWorld, defaultSize, position);
        this.worldDraw = false;
        this.direction = direction;
        this.spriteFileName = file;
        this.moveableUnit = mU;
        this.addComponents();
    }

    private void addComponents(){
        this.add(new CollideComponent(new AAB(this.getSize(), this.getPosition())));
        this.add(new DrawComponent());
        this.add(new SpriteComponent(new Resource().get(spriteFileName)));
        this.add(new TickComponent());
    }

    public void onCollide(GameObject obj){
        if(obj.equals(moveableUnit)){ return; }
        moveableUnit.removeProjectile(this);
    }

    public void delete(){
        moveableUnit.removeProjectile(this);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
    }

    public void onTick(long nanosSinceLastTick){
        Vec2d oldPos = this.getPosition();
        if(direction == Direction.RIGHT){
            this.setPosition(new Vec2d(oldPos.x+speed, oldPos.y));
        } else if(direction == Direction.LEFT){
            this.setPosition(new Vec2d(oldPos.x-speed, oldPos.y));
        } else if(direction == Direction.UP){
            this.setPosition(new Vec2d(oldPos.x, oldPos.y-speed));
        } else {
            this.setPosition(new Vec2d(oldPos.x, oldPos.y+speed));
        }
    }

}
