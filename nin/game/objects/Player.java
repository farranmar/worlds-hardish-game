package nin.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nin.game.NinWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static engine.game.world.GameWorld.getTopElementsByTagName;
import static nin.game.objects.Player.PlayerState.*;

public class Player extends Block {

    private PlayerState state;
    private static final double speed = 3;
    private boolean toJump = false;

    public enum PlayerState {
        FACING_LEFT("FACING_LEFT"),
        FACING_RIGHT("FACING_RIGHT"),
        WALKING_LEFT("WALKING_LEFT"),
        WALKING_RIGHT("WALKING_RIGHT");

        public String string;
        PlayerState(String s){
            this.string = s;
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

    public void fireProjectile(){
        if(this.state == FACING_LEFT || this.state == WALKING_LEFT){
            super.fireProjectile(Projectile.Direction.LEFT);
        } else if (this.state == FACING_RIGHT || this.state == WALKING_RIGHT){
            super.fireProjectile(Projectile.Direction.RIGHT);
        }
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.D){
            this.state = WALKING_RIGHT;
        } else if(e.getCode() == KeyCode.A){
            this.state = WALKING_LEFT;
        } else if(e.getCode() == KeyCode.SHIFT){
            this.toJump = true;
        } else if(e.getCode() == KeyCode.SPACE){
            this.fireProjectile();
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
        super.onDraw(g);
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(this.projectiles.contains(obj)){ return; }
        super.onCollide(obj, mtv);
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

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Player");
        ele.setAttribute("playerState", this.state.string);
        ele.setAttribute("toJump", this.toJump+"");
        return ele;
    }

    public Player(Element ele, GameWorld world){
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Player")){ return; }
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.toJump = Boolean.parseBoolean(ele.getAttribute("toJump"));
        this.state = PlayerState.valueOf(ele.getAttribute("playerState"));

        Element componentsEle = (Element)(getTopElementsByTagName(ele, "Components").item(0));
        this.addComponentsXml(componentsEle);

        Element projsEle = (Element)(getTopElementsByTagName(ele, "Projectiles").item(0));
        this.addGravRaysAndProjs(projsEle);

        Element childrenEle = (Element)(getTopElementsByTagName(ele, "Children").item(0));
        this.setChildrenXml(childrenEle, NinWorld.getClassMap());
    }
}
