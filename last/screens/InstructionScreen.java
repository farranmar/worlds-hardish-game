package last.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.game.components.DragComponent;
import engine.game.world.GameWorld;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import last.game.EditorWorld;
import last.game.LastWorld;
import last.game.objects.*;

import java.util.ArrayList;

public class InstructionScreen extends Screen {

    private Color primaryColor;
    private ArrayList<UIElement> gameEles = new ArrayList<>();
    private ArrayList<UIElement> levelEles = new ArrayList<>();
    private LastWorld lastWorld;

    public InstructionScreen(){
        super(ScreenName.INSTRUCTIONS);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.gameEles;
    }

    public InstructionScreen(ArrayList<UIElement> uiElements) {
        super(ScreenName.INSTRUCTIONS, uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.gameEles;
    }

    public InstructionScreen(Color primaryColor, Vec2d size){
        super(ScreenName.INSTRUCTIONS);
        this.primaryColor = primaryColor;
        this.screenSize = size;
        this.addStandardElements();
        this.uiElements = this.gameEles;
    }

    public InstructionScreen(Color primaryColor){
        super(ScreenName.INSTRUCTIONS);
        this.primaryColor = primaryColor;
        this.screenSize = DEFAULT_SCREEN_SIZE;
        this.addStandardElements();
        this.uiElements = this.gameEles;
    }

    private void addStandardElements(){
        this.addGameEles();
        this.addLevelEles();
    }

    private void addGameEles(){
        this.gameEles.clear();
        Font titleFont = new Font("Courier", 48);
        Font textFont = new Font("Courier", 24);
        double leftMargin = this.screenSize.x/2 - (new FontMetrics("to play: use WASD to move", textFont).width / 2);

        BackButton backButton = new BackButton(primaryColor, new Vec2d(30), new Vec2d(50,30));
        backButton.appendToName(" to menu");
        Text gameplay = new Text("gameplay", primaryColor, 75, titleFont);
        Text toPlay = new Text("to play: use WASD to move", primaryColor, new Vec2d(leftMargin, 150), textFont);
        Text toWin = new Text("to win: get to the end", primaryColor, new Vec2d(leftMargin, 184), textFont);
        Text oneTwo = new Text("1 2", primaryColor, 516, new Font("Courier", 36));
        Text of = new Text("of", primaryColor, 510, new Font("Courier", 16));
        BackButton nextPageButton = new BackButton(primaryColor, new Vec2d(this.screenSize.x/2 + 40, 490), new Vec2d(40, 30));
        nextPageButton.setFacingLeft(false);
        nextPageButton.appendToName(" next page");

        LastWorld lastWorld = new LastWorld(true);
        lastWorld.setSize(new Vec2d(320, 250));
        lastWorld.add(new Wall(lastWorld, new Vec2d(30, 250), new Vec2d(0)));
        lastWorld.add(new Wall(lastWorld, new Vec2d(30, 250), new Vec2d(290, 0)));
        lastWorld.add(new Wall(lastWorld, new Vec2d(320, 30), new Vec2d(0)));
        lastWorld.add(new Wall(lastWorld, new Vec2d(320, 30), new Vec2d(0, 220)));
        lastWorld.add(new Player(lastWorld, new Vec2d(30), new Vec2d(40, 110)));
        lastWorld.add(new Checkpoint(lastWorld, new Vec2d(50, 190), new Vec2d(30), new Vec2d(40, 110)));
        lastWorld.add(new EndPoint(lastWorld, new Vec2d(50, 190), new Vec2d(240, 30)));
        DeathBall deathBall = new DeathBall(lastWorld, new Vec2d(150, 110));
        deathBall.setPathPointPosOne(new Vec2d(165, 45));
        deathBall.setPathPointPosTwo(new Vec2d(165, 205));
        deathBall.setMoving(true);
        lastWorld.add(deathBall);
        Viewport viewport = new Viewport(new Vec2d(320, 250), new Vec2d(0), new Vec2d(320, 250), new Vec2d(320, 204));
        viewport.setWorld(lastWorld);
        this.lastWorld = lastWorld;

        this.gameEles.add(viewport);
        this.gameEles.add(backButton);
        this.gameEles.add(gameplay);
        this.gameEles.add(toPlay);
        this.gameEles.add(toWin);
        this.gameEles.add(oneTwo);
        this.gameEles.add(of);
        this.gameEles.add(nextPageButton);
    }

    private void addLevelEles(){
        this.levelEles.clear();
        Font titleFont = new Font("Courier", 48);
        Font textFont = new Font("Courier", 24);
        double leftMargin = this.screenSize.x/2 - (new FontMetrics("to create: drag and drop items from the menu into the level", textFont).width / 2);
        double leftMarginCreate = leftMargin + new FontMetrics("to create: ", textFont).width;
        double leftMarginPlay = leftMargin + new FontMetrics("to play: ", textFont).width;

        BackButton backButton = new BackButton(primaryColor, new Vec2d(30), new Vec2d(50,30));
        backButton.appendToName("to menu");
        Text levelEditor = new Text("level editor", primaryColor, 75, titleFont);
        Text dragAndDrop = new Text("to create: - drag and drop items from the menu into the level", primaryColor, new Vec2d(leftMargin, 129), textFont);
        Text resize = new Text("- use the red dots to resize walls, checkpoints,", primaryColor, new Vec2d(leftMarginCreate, 163), textFont);
        Text endpoints = new Text("and endpoints", primaryColor, new Vec2d(leftMarginCreate, 189), textFont);
        Text path = new Text("- use the red and green dots to choose the path", primaryColor, new Vec2d(leftMarginCreate, 327), textFont);
        Text deathball = new Text("that a deathball will move on", primaryColor, new Vec2d(leftMarginCreate, 353), textFont);
        Text shift = new Text("- press shift to snap to grid", primaryColor, new Vec2d(leftMarginCreate, 387), textFont);
        Text save = new Text("to play: - save your level through the pause menu, then return", primaryColor, new Vec2d(leftMargin, 431), textFont);
        Text play = new Text("to the start screen to play the game", primaryColor, new Vec2d(leftMarginPlay, 457), textFont);
        Text twoTwo = new Text("2 2", primaryColor, 516, new Font("Courier", 36));
        Text of = new Text("of", primaryColor, 510, new Font("Courier", 16));
        BackButton lastPageButton = new BackButton(primaryColor, new Vec2d(this.screenSize.x/2 - 80, 490), new Vec2d(40, 30));
        lastPageButton.appendToName(" last page");

        EditorWorld editorWorld = new EditorWorld(true);
        editorWorld.setSize(new Vec2d(960, 540));
        Wall wall = new Wall(editorWorld, new Vec2d(30), new Vec2d(335, 231));
        wall.add(new DragComponent(wall));
        wall.setShowResizer(true);
        editorWorld.add(wall);
        DeathBall deathBall = new DeathBall(editorWorld, new Vec2d(565, 231));
        deathBall.setDrawPath(true);
        deathBall.setMoving(false);
        deathBall.addSlideComponent();
        editorWorld.add(deathBall);
        Viewport viewport = new Viewport(new Vec2d(960, 540), new Vec2d(0), new Vec2d(960, 540), new Vec2d(0));
        viewport.setWorld(editorWorld);

        this.levelEles.add(viewport);
        this.levelEles.add(backButton);
        this.levelEles.add(levelEditor);
        this.levelEles.add(dragAndDrop);
        this.levelEles.add(path);
        this.levelEles.add(shift);
        this.levelEles.add(save);
        this.levelEles.add(play);
        this.levelEles.add(resize);
        this.levelEles.add(endpoints);
        this.levelEles.add(deathball);
        this.levelEles.add(twoTwo);
        this.levelEles.add(of);
        this.levelEles.add(lastPageButton);
    }

    public void win(){
        Rectangle rect = new Rectangle(Color.rgb(48, 54, 51, 0.85), Color.rgb(48, 54, 51, 0.85), new Vec2d(320, 250), new Vec2d(320, 204));
        double height = new FontMetrics("victory!", new Font("Courier", 36)).height;
        Text victory = new Text("victory!", primaryColor, 329 + height/2, new Font("Courier", 36));
        this.uiElements.add(rect);
        this.uiElements.add(victory);
        this.lastWorld.setActive(false);
        this.lastWorld = null;
    }

    private void setScreens(){
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
        this.addStandardElements();
        this.onResize(this.windowSize);
        this.uiElements = this.gameEles;
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
            if(name.contains("to menu")){
                this.nextScreen = ScreenName.MENU;
            } else if(name.contains("next page")){
                this.addLevelEles();
                this.uiElements = this.levelEles;
            } else if(name.contains("last page")){
                this.addGameEles();
                this.uiElements = this.gameEles;
            }
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(this.lastWorld != null && this.lastWorld.getResult() == GameWorld.Result.VICTORY){
            this.win();
        }
    }
}
