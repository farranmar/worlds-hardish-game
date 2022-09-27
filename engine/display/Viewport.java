package engine.display;

import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class Viewport extends UIElement {

    private GameWorld gameWorld;
    private Vec2d displaySize; // size (in GameWorld coordinates) of what's being displayed
    private Vec2d displayPosition; // position (in GameWorld coordinates) of what's being displayed
    private Affine affine = new Affine();
    private Direction panning = Direction.NONE;
    private int panningSpeed = 5;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    public Viewport(){
        super("Viewport");
        this.displaySize = new Vec2d(0);
        this.displayPosition = new Vec2d(0);
    }

    public Viewport(Vec2d displaySize, Vec2d displayPosition, Vec2d size, Vec2d position){
        super("Viewport");
        this.displaySize = displaySize;
        this.displayPosition = displayPosition;
        this.size = size;
        this.position = position;
    }

    public void setDisplay(Vec2d displaySize, Vec2d displayPosition){
        this.displaySize = displaySize;
        this.displayPosition = displayPosition;
    }

    public void setWorld(GameWorld world){
        this.gameWorld = world;
    }

    public void reset(){
        this.gameWorld.reset();
    }

    public void onTick(long nanosSinceLastTick){
        if(panning == Direction.UP && this.displayPosition.y > 0){
            this.displayPosition.plus(new Vec2d(0,-1 * panningSpeed));
        } else if(panning == Direction.DOWN && this.displayPosition.y < this.gameWorld.getSize().y){
            this.displayPosition.plus(new Vec2d(0,panningSpeed));
        } else if(panning == Direction.RIGHT && this.displayPosition.x < this.gameWorld.getSize().x){
            this.displayPosition.plus(new Vec2d(panningSpeed,0));
        } else if(panning == Direction.LEFT && this.displayPosition.x > 0){
            this.displayPosition.plus(new Vec2d(-1 * panningSpeed, 0));
        }
    }

    public void onDraw(GraphicsContext g){
        Transform ogTransform = g.getTransform();
        this.affine.setToIdentity();
        this.affine.append(g.getTransform());
        this.affine.appendTranslation(-1 * this.displayPosition.x, -1 * this.displayPosition.y);
        double xScale = this.size.x / this.displaySize.x;
        double yScale = this.size.y / this.displaySize.y;
        affine.appendScale(xScale, yScale);
        affine.appendTranslation(this.position.x, this.position.y);
        g.setTransform(this.affine);
        gameWorld.onDraw(g);
        this.affine.setToIdentity();
        this.affine.append(ogTransform);
        g.setTransform(this.affine);
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.UP){
            this.panning = Direction.UP;
        } else if(e.getCode() == KeyCode.DOWN){
            this.panning = Direction.DOWN;
        } else if(e.getCode() == KeyCode.LEFT){
            this.panning = Direction.LEFT;
        } else if(e.getCode() == KeyCode.RIGHT){
            this.panning = Direction.RIGHT;
        }
    }

    public void onKeyReleased(KeyEvent e){
        if(e.getCode() == KeyCode.UP && panning == Direction.UP){
            panning = Direction.NONE;
        } else if(e.getCode() == KeyCode.DOWN && panning == Direction.DOWN){
            panning = Direction.NONE;
        } else if(e.getCode() == KeyCode.LEFT && panning == Direction.LEFT){
            panning = Direction.NONE;
        } else if(e.getCode() == KeyCode.RIGHT && panning == Direction.RIGHT){
            panning = Direction.NONE;
        }
    }

    public void onMousePressed(MouseEvent e){
        if(inRange(e)){
            double gameCoordX = (e.getX() - this.position.x) * (this.displaySize.x/this.size.x) + this.displayPosition.x;
            double gameCoordY = (e.getY() - this.position.y) * (this.displaySize.y/this.size.y) + this.displayPosition.y;
            gameWorld.onMousePressed(gameCoordX, gameCoordY);
        }
    }

    public void onMouseReleased(MouseEvent e){
        if(inRange(e)){
            double gameCoordX = (e.getX() - this.position.x) * (this.displaySize.x/this.size.x) + this.displayPosition.x;
            double gameCoordY = (e.getY() - this.position.y) * (this.displaySize.y/this.size.y) + this.displayPosition.y;
            gameWorld.onMouseReleased(gameCoordX, gameCoordY);
        }
    }

    public void onMouseDragged(MouseEvent e){
        if(inRange(e)){
            double gameCoordX = (e.getX() - this.position.x) * (this.displaySize.x/this.size.x) + this.displayPosition.x;
            double gameCoordY = (e.getY() - this.position.y) * (this.displaySize.y/this.size.y) + this.displayPosition.y;
            gameWorld.onMouseDragged(gameCoordX, gameCoordY);
        }
    }

}
