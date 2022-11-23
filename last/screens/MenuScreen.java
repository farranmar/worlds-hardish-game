package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.Button;
import engine.display.uiElements.Text;
import engine.display.uiElements.UIElement;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class MenuScreen extends Screen {

    private Color primaryColor;

    public MenuScreen(){
        super(ScreenName.MENU);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
    }

    public MenuScreen(ArrayList<UIElement> uiElements) {
        super(ScreenName.MENU, uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
    }

    public MenuScreen(Color primaryColor, Vec2d size){
        super(ScreenName.MENU);
        this.primaryColor = primaryColor;
        this.screenSize = size;
        this.addStandardElements();
    }

    public MenuScreen(Color primaryColor){
        super(ScreenName.MENU);
        this.primaryColor = primaryColor;
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
    }

    private void addStandardElements(){
        Font titleFont = new Font("Courier", 48);
        double centerY = this.screenSize.y / 2;
        Text nin = new Text("last", primaryColor, centerY, titleFont);
        Vec2d buttonSize = new Vec2d(110,30);
        double spacing = 60;
        Button playButton = new Button(primaryColor, centerY+spacing, buttonSize);
        playButton.setText("play", "Courier");
        Button settingsButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        settingsButton.setText("settings", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.4*spacing+2*buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        super.add(nin);
        super.add(playButton);
        super.add(settingsButton);
        super.add(quitButton);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(ele.inRange(e) && ele.getName().contains("play")){
                this.nextScreen = ScreenName.GAME;
            } else if(ele.inRange(e) && ele.getName().contains("settings")){
                this.nextScreen = ScreenName.SETTINGS;
            } else if(ele.inRange(e) && ele.getName().contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
