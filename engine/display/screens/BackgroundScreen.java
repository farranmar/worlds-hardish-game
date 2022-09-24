package engine.display.screens;

import engine.display.uiElements.Background;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class BackgroundScreen extends Screen {

    private Color screenColor;
    private Color edgeColor;

    public BackgroundScreen() {
        super("Background");
        this.screenColor = Color.rgb(69,69,69);
        this.edgeColor = Color.rgb(0,0,0);
        this.createScreen();
    }

    public BackgroundScreen(Color screenColor, Color edgeColor, Vec2d size) {
        super("Background");
        this.screenSize = size;
        this.screenColor = screenColor;
        this.edgeColor = edgeColor;
        this.createScreen();
    }

    public BackgroundScreen(Color screenColor, Vec2d size) {
        super("Background");
        this.screenSize = size;
        this.screenColor = screenColor;
        this.edgeColor = Color.rgb(0,0,0);
        this.createScreen();
    }

    private void createScreen(){
        Background edgeBackground = new Background(this.edgeColor);
        Background screenBackground = new Background(this.screenColor, this.screenSize);
        this.add(edgeBackground);
        this.add(screenBackground);
    }
}
