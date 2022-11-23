package last;

import engine.Application;
import engine.display.screens.BackgroundScreen;
import engine.display.uiElements.Background;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import last.screens.MenuScreen;

public class App extends Application {

    private static Vec2d DEFAULT_WINDOW_SIZE = new Vec2d(960,540);

    public App(String title) {
        super(title, DEFAULT_WINDOW_SIZE, true, false);
        this.createScreens();
    }

    public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
        super(title, windowSize, debugMode, fullscreen);
        this.createScreens();
    }

    public void createScreens() {
        BackgroundScreen backgroundScreen = new BackgroundScreen(Color.rgb(69,69,69), Color.BLACK);
        backgroundScreen.inactivate();
        backgroundScreen.makeVisible();
        this.add(backgroundScreen);
        MenuScreen menuScreen = new MenuScreen(Color.rgb(117,162,129));
        menuScreen.activate();
        menuScreen.makeVisible();
        this.add(menuScreen);
        EditorScreen editorScreen = new EditorScreen();
        editorScreen.makeInvisible();
        editorScreen.inactivate();
        this.add(editorScreen);
    }
}
