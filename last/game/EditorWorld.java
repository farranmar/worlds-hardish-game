package last.game;

import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.game.objects.UnitMenu;

public class EditorWorld extends GameWorld {

    public EditorWorld(){
        this(false);
    }

    public EditorWorld(boolean empty){
        super("Last");
        this.size = new Vec2d(1920, 1080);
        if(!empty){
            this.addSystems();
            this.createObjects();
        }
    }

    private void createObjects() {
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(54,384), new Vec2d(1328, 318));
        unitMenu.setDrawPriority(1);
        this.add(unitMenu);
    }

    private void addSystems() {
    }
}
