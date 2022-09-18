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

  public App(String title) {
    super(title);
    this.createScreens();
  }

  public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
    super(title, windowSize, debugMode, fullscreen);
    this.createScreens();
  }

  private void createScreens(){
    Screen backgroundScreen = createBackgroundScreen();
    this.add(backgroundScreen);
    backgroundScreen.makeVisible();
    MenuScreen menuScreen = new MenuScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE);
    this.add(menuScreen);
//    menuScreen.makeVisible();
    GameScreen gameScreen = new GameScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE);
    this.add(gameScreen);
    gameScreen.makeVisible();
    gameScreen.activate();
//    gameScreen.getTimer().start();
    GameOverScreen gameOverScreen = new GameOverScreen(primaryColor, secondaryColor, DEFAULT_STAGE_SIZE, GameOverScreen.Result.X);
    this.add(gameOverScreen);
//    gameOverScreen.makeVisible();
//    gameOverScreen.activate();
  }

  private Screen createBackgroundScreen(){
    Background blackBackground = new Background(Color.rgb(0,0,0));
    Background background = new Background(Color.rgb(69,69,69), DEFAULT_STAGE_SIZE);
    Screen backgroundScreen = new MenuScreen();
    backgroundScreen.add(blackBackground);
    backgroundScreen.add(background);
    return backgroundScreen;
  }

}
