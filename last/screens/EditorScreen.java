package last.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.PauseButton;
import engine.display.uiElements.UIElement;
import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import last.game.EditorWorld;
import last.game.objects.*;

import java.util.HashMap;
import java.util.Map;

public class EditorScreen extends Screen {

    public static HashMap<String, Class<? extends GameObject>> classMap = initializeClassMap();
    private Color primaryColor;

    public EditorScreen(Color color){
        super(ScreenName.EDITOR);
        this.primaryColor = color;
        Viewport viewport = new Viewport(new Vec2d(1920,1080), new Vec2d(0, 0), new Vec2d(960,540), new Vec2d(0,0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(this.primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
        PauseButton pauseButton = new PauseButton(this.primaryColor, new Vec2d(910, 30), new Vec2d(25, 35));
        this.add(pauseButton);

        EditorWorld world = new EditorWorld();
        world.setViewport(this.viewport);
        viewport.setWorld(world);
    }

    private static HashMap<String, Class<? extends GameObject>> initializeClassMap(){
        HashMap<String, Class<? extends GameObject>> cM = new HashMap<>();
        cM.put("Unit", Unit.class);
        cM.put("UnitMenu", UnitMenu.class);
        cM.put("Player", Player.class);
        cM.put("Checkpoint", Checkpoint.class);
        cM.put("Wall", Wall.class);
        cM.put("DeathBall", DeathBall.class);
        cM.put("EndPoint", EndPoint.class);
        cM.put("Path", Path.class);
        cM.put("PathPoint", PathPoint.class);
        cM.put("Resizer", Resizer.class);
        return cM;
    }

    public static Map<String, Class<? extends GameObject>> getClassMap(){
        return classMap;
    }

    @Override
    public void reset() {
        super.reset();
        EditorWorld world = new EditorWorld();
        world.setViewport(this.viewport);
        viewport.setWorld(world);
    }

    public void saveTo(String fileName){
        this.viewport.getWorld().saveTo(fileName);
    }

    public void loadFrom(String fileName){
        EditorWorld loadedWorld = new EditorWorld(fileName, classMap);
        loadedWorld.setViewport(this.viewport);
        this.viewport.setWorld(loadedWorld);
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(!ele.inRange(e)){ continue; }
            String name = ele.getName();
            if(name.contains("Back Button")){
                this.nextScreen = ScreenName.MENU;
            } else if(name.contains("Pause")){
                this.nextScreen = ScreenName.PAUSE;
            }
        }
    }

}
