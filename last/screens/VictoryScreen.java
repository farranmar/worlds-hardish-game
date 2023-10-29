package last.screens;

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

public class VictoryScreen extends Screen {

    private Color primaryColor;
    private boolean finalVictory;
    private Button nextLevelBut;
    private Text victoryMessage;

    public VictoryScreen(Color primaryColor, boolean fV) {
        super(ScreenName.GAME_OVER);
        this.primaryColor = primaryColor;
        this.finalVictory = fV;
        this.addStandardElements();
    }

    public VictoryScreen(Color primaryColor){
        this(primaryColor, false);
    }

    public void setFinalVictory(boolean fV){
        if(fV){ 
            this.remove(this.nextLevelBut); 
            this.victoryMessage.setText("game cleared!");
        } else {
            if(!this.contains(this.nextLevelBut)){ this.add(this.nextLevelBut); }
            this.victoryMessage.setText("victory!");
        }
        this.finalVictory = fV;
    }

    public void reset(){
        super.reset();
        this.finalVictory = false;
        if(!this.contains(this.nextLevelBut)){ this.add(this.nextLevelBut); }
        this.victoryMessage.setText("victory!");
    }

    private void addStandardElements(){
        Background overlay = new Background(Color.rgb(48, 54, 51, 0.85));

        String messageString = finalVictory ? "game cleared!" : "victory!";
        Text message = new Text(messageString, this.primaryColor, this.screenSize.y/2, new Font("Courier", 48));
        this.victoryMessage = message;

        Vec2d buttonSize = new Vec2d(140,30);
        double spacing = 60;
        double centerY = this.screenSize.y/2;
        Button nextLevelButton = new Button(primaryColor, centerY+spacing, buttonSize);
        nextLevelButton.setText("next level", "Courier");
        this.nextLevelBut = nextLevelButton;
        Button menuButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        menuButton.setText("menu", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.4*spacing+2*buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        this.add(overlay);
        this.add(message);
        this.add(nextLevelButton);
        this.add(menuButton);
        this.add(quitButton);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(ele.inRange(e) && ele.getName().contains("next level")){
                this.nextScreen = ScreenName.GAME;
            } else if(ele.inRange(e) && ele.getName().contains("menu")){
                this.nextScreen = ScreenName.MENU;
            } else if(ele.inRange(e) && ele.getName().contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
