package engine;

import engine.support.FXFrontEnd;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import tic.App;

import java.util.ArrayList;

/**
 * This is your main Application class that you will contain your
 * 'draws' and 'ticks'. This class is also used for controlling
 * user input.
 */
public class Application extends FXFrontEnd {

  protected ArrayList<Screen> screens;

  public Application(String title) {
    super(title);
    screens = new ArrayList<>();
  }
  public Application(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
    super(title, windowSize, debugMode, fullscreen);
    screens = new ArrayList<>();
  }

  public void add(Screen screen){
    screens.add(screen);
  }

  protected void setActiveScreen(Screen activeScreen){
    for(Screen screen : screens){
      if(screen.getName().equals("Background")){
        screen.inactivate();
        screen.makeVisible();
      } else if(screen.getName().equals(activeScreen.getName())){
        screen.activate();
        screen.makeVisible();
      } else {
        screen.reset();
        screen.inactivate();
        screen.makeInvisible();
      }
    }
  }

  /**
   * Called periodically and used to update the state of your game.
   * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
   */
  @Override
  protected void onTick(long nanosSincePreviousTick) {
    String nextScreen = "";
    for(Screen screen : screens){
      screen.onTick(nanosSincePreviousTick);
      String curNext = screen.getNextScreen();
      if(!curNext.equals("")){
        nextScreen = curNext;
      }
    }
    if(!nextScreen.equals("")){
      if(nextScreen.equals(App.QUIT)) {
        this.shutdown();
      }
      for(Screen screen : screens){
        if(screen.getName().equals(nextScreen)){
          this.setActiveScreen(screen);
        }
      }
    }
  }

  /**
   * Called after onTick().
   */
  @Override
  protected void onLateTick() {
    // Don't worry about this method until you need it. (It'll be covered in class.)
  }

  /**
   *  Called periodically and meant to draw graphical components.
   * @param g		a {@link GraphicsContext} object used for drawing.
   */
  @Override
  protected void onDraw(GraphicsContext g) {
    for(Screen screen : screens){
      screen.onDraw(g);
    }
  }

  /**
   * Called when a key is typed.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyTyped(KeyEvent e) {

  }

  /**
   * Called when a key is pressed.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyPressed(KeyEvent e) {
    if(e.getCode() == KeyCode.ESCAPE){
      this.shutdown();
    }
  }

  /**
   * Called when a key is released.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyReleased(KeyEvent e) {

  }

  /**
   * Called when the mouse is clicked.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseClicked(MouseEvent e) {
    for(Screen screen : screens){
      if(screen.isActive()){
        screen.onMouseClicked(e);
      }
    }
  }

  /**
   * Called when the mouse is pressed.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMousePressed(MouseEvent e) {

  }

  /**
   * Called when the mouse is released.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseReleased(MouseEvent e) {

  }

  /**
   * Called when the mouse is dragged.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseDragged(MouseEvent e) {

  }

  /**
   * Called when the mouse is moved.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseMoved(MouseEvent e) {
    for(Screen screen : screens){
      if(screen.isActive()){
        screen.onMouseMoved(e);
      }
    }
  }

  /**
   * Called when the mouse wheel is moved.
   * @param e		an FX {@link ScrollEvent} representing the input event.
   */
  @Override
  protected void onMouseWheelMoved(ScrollEvent e) {

  }

  /**
   * Called when the window's focus is changed.
   * @param newVal	a boolean representing the new focus state
   */
  @Override
  protected void onFocusChanged(boolean newVal) {

  }

  /**
   * Called when the window is resized.
   * @param newSize	the new size of the drawing area.
   */
  @Override
  protected void onResize(Vec2d newSize) {
    for(Screen screen : screens){
      screen.onResize(newSize);
    }
  }

  /**
   * Called when the app is shutdown.
   */
  @Override
  protected void onShutdown() {

  }

  /**
   * Called when the app is starting up.s
   */
  @Override
  protected void onStartup() {

  }

}
