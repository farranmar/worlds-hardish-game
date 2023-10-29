package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.Button;
import engine.display.uiElements.Text;
import engine.display.uiElements.UIElement;
import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class MenuScreen extends Screen {

    private Color primaryColor;
    private ArrayList<UIElement> basicEles = new ArrayList<>();
    private ArrayList<UIElement> gameEles = new ArrayList<>();
    private ArrayList<UIElement> levelEles = new ArrayList<>();
    public static boolean loadingGame = false;

    public MenuScreen(){
        super(ScreenName.MENU);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.basicEles;
    }

    public MenuScreen(ArrayList<UIElement> uiElements) {
        super(ScreenName.MENU, uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.basicEles;
    }

    public MenuScreen(Color primaryColor, Vec2d size){
        super(ScreenName.MENU);
        this.primaryColor = primaryColor;
        this.screenSize = size;
        this.addStandardElements();
        this.uiElements = this.basicEles;
    }

    public MenuScreen(Color primaryColor){
        super(ScreenName.MENU);
        this.primaryColor = primaryColor;
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.basicEles;
    }

    private void addStandardElements(){
        this.addBasicEles();
        this.addGameEles();
        this.addLevelEles();
    }

    private void addBasicEles(){
        Font titleFont = new Font("Courier", 48);
        double centerishY = this.screenSize.y / 2;
        Text nin = new Text("world's hardish game", primaryColor, centerishY, titleFont);
        Vec2d buttonSize = new Vec2d(160,30);
        double spacing = 60;
        Button playGameButton = new Button(primaryColor, centerishY+spacing, buttonSize);
        playGameButton.setText("play game", "Courier");
        Button makeLevelButton = new Button(primaryColor, centerishY+1.2*spacing+buttonSize.y, buttonSize);
        makeLevelButton.setText("make level", "Courier");
        Button instructionsButton = new Button(primaryColor, centerishY+1.4*spacing+2*buttonSize.y, buttonSize);
        instructionsButton.setText("instructions", "Courier");
        Button quitButton = new Button(primaryColor, centerishY+1.6*spacing+3*buttonSize.y, buttonSize);
        quitButton.setText("quit", "Courier");

        this.basicEles.add(nin);
        this.basicEles.add(playGameButton);
        this.basicEles.add(makeLevelButton);
        this.basicEles.add(instructionsButton);
        this.basicEles.add(quitButton);
    }

    private void addGameEles(){
        Font titleFont = new Font("Courier", 48);
        double centerishY = this.screenSize.y / 2;
        Text nin = new Text("last", primaryColor, centerishY, titleFont);
        Vec2d buttonSize = new Vec2d(220,30);
        double spacing = 60;
        Button newGameButton = new Button(primaryColor, centerishY+spacing, buttonSize);
        newGameButton.setText("start new game", "Courier");
        Button savedGameButton = new Button(primaryColor, centerishY+1.2*spacing+buttonSize.y, buttonSize);
        savedGameButton.setText("load saved game", "Courier");
        Button levelGameButton = new Button(primaryColor, centerishY+1.4*spacing+2*buttonSize.y, buttonSize);
        levelGameButton.setText("play custom level", "Courier");
        BackButton backButton = new BackButton(this.primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);

        this.gameEles.add(nin);
        this.gameEles.add(newGameButton);
        this.gameEles.add(savedGameButton);
        this.gameEles.add(levelGameButton);
        this.gameEles.add(backButton);
    }

    private void addLevelEles(){
        Font titleFont = new Font("Courier", 48);
        double centerishY = this.screenSize.y / 2;
        Text nin = new Text("last", primaryColor, centerishY, titleFont);
        Vec2d buttonSize = new Vec2d(140,30);
        double spacing = 60;
        Button newGameButton = new Button(primaryColor, centerishY+spacing, buttonSize);
        newGameButton.setText("new level", "Courier");
        Button savedGameButton = new Button(primaryColor, centerishY+1.2*spacing+buttonSize.y, buttonSize);
        savedGameButton.setText("edit level", "Courier");
        BackButton backButton = new BackButton(this.primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);

        this.levelEles.add(nin);
        this.levelEles.add(newGameButton);
        this.levelEles.add(savedGameButton);
        this.levelEles.add(backButton);
    }

    private void setScreens(){
        for(UIElement ele : this.basicEles){
            ele.setScreen(this);
        }
        for(UIElement ele : this.gameEles){
            ele.setScreen(this);
        }
        for(UIElement ele : this.levelEles){
            ele.setScreen(this);
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.uiElements = this.basicEles;
    }

    @Override
    public void onResize(Vec2d newSize) {
        this.windowSize = newSize;
        if(newSize.x/newSize.y == 16.0/9.0){
            this.screenSize = newSize;
        } else if(newSize.x/newSize.y < 16.0/9.0){
            double x = newSize.x;
            double y = newSize.x * (9.0/16.0);
            this.screenSize = new Vec2d(x,y);
        } else {
            double x = newSize.y * (16.0/9.0);
            double y = newSize.y;
            this.screenSize = new Vec2d(x,y);
        }

        for(UIElement ele : this.basicEles){
            ele.onResize(this.windowSize, this.screenSize);
        }
        for(UIElement ele : this.gameEles){
            ele.onResize(this.windowSize, this.screenSize);
        }
        for(UIElement ele : this.levelEles){
            ele.onResize(this.windowSize, this.screenSize);
        }
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(!ele.inRange(e)) { continue; }
            String name = ele.getName();
            if(name.contains("play game")){
                this.uiElements = this.gameEles;
            } else if(name.contains("make level")){
                this.uiElements = this.levelEles;
            } else if(name.contains("Back Button")){
                this.uiElements = this.basicEles;
            } else if(name.contains("start new game")){
                this.nextScreen = ScreenName.GAME;
            } else if(name.contains("load saved game")){
                this.nextScreen = ScreenName.SAVE_LOAD_GAME;
            } else if(name.contains("play custom level")){
                loadingGame = true;
                this.nextScreen = ScreenName.SAVE_LOAD_LEVEL;
            } else if(name.contains("new level")){
                this.nextScreen = ScreenName.EDITOR;
            } else if(name.contains("edit level")){
                loadingGame = false;
                this.nextScreen = ScreenName.SAVE_LOAD_LEVEL;
            } else if(name.contains("instructions")){
                this.nextScreen = ScreenName.INSTRUCTIONS;
            } else if(name.contains("quit")){
                this.nextScreen = ScreenName.QUIT;
            }
        }
    }

}
