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
    double speed;

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
        this.speed = 1.0;
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

    public void moveTo(Vec2d newPos){
        this.setPosition(newPos);
    }

}
