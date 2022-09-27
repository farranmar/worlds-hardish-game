package alc.display.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.Button;
import engine.display.uiElements.Text;
import engine.display.uiElements.UIElement;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tic.App;

import java.util.ArrayList;

public class MenuScreen extends Screen {

    private Color primaryColor;

    public MenuScreen(){
        super(ScreenName.MENU);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = new Vec2d(960,540);
        this.addStandardElements();
    }

    public MenuScreen(ArrayList<UIElement> uiElements) {
        super(ScreenName.MENU, uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = new Vec2d(960,540);
        this.addStandardElements();
    }

    public MenuScreen(Color primaryColor, Vec2d size){
        super(ScreenName.MENU);
        this.primaryColor = primaryColor;
        this.screenSize = size;
        this.addStandardElements();
    }

    private void addStandardElements(){
        Font titleFont = new Font("Courier", 48);
        double centerY = this.screenSize.y / 2;
        Text alchemy = new Text("alchemy", primaryColor, centerY, titleFont);

        Vec2d buttonSize = new Vec2d(100,30);
        double spacing = 60;
        Button playButton = new Button(primaryColor, centerY+spacing, buttonSize);
        playButton.setText("play", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        super.add(alchemy);
        super.add(playButton);
        super.add(quitButton);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(ele.inRange(e) && ele.getName().contains("play")){
                this.nextScreen = ScreenName.GAME;
            } else if(ele.inRange(e) && ele.getName().contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
