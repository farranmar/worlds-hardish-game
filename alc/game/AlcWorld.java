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

    private static HashMap<Unit.Type, String> fileNames;

    public AlcWorld() {
        super("Alchemy");
        this.size = new Vec2d(1920,1080);
        this.addSystems(); // should add systems BEFORE objects
        this.addObjects();
        this.constructFileNames();
    }

    private void addObjects(){
        BackgroundObject background = new BackgroundObject(this, Color.rgb(69,69,69), this.size);
        this.add(background);
        background.setDrawPriority(0);
        UnitMenu unitMenu = new UnitMenu(this, Color.rgb(47,47,47), new Vec2d(54,444), new Vec2d(1328, 318));
        unitMenu.setDrawPriority(1);
        Unit air = new Unit(this, "air_ph.jpg");
        unitMenu.add(air);
        this.add(air);
        Unit earth = new Unit(this, "dirt_ph.png");
        unitMenu.add(earth);
        this.add(earth);
        Unit fire = new Unit(this, "fire_ph.jpg");
        unitMenu.add(fire);
        this.add(fire);
        Unit water = new Unit(this, "water_ph.png");
        unitMenu.add(water);
        this.add(water);
        Unit sprout = new Unit(this, "sprout_ref.png");
        Unit sun = new Unit(this, "sun_ph.jpg");
        Unit tree = new Unit(this, "tree_ph.png");
        Unit deadTree = new Unit(this, "deadtree_ph.png");
        Unit mushroom = new Unit(this, "mushroom_ph.jpg");
//        unitMenu.add(sprout);
//        this.add(sprout);
        this.add(unitMenu);
    }

    private void addSystems(){
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new StaticSystem());
        this.addSystem(new CollisionSystem());
    }

    private void constructFileNames(){
        if(fileNames != null){ return; }
        fileNames = new HashMap<>();
        fileNames.put(Unit.Type.AIR, "air_ph.jpg");
        fileNames.put(Unit.Type.EARTH, "dirt_ph.png");
        fileNames.put(Unit.Type.FIRE, "fire_ph.jpg");
        fileNames.put(Unit.Type.WATER, "water_ph.png");
        fileNames.put(Unit.Type.SPROUT, "sprout_ref.png");
        fileNames.put(Unit.Type.SUN, "sun_ph.jpg");
        fileNames.put(Unit.Type.TREE, "tree_ph.png");
        fileNames.put(Unit.Type.DEAD_TREE, "deadtree_ph.png");
        fileNames.put(Unit.Type.MUSHROOM, "mushroom_ph.jpg");
    }

}
