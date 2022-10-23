package engine.display;

import engine.display.uiElements.Background;
import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Viewport extends UIElement {

    private GameWorld gameWorld;
    private Vec2d displaySize; // size (in GameWorld coordinates) of what's being displayed
    private Vec2d displayPosition; // position (in GameWorld coordinates) of what's being displayed
    private final Affine affine = new Affine();
    private Direction panning = Direction.NONE;
    private static final int panningSpeed = 5;
    private static final int zoomSpeed = 1; // percent that you zoom
    private final ArrayList<Background> clippingBackgrounds = new ArrayList<>(4);
    private static final Vec2d minDisplaySize = new Vec2d(120,67.5);

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
        for(int i = 0; i < 4; i++){
            Background background = new Background(Color.rgb(0,0,0), new Vec2d(0), new Vec2d(0));
            this.clippingBackgrounds.add(background);
            this.addChild(background);
        }
    }

    public Viewport(Vec2d displaySize, Vec2d displayPosition, Vec2d size, Vec2d position){
        super("Viewport");
        this.displaySize = displaySize;
        this.displayPosition = displayPosition;
        this.size = size;
        this.position = position;
        for(int i = 0; i < 4; i++){
            Background background = new Background(Color.rgb(0,0,0), new Vec2d(0), new Vec2d(0));
            this.clippingBackgrounds.add(background);
            this.addChild(background);
        }
    }

    public void setDisplay(Vec2d displaySize, Vec2d displayPosition){
        this.displaySize = displaySize;
        this.displayPosition = displayPosition;
    }

    public void setDisplayPosition(Vec2d newDisplayPos){
        this.displayPosition = newDisplayPos;
    }

    public Vec2d getDisplaySize(){
        return this.displaySize;
    }

    public Vec2d getDisplayPosition(){
        return this.displayPosition;
    }

    public void setWorld(GameWorld world){
        this.gameWorld = world;
    }

    public void reset(){
        this.gameWorld.reset();
    }

    public void onTick(long nanosSinceLastTick){
        if(panning == Direction.UP && this.displayPosition.y > 0){
            this.displayPosition = this.displayPosition.plus(new Vec2d(0,-1 * panningSpeed));
        } else if(panning == Direction.DOWN && (this.displayPosition.y+this.displaySize.y) < this.gameWorld.getSize().y){
            this.displayPosition = this.displayPosition.plus(new Vec2d(0,panningSpeed));
        } else if(panning == Direction.RIGHT && (this.displayPosition.x+this.displaySize.x) < this.gameWorld.getSize().x){
            this.displayPosition = this.displayPosition.plus(new Vec2d(panningSpeed,0));
        } else if(panning == Direction.LEFT && this.displayPosition.x > 0){
            this.displayPosition = this.displayPosition.plus(new Vec2d(-1 * panningSpeed, 0));
        }
        if(gameWorld != null){ gameWorld.onTick(nanosSinceLastTick); }
    }

    public void onDraw(GraphicsContext g){
        Transform ogTransform = g.getTransform();
        this.affine.setToIdentity();
        this.affine.append(g.getTransform());
        // note about affines: they scale then translate (even if you add the translation first), so
        // this translation is scaled to account for that
        this.affine.appendTranslation(-1 * this.displayPosition.x * (this.size.x / this.displaySize.x), -1 * this.displayPosition.y * (this.size.y / this.displaySize.y));
        double xScale = this.size.x / this.displaySize.x;
        double yScale = this.size.y / this.displaySize.y;
        affine.appendScale(xScale, yScale);
        affine.appendTranslation(this.position.x, this.position.y);
        g.setTransform(this.affine);
        gameWorld.onDraw(g);
        this.affine.setToIdentity();
        this.affine.append(ogTransform);
        g.setTransform(this.affine);
        super.onDraw(g);
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        super.onResize(newWindowSize, newScreenSize);
        double newLeftSpacing = (newWindowSize.x - newScreenSize.x) / 2;
        double newTopSpacing = (newWindowSize.y - newScreenSize.y) / 2;
        List<Vec2d> positions = Arrays.asList(new Vec2d(0), new Vec2d(newWindowSize.x-newLeftSpacing, 0), new Vec2d(0), new Vec2d(0, newWindowSize.y-newTopSpacing));
        List<Vec2d> sizes = Arrays.asList(new Vec2d(newLeftSpacing, newWindowSize.y), new Vec2d(newLeftSpacing, newWindowSize.y), new Vec2d(newWindowSize.x, newTopSpacing), new Vec2d(newWindowSize.x, newTopSpacing));

        for(int i = 0; i < 4; i++){
            Background background = clippingBackgrounds.get(i);
            background.setPosition(positions.get(i));
            background.setSize(sizes.get(i));
        }
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W){
            this.panning = Direction.UP;
        } else if(e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S){
            this.panning = Direction.DOWN;
        } else if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A){
            this.panning = Direction.LEFT;
        } else if(e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D){
            this.panning = Direction.RIGHT;
        }
    }

    public void onKeyReleased(KeyEvent e){
        if((e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) && panning == Direction.UP){
            panning = Direction.NONE;
        } else if((e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) && panning == Direction.DOWN){
            panning = Direction.NONE;
        } else if((e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) && panning == Direction.LEFT){
            panning = Direction.NONE;
        } else if((e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) && panning == Direction.RIGHT){
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

    public void onMouseWheelMoved(ScrollEvent e){
        double zoomPercent = 1 - (e.getDeltaY() * zoomSpeed / 100);
        double x = (e.getX() - this.position.x) * (this.displaySize.x / this.size.x) + this.displayPosition.x;
        double y = (e.getY() - this.position.y) * (this.displaySize.y / this.size.y) + this.displayPosition.y;
        double topDist = (y - this.displayPosition.y) * zoomPercent;
        double botDist = ((this.displayPosition.y + this.displaySize.y) - y) * zoomPercent;
        double leftDist = (x - this.displayPosition.x) * zoomPercent;
        double rightDist = ((this.displayPosition.x + this.displaySize.x) - x) * zoomPercent;
        double newDisplaySizeX = Math.min(leftDist + rightDist, this.gameWorld.getSize().x);
        newDisplaySizeX = Math.max(newDisplaySizeX, this.minDisplaySize.x);
        double newDisplaySizeY = Math.min(topDist + botDist, this.gameWorld.getSize().y);
        newDisplaySizeY = Math.max(newDisplaySizeY, this.minDisplaySize.y);
        double newDisplayPosX = x - ((e.getX() - this.position.x) / this.size.x * newDisplaySizeX);
        double newDisplayPosY = y - ((e.getY() - this.position.y) / this.size.y * newDisplaySizeY);
        this.displayPosition = new Vec2d(newDisplayPosX, newDisplayPosY);
        this.displaySize = new Vec2d(newDisplaySizeX, newDisplaySizeY);
    }
}
