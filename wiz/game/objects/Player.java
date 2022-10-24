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
import javafx.scene.input.KeyEvent;
import wiz.resources.Resource;

public class Player extends GameObject {

    private PlayerState state;
    private double speed;
    private Vec2d destination;

    public enum PlayerState {
        FACING_LEFT,
        FACING_RIGHT,
        FACING_UP,
        FACING_DOWN,
        WALKING_LEFT,
        WALKING_RIGHT,
        WALKING_UP,
        WALKING_DOWN,
    }

    public Player(GameWorld gameWorld, Vec2d size, Vec2d position, String spriteFile){
        super(gameWorld, size, position);
        this.state = PlayerState.FACING_DOWN;
        this.speed = 10.0;
        this.add(new Collidable(new AAB(size, position)));
        this.add(new Drawable());
        this.add(new HasSprite(new Resource().get(spriteFile)));
        this.add(new Tickable());
    }

    public void setSpeed(double s){
        this.speed = s;
    }

    public PlayerState getState(){
        return this.state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public boolean isMoving(){
        return this.state == PlayerState.WALKING_LEFT || this.state == PlayerState.WALKING_RIGHT || this.state == PlayerState.WALKING_UP || this.state == PlayerState.WALKING_DOWN;
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
        System.out.println("movng from "+curPos+" to "+destination+", state is "+this.state);
    }

    public void onTick(long nanosSinceLastTick){
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
    }

}
