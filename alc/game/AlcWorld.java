package alc.game;

import alc.game.units.Unit;
import alc.game.units.UnitMenu;
import engine.game.systems.CollisionSystem;
import engine.game.systems.GraphicsSystem;
import engine.game.systems.InputSystem;
import engine.game.systems.StaticSystem;
import engine.game.world.GameWorld;
import engine.game.objects.BackgroundObject;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class AlcWorld extends GameWorld {

    public AlcWorld() {
        super("Alchemy");
        this.size = new Vec2d(1920,1080);
        this.addSystems(); // should add systems BEFORE objects
        this.addObjects();
    }

    private void addObjects(){
        BackgroundObject background = new BackgroundObject(this, Color.rgb(69,69,69), this.size);
        this.add(background);
        background.setDrawPriority(0);
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(54,444), new Vec2d(1328, 318));
        unitMenu.setDrawPriority(1);
        Unit air = new Unit(this, Unit.Type.AIR);
        unitMenu.add(air);
        this.add(air);
        Unit earth = new Unit(this, Unit.Type.EARTH);
        unitMenu.add(earth);
        this.add(earth);
        Unit fire = new Unit(this, Unit.Type.FIRE);
        unitMenu.add(fire);
        this.add(fire);
        Unit water = new Unit(this, Unit.Type.WATER);
        unitMenu.add(water);
        this.add(water);
        this.add(unitMenu);
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new StaticSystem());
        this.addSystem(new CollisionSystem());
    }

}
