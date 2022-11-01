package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.Drawable;
import engine.game.components.HasSprite;
import engine.game.components.Tickable;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import wiz.resources.Resource;

import java.util.ArrayList;

public class MoveableUnit extends GameObject {

    protected double speed;
    protected Vec2d destination;
    protected ArrayList<Projectile> projectiles = new ArrayList<>();
    protected static final int numFrames = 4;
    protected static final Vec2d frameSize = new Vec2d(300);
    protected static final int animationSpeed = 3;
    protected Map map;
    protected int ticksSinceDeath = -1;
    protected Vec2i mapPosition;

    public MoveableUnit(GameWorld gameWorld, Map map, Vec2d size, Vec2i mapPosition, String spriteFile){
        super(gameWorld, size, map.getWorldPos(mapPosition));
        this.map = map;
        this.speed = 5.0;
        this.mapPosition = mapPosition;
        this.add(new Collidable(new AAB(size, map.getWorldPos(mapPosition))));
        this.add(new Drawable());
        this.add(new HasSprite(new Resource().get(spriteFile)));
        this.add(new Tickable());
    }

    public void setSpeed(double s){
        this.speed = s;
    }

    public void die(){
        this.gameWorld.addToRemovalQueue(this);
    }

    public void moveTo(Vec2i newMapPosition){
        this.setPosition(map.getWorldPos(newMapPosition));
    }

    public Vec2i getMapPosition(){
        return this.mapPosition;
    }

    public void fireProjectile(Projectile.Direction direction){
        Vec2d curPos = this.getPosition();
        Vec2d curSize = this.getSize();
        Vec2d projSize = new Vec2d(curSize.x/2, curSize.y/2);
        Vec2d projPos;
        if(direction == Projectile.Direction.LEFT){
            projPos = new Vec2d(curPos.x - projSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (direction == Projectile.Direction.RIGHT){
            projPos = new Vec2d(curPos.x+curSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (direction == Projectile.Direction.DOWN){
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y+curSize.y);
        } else {
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y);
        }
        Projectile proj = new Projectile(this.gameWorld, this, projSize, projPos, direction);
        this.projectiles.add(proj);
        this.gameWorld.addToAdditionQueue(proj);
    }

    public void removeProjectile(Projectile proj){
        this.gameWorld.addToRemovalQueue(proj);
        this.projectiles.remove(proj);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        for(Projectile proj : projectiles){
            proj.onDraw(g);
        }
    }

    public void onTick(long nanosSinceLastTick){
        for(Projectile proj : projectiles){
            proj.onTick(nanosSinceLastTick);
        }
    }

}
