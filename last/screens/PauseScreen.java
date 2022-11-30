package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PauseScreen extends Screen {

    private Color primaryColor;
    private boolean level = false;

    public PauseScreen(){
        super(ScreenName.PAUSE);
        this.primaryColor = Color.BLACK;
        this.addElements();
    }

    public PauseScreen(Color color){
        super(ScreenName.PAUSE);
        this.primaryColor = color;
        this.addElements();
    }

    private void addElements(){
        Background background = new Background(Color.rgb(48, 54, 51, 0.85));
        this.add(background);

        double centerY = this.screenSize.y/2;
        Vec2d buttonSize=  new Vec2d(110,30);
        double spacing = 12+buttonSize.y;

        Text gamePaused = new Text("game paused", this.primaryColor, centerY, new Font("Courier", 48));
        this.add(gamePaused);
        Button continueButton = new Button(this.primaryColor, centerY+24, buttonSize);
        continueButton.setText("continue", "Courier");
        this.add(continueButton);
        Button save = new Button(this.primaryColor, centerY+spacing+24, buttonSize);
        save.setText("save", "Courier");
        this.add(save);
        Button menu = new Button(this.primaryColor, centerY+spacing*2+24, buttonSize);
        menu.setText("menu", "Courier");
        this.add(menu);
        Button quit = new Button(this.primaryColor, centerY+spacing*3+24, buttonSize);
        quit.setText("quit", "Courier");
        this.add(quit);
    }

    public void activate(boolean level) {
        super.activate();
        this.level = level;
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements) {
            if (!ele.inRange(e)) {
                continue;
            }
            String name = ele.getName();
            if (name.contains("continue")) {
                if(this.level){ this.nextScreen = ScreenName.EDITOR; }
                else { this.nextScreen = ScreenName.GAME; }
            } else if (name.contains("save")) {
                if(this.level){ this.nextScreen = ScreenName.SAVE_LOAD_LEVEL; }
                else { this.nextScreen = ScreenName.SAVE_LOAD_GAME; }
            } else if (name.contains("menu")) {
                this.nextScreen = ScreenName.MENU;
            } else if (name.contains("quit")) {
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
