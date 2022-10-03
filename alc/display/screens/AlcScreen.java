package alc.display.screens;

import alc.game.AlcWorld;
import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.Background;
import engine.display.uiElements.UIElement;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class AlcScreen extends Screen {

    private Viewport viewport;

    public AlcScreen(ScreenName name) {
        super(name);
    }

    public AlcScreen(){
        super(ScreenName.GAME);
        Viewport viewport = new Viewport(new Vec2d(960,540), new Vec2d(480,270), new Vec2d(960,540), new Vec2d(0,0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(Color.rgb(189,154,221), new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
    }

    public void reset(){
        super.reset();
        viewport.setDisplay(new Vec2d(960,540), new Vec2d(480,270));
    }

    public void activate(){
        super.activate();
        AlcWorld alcWorld = new AlcWorld();
        viewport.setWorld(alcWorld);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(ele.inRange(e) && ele.getName().equals("Back Button")){
                this.nextScreen = ScreenName.MENU;
            }
        }
    }

}
