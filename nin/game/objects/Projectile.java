package nin.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.components.PhysicsComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.objects.shapes.Circle;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static nin.game.objects.Projectile.Direction.LEFT;
import static nin.game.objects.Projectile.Direction.RIGHT;

public class Projectile extends GameObject {

    private Direction direction;
    private double speed = 5;
    private Block block;
    private Color color;

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    public Projectile(GameWorld gameWorld, Block b, Vec2d size, Vec2d position, Direction direction, Color color) {
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.direction = direction;
        this.block = b;
        this.color = color;
        this.addComponents();
    }

    private void addComponents(){
        this.add(new CollideComponent(new Circle(this.getSize().x, this.getPosition().plus(this.getSize().sdiv(2)))));
        this.add(new DrawComponent());
        this.add(new TickComponent());
        this.add(new PhysicsComponent(this, 1));
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(obj == this.block || !(obj instanceof Block)) { return; }
        super.onCollide(obj, mtv);
        this.delete();
    }

    public void delete(){
        block.removeProjectile(this);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        g.setFill(this.color);
        Vec2d pos = this.getPosition();
        Vec2d size = this.getSize();
        g.fillOval(pos.x, pos.y, size.x, size.y);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
//        Vec2d oldPos = this.getPosition();
//        if(direction == RIGHT){
//            this.setPosition(new Vec2d(oldPos.x+speed, oldPos.y));
//        } else if(direction == LEFT){
//            this.setPosition(new Vec2d(oldPos.x-speed, oldPos.y));
//        } else if(direction == Direction.UP){
//            this.setPosition(new Vec2d(oldPos.x, oldPos.y-speed));
//        } else {
//            this.setPosition(new Vec2d(oldPos.x, oldPos.y+speed));
//        }
    }

}
