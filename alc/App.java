package alc;

import alc.display.screens.AlcScreen;
import alc.display.screens.MenuScreen;
import engine.Application;
import engine.display.screens.BackgroundScreen;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class App extends Application {

    private static Vec2d DEFAULT_WINDOW_SIZE = new Vec2d(960,540);

    public App(String title) {
        super(title);
        this.createScreens();
    }

    public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
        super(title, windowSize, debugMode, fullscreen);
        this.createScreens();
    }

    private void createScreens(){
        BackgroundScreen backgroundScreen = new BackgroundScreen(Color.rgb(69,69,69), DEFAULT_WINDOW_SIZE);
        backgroundScreen.inactivate();
        backgroundScreen.makeVisible();
        this.add(backgroundScreen);
        MenuScreen menuScreen = new MenuScreen(Color.rgb(189,154,221), new Vec2d(960,540));
        menuScreen.activate();
        menuScreen.makeVisible();
        this.add(menuScreen);
        AlcScreen alcScreen = new AlcScreen();
        alcScreen.inactivate();
        alcScreen.makeInvisible();
        this.add(alcScreen);
    }

}
