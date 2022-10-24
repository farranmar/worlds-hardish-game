package wiz;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import wiz.display.EndScreen;
import wiz.display.MenuScreen;
import engine.Application;
import engine.display.screens.BackgroundScreen;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import wiz.display.WizScreen;

public class App extends Application {

    private static Vec2d DEFAULT_WINDOW_SIZE = new Vec2d(960,540);
    private EndScreen endScreen;
    private WizScreen wizScreen;

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
        WizScreen wizScreen = new WizScreen();
        wizScreen.inactivate();
        wizScreen.makeInvisible();
        this.wizScreen = wizScreen;
        this.add(wizScreen);
        EndScreen endScreen = new EndScreen(Color.rgb(189, 154, 221));
        endScreen.inactivate();
        endScreen.makeInvisible();
        this.endScreen = endScreen;
        this.add(endScreen);
    }

    public void setActiveScreen(Screen activeScreen){
        if(activeScreen.getName() == ScreenName.GAME){ activeScreen.reset(); }
        if(activeScreen.getName() != ScreenName.GAME_OVER){ super.setActiveScreen(activeScreen); }
        else {
            endScreen.setResult(wizScreen.getResult());
            for(Screen screen : this.screens){
                if(screen.getName().equals(ScreenName.BACKGROUND)){
                    screen.inactivate();
                    screen.makeVisible();
                } else if(screen.getName().equals(ScreenName.MENU)){
                    screen.inactivate();
                    screen.makeInvisible();
                } else if(screen.getName().equals(ScreenName.GAME)){
                    screen.inactivate();
                    screen.makeVisible();
                } else if(screen.getName().equals(ScreenName.GAME_OVER)){
                    screen.activate();
                    screen.makeVisible();
                }
            }
        }
    }

}
