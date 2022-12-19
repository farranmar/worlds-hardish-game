package last.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.util.HashMap;
import java.util.Map;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Player extends GameObject {

    protected Color color;
    private Map<KeyCode, Boolean> directionMap = initializeDirectionMap();
    private static final double speed = 4;
    private static int numPlayers = 0;
    private double boosted = 1;
    private Vec2d respawnPoint;
    private boolean active = true;

    public Player(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position);
        this.addComponents();
        this.color = Color.rgb(156, 122, 151);
        this.respawnPoint = position;
        this.drawPriority = 1000000+numPlayers;
        numPlayers++;
    }

    public Player(GameWorld world, Vec2d size, Vec2d position, Color color){
        super(world, size, position);
        this.addComponents();
        this.color = color;
        this.respawnPoint = position;
        this.drawPriority = 10000+numPlayers;
        numPlayers++;
    }

    private Map<KeyCode, Boolean> initializeDirectionMap(){
        Map<KeyCode, Boolean> theMap = new HashMap<>();
        theMap.put(KeyCode.W, false);
        theMap.put(KeyCode.A, false);
        theMap.put(KeyCode.S, false);
        theMap.put(KeyCode.D, false);
        return theMap;
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()));
        this.add(collide);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
        TickComponent tick = new TickComponent();
        this.add(tick);
        KeyComponent key = new KeyComponent();
        this.add(key);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void die(){
        this.setPosition(this.respawnPoint);
        this.directionMap = initializeDirectionMap();
    }

    public void setCheckpoint(Vec2d newCheckpoint) {
        this.respawnPoint = newCheckpoint;
    }

    public void setActive(boolean a){
        this.active = a;
        if(!active){
            this.directionMap = initializeDirectionMap();
        }
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(obj instanceof DeathBall){
            this.die();
        } else if (obj instanceof Checkpoint) {
            this.setCheckpoint(((Checkpoint)obj).getSaveSpot());
            return;
        } else if(obj instanceof EndPoint || obj instanceof PathPoint){
            return;
        }
        super.onCollide(obj, mtv);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
    }

    public void onKeyPressed(KeyEvent e){
        if(!active){ return; }
        if(e.getCode() != KeyCode.A && e.getCode() != KeyCode.W && e.getCode() != KeyCode.S && e.getCode() != KeyCode.D){ return; }
        directionMap.put(e.getCode(), true);
    }

    public void onKeyReleased(KeyEvent e){
        if(e.getCode() != KeyCode.A && e.getCode() != KeyCode.W && e.getCode() != KeyCode.S && e.getCode() != KeyCode.D){ return; }
        directionMap.put(e.getCode(), false);
    }

    public void onTick(long nanosSinceLastTick){
        Vec2d curPos = this.getPosition();
        Vec2d newPos = curPos;
        if(directionMap.get(KeyCode.W)){
            newPos = newPos.plus(new Vec2d(0, -1*speed*boosted));
        }
        if(directionMap.get(KeyCode.S)){
            newPos = newPos.plus(new Vec2d(0, speed*boosted));
        }
        if(directionMap.get(KeyCode.A)){
            newPos = newPos.plus(new Vec2d(-1*speed*boosted, 0));
        }
        if(directionMap.get(KeyCode.D)){
            newPos = newPos.plus(new Vec2d(speed*boosted, 0));
        }
        this.setPosition(newPos);
        super.onTick(nanosSinceLastTick);
    }

    public Player clone(){
        Player clone = new Player(this.gameWorld, this.getSize(), this.getPosition(), this.color);
        clone.directionMap = initializeDirectionMap();
        clone.boosted = this.boosted;
        clone.respawnPoint = this.respawnPoint;
        clone.active = this.active;
        clone.setCollidable(this.isCollidable());
        return clone;
    }

    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Player");
        ele.setAttribute("active", this.active+"");
        ele.setAttribute("boosted", this.boosted+"");
        ele.appendChild(colorToXml(doc, color));
        ele.appendChild(this.respawnPoint.toXml(doc, "RespawnPoint"));
        return ele;
    }

    public Player (Element ele, GameWorld world){
        this.gameWorld = world;
        this.setConstantsXml(ele);
        Color color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        this.setColor(color);
        this.respawnPoint = Vec2d.fromXml(getTopElementsByTagName(ele, "RespawnPoint").get(0));
        this.active = Boolean.parseBoolean(ele.getAttribute("active"));
        this.boosted = Double.parseDouble(ele.getAttribute("boosted"));

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);
        this.drawPriority = 10000+numPlayers;
        numPlayers++;
    }

}
