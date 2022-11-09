package nin.game.objects;

import engine.game.components.*;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import static nin.game.objects.Player.PlayerState.*;

public class Player extends Block {

    private PlayerState state;
    private static final double speed = 3;
    private boolean toJump = false;

    public enum PlayerState {
        FACING_LEFT(2),
        FACING_RIGHT(1),
        WALKING_LEFT(2),
        WALKING_RIGHT(1);

        public int index;
        PlayerState(int index){
            this.index = index;
        }
    }

    public Player(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position, 0);
        KeyComponent key = new KeyComponent();
        this.add(key);
        this.state = FACING_RIGHT;
    }

    public PlayerState getState(){
        return this.state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public boolean isMoving(){
        return this.state == WALKING_LEFT || this.state == WALKING_RIGHT;
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.D){
            this.state = WALKING_RIGHT;
        } else if(e.getCode() == KeyCode.A){
            this.state = WALKING_LEFT;
        } else if(e.getCode() == KeyCode.SHIFT){
            this.toJump = true;
        }
    }

    public void onKeyReleased(KeyEvent e){
        if(e.getCode() == KeyCode.D && this.state == WALKING_RIGHT){
            this.state = FACING_RIGHT;
        } else if(e.getCode() == KeyCode.A && this.state == WALKING_LEFT){
            this.state = FACING_LEFT;
        }
    }

    public void onDraw(GraphicsContext g){
        g.setFill(Color.rgb(184, 132, 150));
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
    }

    public void onTick(long nanosSinceLastTick){
        Vec2d curPos = this.getPosition();
        if(this.state == WALKING_LEFT){
            Vec2d newPos = new Vec2d(curPos.x-speed, curPos.y);
            this.setPosition(newPos);
        } else if(this.state == WALKING_RIGHT){
            Vec2d newPos = new Vec2d(curPos.x+speed, curPos.y);
            this.setPosition(newPos);
        }
        if(!gravity && toJump){
            this.applyImpulse(new Vec2d(0, -50));
            this.toJump = false;
        }
        super.onTick(nanosSinceLastTick);
    }

}
