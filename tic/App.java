package tic;

import engine.Application;
import engine.Screen;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import engine.uiElements.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tic.screens.GameOverScreen;
import tic.screens.GameScreen;
import tic.screens.MenuScreen;
import tic.uiElements.Board;

import java.awt.*;

/**
 * This is your Tic-Tac-Toe top-level, App class.
 * This class will contain every other object in your game.
 */
public class App extends Application {

  private static final Vec2d DEFAULT_STAGE_SIZE = new Vec2d(960,540);
  private static final Color primaryColor = Color.rgb(255,136,213);
  private static final Color secondaryColor = Color.rgb(128,147,241);
  public static final String MENU = "Menu";
  public static final String GAME = "Game";
  public static final String GAME_OVER = "Game Over";
  public static final String QUIT = "Quit";
  public static final String BACKGROUND = "Background";

  private GameScreen gameScreen;
  private MenuScreen menuScreen;
  private GameOverScreen gameOverScreen;

  public App(String title) {
    super(title);
    this.createScreens();
  }

  public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
    super(title, windowSize, debugMode, fullscreen);
    this.createScreens();
  }

  @Override
  public void setActiveScreen(Screen activeScreen){
    if(activeScreen.getName().equals(GAME)){
      activeScreen.setColor(new Color[]{primaryColor, secondaryColor});
      activeScreen.reset();
    }
    if(!activeScreen.getName().equals(GAME_OVER)){
      super.setActiveScreen(activeScreen);
    } else { // activeScreen is GAME_OVER
      for(Screen screen : this.screens){
        gameOverScreen.setResult(gameScreen.getStatus());
        if(screen.getName().equals(BACKGROUND)){
          screen.inactivate();
          screen.makeVisible();
        } else if(screen.getName().equals(MENU)){
          screen.inactivate();
          screen.makeInvisible();
        } else if(screen.getName().equals(GAME)){
          screen.inactivate();
          screen.makeVisible();
        } else if(screen.getName().equals(GAME_OVER)){
          screen.activate();
          screen.makeVisible();
        }
      }
    }
  }

  private void createScreens(){
    Screen backgroundScreen = createBackgroundScreen();
    this.add(backgroundScreen);
    backgroundScreen.makeVisible();
    MenuScreen menuScreen = new MenuScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE);
    this.add(menuScreen);
    this.menuScreen = menuScreen;
    menuScreen.activate();
    menuScreen.makeVisible();
    GameScreen gameScreen = new GameScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE);
    this.add(gameScreen);
    this.gameScreen = gameScreen;
    GameOverScreen gameOverScreen = new GameOverScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE, GameOverScreen.Result.X);
    this.add(gameOverScreen);
    this.gameOverScreen = gameOverScreen;
  }

  private Screen createBackgroundScreen(){
    Background blackBackground = new Background(Color.rgb(0,0,0));
    Background background = new Background(Color.rgb(69,69,69), DEFAULT_STAGE_SIZE);
    Screen backgroundScreen = new Screen("Background");
    backgroundScreen.add(blackBackground);
    backgroundScreen.add(background);
    return backgroundScreen;
  }

  @Override
  public void onTick(long nanosSinceLastTick){
    super.onTick(nanosSinceLastTick);

  }

}
