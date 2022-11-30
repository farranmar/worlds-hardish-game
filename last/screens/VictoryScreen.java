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

    public VictoryScreen(Color primaryColor) {
        super(ScreenName.GAME_OVER);
        this.primaryColor = primaryColor;
        this.addStandardElements();
    }

    private void addStandardElements(){
        Background overlay = new Background(Color.rgb(48, 54, 51, 0.85));

        String messageString = "victory!";
        Text message = new Text(messageString, this.primaryColor, this.screenSize.y/2, new Font("Courier", 48));

        Vec2d buttonSize = new Vec2d(140,30);
        double spacing = 60;
        double centerY = this.screenSize.y/2;
        Button nextLevelButton = new Button(primaryColor, centerY+spacing, buttonSize);
        nextLevelButton.setText("next level", "Courier");
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
//                this.nextScreen = ScreenName.GAME;
            } else if(ele.inRange(e) && ele.getName().contains("menu")){
                this.nextScreen = ScreenName.MENU;
            } else if(ele.inRange(e) && ele.getName().contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
