package alc.game;

import alc.game.units.Square;
import alc.game.units.UnitMenu;
import engine.game.world.GameWorld;
import engine.game.objects.BackgroundObject;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class AlcWorld extends GameWorld {

    public AlcWorld() {
        super("Alchemy");
        this.size = new Vec2d(1920,1080);
        this.addObjects();
    }

    private void addObjects(){
        BackgroundObject background = new BackgroundObject(Color.rgb(69,69,69), this.size);
        this.add(background);
        UnitMenu unitMenu = new UnitMenu(Color.rgb(47,47,47), new Vec2d(54,444), new Vec2d(1328, 318));
        Square square = new Square(Color.rgb(206,24,142));
        unitMenu.add(square);
        this.add(square);
        this.add(unitMenu);
    }

}
