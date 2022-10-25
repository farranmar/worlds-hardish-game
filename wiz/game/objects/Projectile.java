package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.Drawable;
import engine.game.components.HasSprite;
import engine.game.components.Tickable;
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
    private Player player;

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    public Projectile(GameWorld gameWorld, Player p, Vec2d size, Vec2d position, Direction direction) {
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.direction = direction;
        this.spriteFileName = "projectile.png";
        this.player = p;
        this.addComponents();
    }

    public Projectile(GameWorld gameWorld, Player p, Vec2d position, Direction direction, String file) {
        super(gameWorld, defaultSize, position);
        this.worldDraw = false;
        this.direction = direction;
        this.spriteFileName = file;
        this.player = p;
        this.addComponents();
    }

    private void addComponents(){
        this.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        this.add(new Drawable());
        this.add(new HasSprite(new Resource().get(spriteFileName)));
        this.add(new Tickable());
    }

    public void onCollide(GameObject obj){
        if(obj.equals(player)){ return; }
        player.removeProjectile(this);
    }

    public void delete(){
        player.removeProjectile(this);
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
