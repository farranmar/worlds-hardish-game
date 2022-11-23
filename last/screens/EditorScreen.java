package last.screens;

import engine.display.Viewport;
import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.BackButton;
import engine.display.uiElements.UIElement;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import last.game.EditorWorld;
import last.game.objects.Unit;
import last.game.objects.UnitMenu;
import nin.game.NinWorld;

import java.util.HashMap;

public class EditorScreen extends Screen {

    private EditorWorld editorWorld;
    public static HashMap<String, Class<? extends GameObject>> classMap = initializeClassMap();

    public EditorScreen(){
        super(ScreenName.GAME);
        Viewport viewport = new Viewport(new Vec2d(960,540), new Vec2d(480, 270), new Vec2d(960,540), new Vec2d(0,0));
        this.viewport = viewport;
        this.add(viewport);
        BackButton backButton = new BackButton(Color.rgb(117,162,129), new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
    }

    private static HashMap<String, Class<? extends GameObject>> initializeClassMap(){
        HashMap<String, Class<? extends GameObject>> cM = new HashMap<>();
        cM.put("Unit", Unit.class);
        cM.put("UnitMenu", UnitMenu.class);
        return cM;
    }

    public void activate(){
        super.activate();
        EditorWorld world = new EditorWorld();
        world.setViewport(this.viewport);
        this.editorWorld = world;
        viewport.setWorld(world);
    }

    public void onKeyPressed(KeyEvent e){
        super.onKeyPressed(e);
        if(e.getCode() == KeyCode.L){
            this.editorWorld = null;
            if(classMap == null){ this.initializeClassMap(); }
            EditorWorld newWorld = new EditorWorld("save-file.xml", classMap);
            newWorld.setViewport(this.viewport);
            this.editorWorld = newWorld;
            this.viewport.setWorld(this.editorWorld);
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
