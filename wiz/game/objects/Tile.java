package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.GameComponent;
import engine.game.components.HasSprite;
import engine.game.components.Tag;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import wiz.game.helpers.TileType;
import wiz.resources.Resource;

public class Tile extends GameObject {

    private TileType type;
    private static final String passableSpriteFile = "purple_floor.png";
    private static final String impassableSpriteFile = "impassable_brick.png";
    private static final String exitSpriteFile = "stairs.png";

    public Tile(GameWorld world, TileType type, Vec2d size, Vec2d position){
        super(world, size, position);
        this.type = type;
        this.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        if(this.type == TileType.PASSABLE || this.type == TileType.SPAWN){ this.add(new HasSprite(new Resource().get(passableSpriteFile))); }
        else if(this.type == TileType.EXIT){ this.add(new HasSprite(new Resource().get(exitSpriteFile))); }
        else { this.add(new HasSprite(new Resource().get(impassableSpriteFile))); }
    }

    public void setType(TileType type){
        this.type = type;
        for(GameComponent component : this.components){
            if(component.getTag() == Tag.HAS_SPRITE){
                if(this.type == TileType.PASSABLE || this.type == TileType.SPAWN){ ((HasSprite)component).setImage(new Resource().get(passableSpriteFile)); }
                else if(this.type == TileType.EXIT){ ((HasSprite)component).setImage(new Resource().get(exitSpriteFile)); }
                else { ((HasSprite)component).setImage(new Resource().get(impassableSpriteFile)); }
            }
        }
    }

    public TileType getType(){
        return this.type;
    }

}
