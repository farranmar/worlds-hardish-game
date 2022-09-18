package tic.screens;

import engine.Screen;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import engine.support.Vec2i;
import engine.uiElements.Button;
import engine.uiElements.Text;
import engine.uiElements.UIElement;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class MenuScreen extends Screen {

    private Color primaryColor;
    private Color secondaryColor;
    private Vec2d screenSize;

    public MenuScreen(){
        super();
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.addStandardElements();
    }

    public MenuScreen(ArrayList<UIElement> uiElements) {
        super(uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.addStandardElements();
    }

    public MenuScreen(Color primaryColor, Color secondaryColor, Vec2d size){
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.screenSize = size;
        this.addStandardElements();
    }

    private void addStandardElements(){
        Font titleFont = new Font("Courier", 48);
        FontMetrics toTicMetrics = new FontMetrics("to tic", titleFont);
        double centerY = this.screenSize.y / 2;
        Text welcome = new Text("welcome", primaryColor, centerY - toTicMetrics.height-10, titleFont);
        Text toTic = new Text("to tic", primaryColor, centerY, titleFont);

        Vec2d buttonSize = new Vec2d(100,30);
        double spacing = 60;
        Button playButton = new Button(primaryColor, centerY+spacing, buttonSize);
        playButton.setText("play", "Courier");
        Button quitButton = new Button(primaryColor, centerY+1.2*spacing+buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        super.add(welcome);
        super.add(toTic);
        super.add(playButton);
        super.add(quitButton);
    }


}
