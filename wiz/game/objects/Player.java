package wiz.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import wiz.resources.Resource;

import java.util.ArrayList;

public class Player extends GameObject {

    private PlayerState state;
    private double speed;
    private Vec2d destination;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private static final int numFrames = 4;
    private static final Vec2d frameSize = new Vec2d(300);
    private static final int animationSpeed = 3;
    private Map map;
    private int ticksSinceDeath = -1;

    public enum PlayerState {
        FACING_LEFT(2),
        FACING_RIGHT(1),
        FACING_UP(3),
        FACING_DOWN(0),
        WALKING_LEFT(2),
        WALKING_RIGHT(1),
        WALKING_UP(3),
        WALKING_DOWN(0),
        DYING(4);

        public int index;
        PlayerState(int index){
            this.index = index;
        }
    }

    public Player(GameWorld gameWorld, Map map, Vec2d size, Vec2d position, String spriteFile){
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.map = map;
        this.state = PlayerState.FACING_DOWN;
        this.speed = 5.0;
        this.add(new Collidable(new AAB(size, position)));
        this.add(new Drawable());
        this.add(new HasSprite(new Resource().get(spriteFile)));
        this.add(new Tickable());
        this.setSubImage(new HasSprite.SubImage(frameSize, new Vec2d(0, this.state.index*frameSize.y)));
    }

    public void setSpeed(double s){
        this.speed = s;
    }

    public PlayerState getState(){
        return this.state;
    }

    public void setState(PlayerState state){
        this.state = state;
        if(state == PlayerState.WALKING_DOWN){
            this.animate(frameSize, this.state.index*frameSize.y, 0, numFrames, 2);
        }
    }

    public boolean isMoving(){
        return this.state == PlayerState.WALKING_LEFT || this.state == PlayerState.WALKING_RIGHT || this.state == PlayerState.WALKING_UP || this.state == PlayerState.WALKING_DOWN;
    }

    public void die(){
        if(this.state == PlayerState.DYING){ return; }
        this.ticksSinceDeath = 0;
        this.state = PlayerState.DYING;
        this.animate(frameSize, 4*frameSize.y, 0, numFrames, 2);
    }

    public void moveTo(Vec2d newPos){
        if(this.isMoving()){
            return;
        }
        this.destination = newPos;
        Vec2d curPos = this.getPosition();
        if(newPos.x < curPos.x){
            this.state = PlayerState.WALKING_LEFT;
        } else if(newPos.x > curPos.x){
            this.state = PlayerState.WALKING_RIGHT;
        } else if(newPos.y < curPos.y){
            this.state = PlayerState.WALKING_UP;
        } else if(newPos.y > curPos.y){
            this.state = PlayerState.WALKING_DOWN;
        }
    }

    public void fireProjectile(){
        Projectile.Direction direction;
        Vec2d curPos = this.getPosition();
        Vec2d curSize = this.getSize();
        Vec2d projSize = new Vec2d(curSize.x/2, curSize.y/2);
        Vec2d projPos;
        if(this.state == PlayerState.FACING_LEFT || this.state == PlayerState.WALKING_LEFT){
            direction = Projectile.Direction.LEFT;
            projPos = new Vec2d(curPos.x - projSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (this.state == PlayerState.FACING_RIGHT || this.state == PlayerState.WALKING_RIGHT){
            direction = Projectile.Direction.RIGHT;
            projPos = new Vec2d(curPos.x+curSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (this.state == PlayerState.FACING_DOWN || this.state == PlayerState.WALKING_DOWN){
            direction = Projectile.Direction.DOWN;
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y+curSize.y);
        } else {
            direction = Projectile.Direction.UP;
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

    public void onCollide(GameObject obj){
        if(obj instanceof Enemy){
            this.die();
        }
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
        if(this.state == PlayerState.DYING){
            if(this.ticksSinceDeath >= 8){ this.map.setResult(GameWorld.Result.DEFEAT); }
            this.ticksSinceDeath++;
        }
        Vec2d curPos = this.getPosition();
        if(this.state == PlayerState.WALKING_LEFT){
            Vec2d newPos = new Vec2d(curPos.x-speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x <= destination.x){
                this.state = PlayerState.FACING_LEFT;
            }
        } else if(this.state == PlayerState.WALKING_RIGHT){
            Vec2d newPos = new Vec2d(curPos.x+speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x >= destination.x){
                this.state = PlayerState.FACING_RIGHT;
            }
        } else if(this.state == PlayerState.WALKING_UP){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y-speed);
            this.setPosition(newPos);
            if(newPos.y <= destination.y){
                this.state = PlayerState.FACING_UP;
            }
        } else if(this.state == PlayerState.WALKING_DOWN){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y+speed);
            this.setPosition(newPos);
            if(newPos.y >= destination.y){
                this.state = PlayerState.FACING_DOWN;
            }
        }
        if(this.isMoving()){
            this.animate(frameSize, this.state.index*frameSize.y, 0, numFrames, animationSpeed);
        } else if(this.state != PlayerState.DYING) {
            this.setSubImage(new HasSprite.SubImage(frameSize, new Vec2d(0, this.state.index*frameSize.y)));
        }
        super.onTick(nanosSinceLastTick);
    }

}
