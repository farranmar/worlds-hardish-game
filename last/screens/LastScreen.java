package last.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.PauseButton;
import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import last.game.EditorWorld;
import last.game.LastWorld;

public class LastScreen extends Screen {

    private LastWorld world;
    private Color primaryColor;

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
    }

    public void loadFromLevel(String fileName){
        EditorWorld editorWorld = new EditorWorld(fileName, EditorScreen.getClassMap());
        LastWorld loadedWorld = new LastWorld(editorWorld);
        loadedWorld.setViewport(this.viewport);
        this.world = loadedWorld;
        this.viewport.setWorld(loadedWorld);
    }

    public void loadFromGame(String fileName){
        LastWorld loadedWorld = new LastWorld(fileName, EditorScreen.getClassMap());
        loadedWorld.setViewport(this.viewport);
        this.world = loadedWorld;
        this.viewport.setWorld(loadedWorld);
    }

    public void saveTo(String fileName){
        this.viewport.getWorld().saveTo(fileName);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        if(this.viewport.getResult() != GameWorld.Result.PLAYING){
            this.nextScreen = ScreenName.GAME_OVER;
        }
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
