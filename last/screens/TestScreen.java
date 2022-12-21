package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class TestScreen extends Screen {

    public TestScreen(){
        super(ScreenName.MENU);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        TextBox textBox = new TextBox("enter save file name:", Color.DARKBLUE, new Vec2d(300, 200), new Font("Courier", 36), 30);
        this.add(textBox);
    }

}
