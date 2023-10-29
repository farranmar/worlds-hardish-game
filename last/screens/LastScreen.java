package last.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.PauseButton;
import engine.display.uiElements.UIElement;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import last.game.EditorWorld;
import last.game.LastWorld;

public class LastScreen extends Screen {

    private LastWorld world;
    private Color primaryColor;
    private int nextLevel = 0;
    private boolean clearedGame = false;

    public LastScreen(Color color){
        super(ScreenName.GAME);
        this.primaryColor = color;
        Viewport viewport = new Viewport(new Vec2d(1920,1080), new Vec2d(0), new Vec2d(960,540), new Vec2d(0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(this.primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
        PauseButton pauseButton = new PauseButton(this.primaryColor, new Vec2d(910, 30), new Vec2d(25, 35));
        this.add(pauseButton);

        LastWorld lastWorld = new LastWorld();
        lastWorld.setViewport(this.viewport);
        this.world = lastWorld;
        viewport.setWorld(lastWorld);
    }

    @Override
    public void reset() {
        super.reset();
        LastWorld lastWorld = new LastWorld();
        lastWorld.setViewport(this.viewport);
        this.world = lastWorld;
        viewport.setWorld(lastWorld);
        this.nextLevel = 0;
        this.clearedGame = false;
    }

    public void loadFromLevel(String fileName){
        EditorWorld editorWorld = new EditorWorld(fileName, EditorScreen.getClassMap());
        LastWorld loadedWorld = new LastWorld(editorWorld);
        loadedWorld.setViewport(this.viewport);
        if(fileName.contains("level")){ this.nextLevel = 10; }
        loadedWorld.setLevelNum(this.nextLevel);
        this.world = loadedWorld;
        this.viewport.setWorld(loadedWorld);
    }

    public void loadFromGame(String fileName){
        LastWorld loadedWorld = new LastWorld(fileName, EditorScreen.getClassMap());
        loadedWorld.setViewport(this.viewport);
        this.nextLevel = loadedWorld.getLevelNum();
        this.world = loadedWorld;
        this.viewport.setWorld(loadedWorld);
    }

    public void saveTo(String fileName){
        this.viewport.getWorld().saveTo(fileName);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        if(this.viewport.getResult() != GameWorld.Result.PLAYING && this.active && this.nextScreen != ScreenName.MENU){
            System.out.println("setting next screen to game over; this.nextLevel is " + this.nextLevel);
            if(this.nextLevel >= 4){ this.clearedGame = true; }
            this.nextScreen = ScreenName.GAME_OVER;
        }
    }

    public boolean getClearedGame(){
        return this.clearedGame;
    }

    public void nextLevel() {
        this.nextLevel += 1;
        if (this.nextLevel > 4) {
            this.nextLevel = 0;
            this.clearedGame = true;
            return;
        }
        this.loadFromLevel("last/save-files/load" + this.nextLevel + ".xml");
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(!ele.inRange(e)){ continue; }
            String name = ele.getName();
            if(name.contains("Back Button")){
                this.nextScreen = ScreenName.MENU;
            } else if(name.contains("Pause Button")){
                this.nextScreen = ScreenName.PAUSE;
            }
        }
    }

}
