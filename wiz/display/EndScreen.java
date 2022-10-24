package wiz.display;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.Background;
import engine.display.uiElements.Button;
import engine.display.uiElements.Text;
import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tic.screens.GameOverScreen;

public class EndScreen extends Screen {

    private GameWorld.Result result;
    private Color primaryColor;
    private Text message;

    public EndScreen(Color primaryColor, GameWorld.Result result) {
        super(ScreenName.GAME_OVER);
        this.result = result;
        this.primaryColor = primaryColor;
        this.addStandardElements();
    }

    public EndScreen(Color primaryColor) {
        super(ScreenName.GAME_OVER);
        this.result = GameWorld.Result.PLAYING;
        this.primaryColor = primaryColor;
        this.addStandardElements();
    }

    public void setResult(GameWorld.Result result){
        this.result = result;
        if(result == GameWorld.Result.VICTORY){ this.message.setText("victory!"); }
        else if(result == GameWorld.Result.DEFEAT){ this.message.setText("oops, you died :("); }
    }

    private void addStandardElements(){
        Background overlay = new Background(Color.rgb(69, 69, 69, 0.7));

        String messageString = "";
        if(this.result == GameWorld.Result.VICTORY){ messageString = "victory!"; }
        else { messageString = "oops, you died :("; }
        Text message = new Text(messageString, this.primaryColor, this.screenSize.y/2, new Font("Courier", 48));
        this.message = message;

        Vec2d buttonSize = new Vec2d(100,30);
        double spacing = 60;
        double centerY = this.screenSize.y/2;
        Button restartButton = new Button(primaryColor, centerY+spacing, buttonSize);
        restartButton.setText("restart", "Courier");
        Button menuButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        menuButton.setText("menu", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.4*spacing+2*buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        this.add(overlay);
        this.add(message);
        this.add(restartButton);
        this.add(menuButton);
        this.add(quitButton);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(ele.inRange(e) && ele.getName().contains("restart")){
                this.result = GameWorld.Result.PLAYING;
                this.nextScreen = ScreenName.GAME;
            } else if(ele.inRange(e) && ele.getName().contains("menu")){
                this.result = GameWorld.Result.PLAYING;
                this.nextScreen = ScreenName.MENU;
            } else if(ele.inRange(e) && ele.getName().contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
