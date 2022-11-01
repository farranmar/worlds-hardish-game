package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.HasSprite;
import engine.game.components.Tickable;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import engine.support.graph.Node;
import wiz.game.ai.BTHandler;
import wiz.game.helpers.TileType;
import wiz.resources.Resource;

import java.util.Stack;

public class Enemy extends MoveableUnit {

    private String spriteFileName;
    private EnemyState state;
    private Stack<Node<Tile>> path = new Stack<>();
    private BTHandler btHandler;
    private int ticksSinceUpdate = -1;
    private long nanosSinceUpdate = -1;
    private static int delay = 12;

    public static void setDelay(int newDelay){
        delay = newDelay;
    }

    public enum EnemyState{
        MOVING_LEFT,
        MOVING_RIGHT,
        MOVING_UP,
        MOVING_DOWN,
        IDLE;
    }

    public Enemy(GameWorld gameWorld, Map map, Vec2d size, Vec2i mapPosition, String fileName){
        super(gameWorld, map, size, mapPosition, fileName);
        this.spriteFileName = fileName;
        this.map = map;
        this.addComponents();
        this.worldDraw = false;
        this.state = EnemyState.IDLE;
        this.btHandler = new BTHandler(this.map, this);
    }

    private void addComponents(){
        this.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        this.add(new HasSprite(new Resource().get(this.spriteFileName)));
        this.add(new Tickable());
    }

    public void setPath(Stack<Node<Tile>> newPath){
        this.path = newPath;
    }

    public void onCollide(GameObject obj){
        if(obj instanceof Projectile){
            this.map.removeEnemy(this);
        } else if(obj instanceof Player){
            ((Player)obj).die();
        }
    }

    public void moveTo(Vec2i newMapPos){
        if(this.map.getTile(newMapPos).getType() == TileType.IMPASSABLE){
            return;
        }
        Vec2d newPos = map.getWorldPos(newMapPos);
        if(this.state != EnemyState.IDLE){
            return;
        }
        this.mapPosition = newMapPos;
        this.destination = newPos;
        Vec2d curPos = this.getPosition();
        if(newPos.x < curPos.x){
            this.state = EnemyState.MOVING_LEFT;
        } else if(newPos.x > curPos.x){
            this.state = EnemyState.MOVING_RIGHT;
        } else if(newPos.y < curPos.y){
            this.state = EnemyState.MOVING_UP;
        } else if(newPos.y > curPos.y){
            this.state = EnemyState.MOVING_DOWN;
        }
    }

    private boolean isOnScreen(){
        Vec2d vpPosition = this.gameWorld.getViewport().getDisplayPosition();
        Vec2d vpSize = this.gameWorld.getViewport().getDisplaySize();
        Vec2d thisPosition = this.getPosition();
        Vec2d thisSize = this.getSize();
        boolean vertOverlap = thisPosition.y+thisSize.y > vpPosition.y && thisPosition.y < vpPosition.y+vpSize.y;
        boolean horOverlap = this.getPosition().x+thisSize.x > vpPosition.x && thisPosition.x < vpPosition.x+vpSize.x;
        return vertOverlap && horOverlap;
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        this.ticksSinceUpdate++;
        this.nanosSinceUpdate += nanosSinceLastTick;
        if(!this.isOnScreen()){ return; }
        Vec2d curPos = this.getPosition();
        if(this.state == EnemyState.MOVING_LEFT){
            Vec2d newPos = new Vec2d(curPos.x-speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x <= destination.x){
                this.state = EnemyState.IDLE;
            }
        } else if(this.state == EnemyState.MOVING_RIGHT){
            Vec2d newPos = new Vec2d(curPos.x+speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x >= destination.x){
                this.state = EnemyState.IDLE;
            }
        } else if(this.state == EnemyState.MOVING_UP){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y-speed);
            this.setPosition(newPos);
            if(newPos.y <= destination.y){
                this.state = EnemyState.IDLE;

            }
        } else if(this.state == EnemyState.MOVING_DOWN){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y+speed);
            this.setPosition(newPos);
            if(newPos.y >= destination.y){
                this.state = EnemyState.IDLE;
            }
        }
        if(this.state == EnemyState.IDLE && ticksSinceUpdate >= delay){
            this.btHandler.update(nanosSinceLastTick);
            ticksSinceUpdate = 0;
            nanosSinceUpdate = 0;
        }
        if(!this.path.empty()){
            Vec2i nextPos = this.path.pop().getValue().getMapPosition();
            this.moveTo(nextPos);
        }
    }

}
