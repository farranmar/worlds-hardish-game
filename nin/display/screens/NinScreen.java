package nin.display.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import nin.game.NinWorld;

public class NinScreen extends Screen {

    private NinWorld world;

    public NinScreen(){
        super(ScreenName.GAME);
        Viewport viewport = new Viewport(new Vec2d(960,540), new Vec2d(480, 270), new Vec2d(960,540), new Vec2d(0,0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(Color.rgb(189,154,221), new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
    }

    public void activate(){
        super.activate();
        NinWorld ninWorld = new NinWorld(false);
        ninWorld.setViewport(this.viewport);
        this.world = ninWorld;
        viewport.setWorld(ninWorld);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        if(this.viewport.getResult() != GameWorld.Result.PLAYING){
            this.nextScreen = ScreenName.GAME_OVER;
        }
    }

    public void onKeyPressed(KeyEvent e){
        super.onKeyPressed(e);
        if(e.getCode() == KeyCode.L){
            this.world = null;
            NinWorld newWorld = NinWorld.loadFrom("save-file.xml");
            newWorld.setViewport(this.viewport);
            this.world = newWorld;
            this.viewport.setWorld(this.world);
        }
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