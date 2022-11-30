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
    private static final int panningSpeed = 5;
    private static final int zoomSpeed = 1; // percent that you zoom
    private static final ArrayList<Background> clippingBackgrounds = new ArrayList<>(4);
    private static final Vec2d minDisplaySize = new Vec2d(120,67.5);

    public Viewport(){
        super("Viewport");
        this.displaySize = new Vec2d(0);
        this.displayPosition = new Vec2d(0);
        for(int i = 0; i < 4; i++){
            Background background = new Background(Color.rgb(0,0,0), new Vec2d(0), new Vec2d(0));
            clippingBackgrounds.add(background);
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

    public GameWorld getWorld(){
        return this.gameWorld;
    }

    public void reset(){
        if(gameWorld != null){ this.gameWorld.reset(); }
    }

    public GameWorld.Result getResult(){
        if(gameWorld != null){ return this.gameWorld.getResult(); }
        else { return GameWorld.Result.PLAYING; }
    }

    public void onTick(long nanosSinceLastTick){
        if(gameWorld != null){ gameWorld.onTick(nanosSinceLastTick); }
    }

    public void onLateTick(){
        if(gameWorld != null){ gameWorld.onLateTick(); }
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
        if(gameWorld != null) { gameWorld.onResize(newWindowSize, newScreenSize); }
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
        gameWorld.onKeyPressed(e);
    }

    public void onKeyReleased(KeyEvent e){
        gameWorld.onKeyReleased(e);
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
