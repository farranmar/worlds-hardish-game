package last;

import engine.Application;
import engine.support.Vec2d;

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

    }
}
