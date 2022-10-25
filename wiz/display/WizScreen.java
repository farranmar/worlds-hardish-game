package wiz.display;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.Text;
import engine.display.uiElements.UIElement;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import wiz.game.WizWorld;

import java.util.Random;

public class WizScreen extends Screen {

    private Viewport viewport;
    private long seed = new Random().nextLong();
    private Text seedLabel;

    public WizScreen(){
        super(ScreenName.GAME);
        Viewport viewport = new Viewport(new Vec2d(400,225), new Vec2d(0), new Vec2d(960,540), new Vec2d(0,0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(Color.rgb(189,154,221), new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
        seedLabel = new Text("seed: "+this.seed, Color.WHITE, new Vec2d(10, this.screenSize.y - 10), new Font("Courier", 12));
        this.add(seedLabel);
    }

    public void reset(){
        super.reset();
        viewport.setDisplay(new Vec2d(400,225), new Vec2d(0));
    }

    public GameWorld.Result getResult(){
        return this.viewport.getResult();
    }

    public void setSeed(long s){
        this.seed = s;
        seedLabel.setText("seed: "+this.seed);
    }

    public void activate(){
        super.activate();
        WizWorld wizWorld = new WizWorld("Wiz", this.seed);
        wizWorld.setViewport(this.viewport);
        viewport.setWorld(wizWorld);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        if(this.viewport.getResult() != GameWorld.Result.PLAYING){
            this.nextScreen = ScreenName.GAME_OVER;
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
