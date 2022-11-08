package nin.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.systems.GameSystem;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import static nin.game.objects.Player.PlayerState.*;

public class Player extends GameObject {

    private PlayerState state;
    private static final double speed = 5;
    private static final double mass = 5;
    private boolean gravity;

    public enum PlayerState {
        FACING_LEFT(2),
        FACING_RIGHT(1),
        WALKING_LEFT(2),
        WALKING_RIGHT(1),
        DYING(4);

        public int index;
        PlayerState(int index){
            this.index = index;
        }
    }

    public Player(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position);
        this.addComponents();
        this.state = FACING_RIGHT;
        this.gravity = false;
        this.setMass(mass);
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()));
        this.add(collide);
        KeyComponent key = new KeyComponent();
        this.add(key);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
        PhysicsComponent physics = new PhysicsComponent(this);
        this.add(physics);
        TickComponent tick = new TickComponent();
        this.add(tick);
    }

    public PlayerState getState(){
        return this.state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public double getMass() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getMass();
            }
        }
        return 0;
    }

    public void setMass(double mass) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setMass(mass);
            }
        }
    }

    public Vec2d getForce() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getForce();
            }
        }
        return new Vec2d(0);
    }

    public void setForce(Vec2d force) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setForce(force);
            }
        }
    }

    public Vec2d getImpulse() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getImpulse();
            }
        }
        return new Vec2d(0);
    }

    public void setImpulse(Vec2d impulse) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setImpulse(impulse);
            }
        }
    }

    public Vec2d getVelocity() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getVelocity();
            }
        }
        return new Vec2d(0);
    }

    public void setVelocity(Vec2d velocity) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setVelocity(velocity);
            }
        }
    }

    public Vec2d getAcceleration() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getAcceleration();
            }
        }
        return new Vec2d(0);
    }

    public void setAcceleration(Vec2d acceleration) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setAcceleration(acceleration);
            }
        }
    }

    public boolean getGravity(){
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).getGravity();
            }
        }
        return false;
    }

    public void setGravity(boolean grav){
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setGravity(grav);
            }
        }
    }

    public double getRestitution() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getRestitution();
            }
        }
        return 0;
    }

    public void setRestitution(double restitution) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setRestitution(restitution);
            }
        }
    }

    public boolean isMoving(){
        return this.state == WALKING_LEFT || this.state == WALKING_RIGHT;
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.D){
            this.state = WALKING_RIGHT;
        } else if(e.getCode() == KeyCode.A){
            this.state = WALKING_LEFT;
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
        super.onTick(nanosSinceLastTick);
        Vec2d curPos = this.getPosition();
        if(this.state == WALKING_LEFT){
            Vec2d newPos = new Vec2d(curPos.x-speed, curPos.y);
            this.setPosition(newPos);
        } else if(this.state == WALKING_RIGHT){
            Vec2d newPos = new Vec2d(curPos.x+speed, curPos.y);
            this.setPosition(newPos);
        }
    }

}
