package last;

import engine.Application;
import engine.display.screens.BackgroundScreen;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.game.objects.EndPoint;
import last.screens.*;

import java.util.ArrayList;

public class App extends Application {

    private static Vec2d DEFAULT_WINDOW_SIZE = new Vec2d(960,540);
    private static final Color primaryColor = Color.rgb(117, 162, 129);
    private Screen curActiveScreen = null;
    private Screen prevActiveScreen = null;

    public App(String title) {
        super(title, DEFAULT_WINDOW_SIZE, true, false);
        this.createScreens();
    }

    public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
        super(title, windowSize, debugMode, fullscreen);
        this.createScreens();
    }

    public void createScreens() {
        BackgroundScreen backgroundScreen = new BackgroundScreen(Color.rgb(48, 54, 51), Color.BLACK);
        backgroundScreen.makeVisible();
        this.add(backgroundScreen);
        MenuScreen menuScreen = new MenuScreen(primaryColor);
        menuScreen.activate();
        menuScreen.makeVisible();
        this.curActiveScreen = menuScreen;
        this.add(menuScreen);
        EditorScreen editorScreen = new EditorScreen(primaryColor);
        this.add(editorScreen);
        LastScreen lastScreen = new LastScreen(primaryColor);
        this.add(lastScreen);
        PauseScreen pauseScreen = new PauseScreen(primaryColor);
        this.add(pauseScreen);
        SaveScreen levelSaveScreen = new SaveScreen(ScreenName.SAVE_LOAD_LEVEL, primaryColor, "last/save-files/level");
        this.add(levelSaveScreen);
        SaveScreen gameSaveScreen = new SaveScreen(ScreenName.SAVE_LOAD_GAME, primaryColor, "last/save-files/game");
        this.add(gameSaveScreen);
        VictoryScreen victoryScreen = new VictoryScreen(primaryColor);
        this.add(victoryScreen);
    }

    @Override
    protected void setActiveScreen(Screen activeScreen) {
        ArrayList<ScreenName> visible = new ArrayList<>();
        visible.add(ScreenName.BACKGROUND);
        // if paused in game, make GAME visible
        if(activeScreen.getName() == ScreenName.PAUSE && (this.curActiveScreen.getName() == ScreenName.GAME || this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_GAME)){
            visible.add(ScreenName.GAME);
        }
        // if saving game, make game visible
        if(this.curActiveScreen.getName() == ScreenName.PAUSE && activeScreen.getName() == ScreenName.SAVE_LOAD_GAME){
            visible.add(ScreenName.GAME);
        }
        // if paused in editor, make EDITOR visible
        if(activeScreen.getName() == ScreenName.PAUSE && (this.curActiveScreen.getName() == ScreenName.EDITOR || this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_LEVEL)){
            visible.add(ScreenName.EDITOR);
        }
        // if saving level, make editor visible
        if(this.curActiveScreen.getName() == ScreenName.PAUSE && activeScreen.getName() == ScreenName.SAVE_LOAD_LEVEL){
            visible.add(ScreenName.EDITOR);
        }
        if(activeScreen.getName() == ScreenName.GAME_OVER){
            visible.add(ScreenName.GAME);
        }

        boolean choosingGame = this.curActiveScreen.getName() == ScreenName.MENU && activeScreen.getName() == ScreenName.SAVE_LOAD_GAME;
        boolean choosingLevel = this.curActiveScreen.getName() == ScreenName.MENU && activeScreen.getName() == ScreenName.SAVE_LOAD_LEVEL;
        boolean loadingGame = (this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_GAME || this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_LEVEL)
                && activeScreen.getName() == ScreenName.GAME;
        boolean loadingLevel = this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_LEVEL && activeScreen.getName() == ScreenName.EDITOR;

        if(loadingLevel){
            for(Screen screen : screens){
                if(screen.getName() == activeScreen.getName()){
                    String fileName = ((SaveScreen)curActiveScreen).getSaveFile();
                    ((EditorScreen)screen).loadFrom(fileName);
                    screen.activate();
                    screen.makeVisible();
                } else if(visible.contains(screen.getName())){
                    screen.inactivate();
                    screen.makeVisible();
                } else {
                    screen.reset();
                    screen.inactivate();
                    screen.makeInvisible();
                }
            }
        } else if(loadingGame) {
            for(Screen screen : screens){
                if(screen.getName() == activeScreen.getName()){
                    String fileName = ((SaveScreen)curActiveScreen).getSaveFile();
                    if(curActiveScreen.getName() == ScreenName.SAVE_LOAD_GAME){ ((LastScreen)screen).loadFromGame(fileName); }
                    else { ((LastScreen)screen).loadFromLevel(fileName); }
                    screen.activate();
                    screen.makeVisible();
                } else if(visible.contains(screen.getName())){
                    screen.inactivate();
                    screen.makeVisible();
                } else {
                    screen.reset();
                    screen.inactivate();
                    screen.makeInvisible();
                }
            }
        } else {
            for(Screen screen : screens){
                if(screen.getName() == activeScreen.getName()){
                    if(screen.getName() == ScreenName.SAVE_LOAD_GAME){
                        if(choosingGame){ ((SaveScreen)screen).activate(SaveScreen.SaveType.LOAD_GAME); }
                        else { ((SaveScreen)screen).activate(SaveScreen.SaveType.SAVE_GAME); }
                    } else if(screen.getName() == ScreenName.SAVE_LOAD_LEVEL){
                        if(choosingLevel && !MenuScreen.loadingGame){ ((SaveScreen)screen).activate(SaveScreen.SaveType.LOAD_LEVEL); }
                        else if(choosingLevel) { ((SaveScreen)screen).activate(SaveScreen.SaveType.LOAD_GAME); }
                        else { ((SaveScreen)screen).activate(SaveScreen.SaveType.SAVE_LEVEL); }
                    } else if(screen.getName() == ScreenName.PAUSE){
                        if(this.curActiveScreen.getName() == ScreenName.GAME || curActiveScreen.getName() == ScreenName.SAVE_LOAD_GAME){
                            ((PauseScreen)screen).activate(false);
                        } else {
                            ((PauseScreen)screen).activate(true);
                        }
                    } else {
                        screen.activate();
                    }
                    screen.makeVisible();
                } else if(visible.contains(screen.getName())){
                    screen.inactivate();
                    screen.makeVisible();
                } else {
                    screen.reset();
                    screen.inactivate();
                    screen.makeInvisible();
                }
            }
        }
        this.prevActiveScreen = this.curActiveScreen;
        this.curActiveScreen = activeScreen;
    }

    private void saveLevelTo(String fileName){
        for(Screen screen : screens){
            if(screen.getName() == ScreenName.EDITOR){
                ((EditorScreen)screen).saveTo(fileName);
            }
        }
    }

    private void saveGameTo(String fileName){
        for(Screen screen : screens){
            if(screen.getName() == ScreenName.GAME){
                ((LastScreen)screen).saveTo(fileName);
            }
        }
    }

    private void loadLevelFrom(String fileName){
        for(Screen screen : screens){
            if(screen.getName() == ScreenName.EDITOR){
                ((EditorScreen)screen).loadFrom(fileName);
            }
        }
    }

    @Override
    protected void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(this.prevActiveScreen != null && this.curActiveScreen != null && this.prevActiveScreen.getName() == ScreenName.PAUSE && this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_LEVEL){
            String levelSaveFile = null;
            for(Screen screen : screens){
                if(screen.getName() == ScreenName.SAVE_LOAD_LEVEL){
                    levelSaveFile = ((SaveScreen)screen).getSaveFile();
                }
            }
            if(levelSaveFile != null){ this.saveLevelTo(levelSaveFile); }
        } else if(this.prevActiveScreen != null && this.curActiveScreen != null && this.prevActiveScreen.getName() == ScreenName.PAUSE && this.curActiveScreen.getName() == ScreenName.SAVE_LOAD_GAME){
            String gameSaveFile = null;
            for(Screen screen : screens){
                if(screen.getName() == ScreenName.SAVE_LOAD_GAME){
                    gameSaveFile = ((SaveScreen)screen).getSaveFile();
                }
            }
            if(gameSaveFile != null){
                this.saveGameTo(gameSaveFile);
            }
        }
    }
}
