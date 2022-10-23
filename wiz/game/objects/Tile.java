package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.GameComponent;
import engine.game.components.HasSprite;
import engine.game.components.Tag;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import wiz.resources.Resource;

public class Tile extends GameObject {

    private boolean passable;
    private boolean spawn = false;
    private boolean exit = false;
    private String passableSpriteFile = "purple_floor.jpg";
    private String impassableSpriteFile = "impassable_brick.png";

    public Tile(GameWorld world, boolean passability, Vec2d size, Vec2d position){
        super(world, size, position);
        this.passable = passability;
        this.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        if(this.passable){ this.add(new HasSprite(new Resource().get(passableSpriteFile))); }
        else { this.add(new HasSprite(new Resource().get(impassableSpriteFile))); }
    }

    public void setPassable(boolean passable){
        this.passable = passable;
        for(GameComponent component : this.components){
            if(component.getTag() == Tag.HAS_SPRITE){
                if(passable){ ((HasSprite)component).setImage(new Resource().get(passableSpriteFile)); }
                else { ((HasSprite)component).setImage(new Resource().get(impassableSpriteFile)); }
            }
        }
    }

    public void setSpawn(boolean spawn){
        this.spawn = spawn;
    }

    public void setExit(boolean exit){
        this.exit = exit;
    }

}
