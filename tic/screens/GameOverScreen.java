package tic.screens;

import engine.Screen;
import engine.support.Vec2d;
import engine.uiElements.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class GameOverScreen extends Screen {

    private Color primaryColor;
    private Color secondaryColor;
    private Vec2d screenSize;
    private Result result;

    public enum Result {
        X,
        O,
        DRAW,
        UNFINISHED
    }

    public GameOverScreen(){
        super();
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.result = Result.DRAW;
        this.addStandardElements();
    }

    public GameOverScreen(ArrayList<UIElement> uiElements) {
        super(uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.result = Result.DRAW;
        this.addStandardElements();
    }

    public GameOverScreen(Color primaryColor, Color secondaryColor, Vec2d size, Result result){
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.screenSize = size;
        this.result = result;
        this.addStandardElements();
    }

    private void addStandardElements(){
        String messageString = "";
        if(this.result == Result.X){ messageString = "X wins!"; }
        else if(this.result == Result.O){ messageString = "O wins!"; }
        else if(this.result == Result.DRAW){ messageString = "it's a draw"; }
        Text message = new Text(messageString, this.primaryColor, this.screenSize.y/2, new Font("Courier", 48));

        Vec2d buttonSize = new Vec2d(100,30);
        double spacing = 60;
        double centerY = this.screenSize.y/2;
        Button restartButton = new Button(primaryColor, centerY+spacing, buttonSize);
        restartButton.setText("restart", "Courier");
        Button menuButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        menuButton.setText("menu", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.4*spacing+2*buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        this.add(message);
        this.add(restartButton);
        this.add(menuButton);
        this.add(quitButton);
    }

    public void setColors(Color primary, Color secondary){
        this.primaryColor = primary;
        this.secondaryColor = secondary;
    }

}
