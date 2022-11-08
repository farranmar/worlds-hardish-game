package wiz.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class Player extends MoveableUnit {

    private PlayerState state;

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

    public Player(GameWorld gameWorld, Map map, Vec2d size, Vec2i mapPosition, String spriteFile){
        super(gameWorld, map, size, mapPosition, spriteFile);
        this.worldDraw = false;
        this.state = Player.PlayerState.FACING_DOWN;
        this.setSubImage(new SpriteComponent.SubImage(frameSize, new Vec2d(0, this.state.index*frameSize.y)));
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

    public void moveTo(Vec2i newMapPos){
        this.mapPosition = newMapPos;
        Vec2d newPos = map.getWorldPos(newMapPos);
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
        if(this.state == PlayerState.FACING_LEFT || this.state == PlayerState.WALKING_LEFT){
            super.fireProjectile(Projectile.Direction.LEFT);
        } else if (this.state == PlayerState.FACING_RIGHT || this.state == PlayerState.WALKING_RIGHT){
            super.fireProjectile(Projectile.Direction.RIGHT);
        } else if (this.state == PlayerState.FACING_DOWN || this.state == PlayerState.WALKING_DOWN){
            super.fireProjectile(Projectile.Direction.DOWN);
        } else {
            super.fireProjectile(Projectile.Direction.UP);
        }
    }

    public void onCollide(GameObject obj){
        if(obj instanceof Enemy){
            this.die();
        }
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
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
            this.setSubImage(new SpriteComponent.SubImage(frameSize, new Vec2d(0, this.state.index*frameSize.y)));
        }
    }

}
