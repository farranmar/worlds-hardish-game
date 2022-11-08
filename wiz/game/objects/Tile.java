package wiz.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.GameComponent;
import engine.game.components.SpriteComponent;
import engine.game.components.ComponentTag;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import wiz.game.helpers.TileType;
import wiz.resources.Resource;

public class Tile extends GameObject {

    private TileType type;
    private Vec2i mapPosition;
    private static final String passableSpriteFile = "purple_floor.png";
    private static final String impassableSpriteFile = "impassable_brick.png";
    private static final String exitSpriteFile = "stairs.png";

    public Tile(GameWorld world, TileType type, Vec2d size, Vec2d position){
        super(world, size, position);
        this.worldDraw = false;
        this.type = type;
        CollideComponent collideComponent = new CollideComponent(new AAB(this.getSize(), this.getPosition()));
        collideComponent.setStatic(true);
        collideComponent.setCollidable(type == TileType.IMPASSABLE);
        this.add(collideComponent);
        if(this.type == TileType.PASSABLE || this.type == TileType.SPAWN){
            this.add(new SpriteComponent(new Resource().get(passableSpriteFile)));
        } else if(this.type == TileType.EXIT){
            this.add(new SpriteComponent(new Resource().get(exitSpriteFile)));
        } else {
            this.add(new SpriteComponent(new Resource().get(impassableSpriteFile)));
        }
        this.gameWorld.add(this);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Tile) || this.mapPosition == null){ return false; }
        return this.type == ((Tile)obj).getType() && this.mapPosition.equals(((Tile)obj).getMapPosition());
    }

    public void setType(TileType type){
        this.type = type;
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.SPRITE){
                if(this.type == TileType.PASSABLE || this.type == TileType.SPAWN){ ((SpriteComponent)component).setImage(new Resource().get(passableSpriteFile)); }
                else if(this.type == TileType.EXIT){ ((SpriteComponent)component).setImage(new Resource().get(exitSpriteFile)); }
                else { ((SpriteComponent)component).setImage(new Resource().get(impassableSpriteFile)); }
            }
            if(type != TileType.IMPASSABLE && component.getTag() == ComponentTag.COLLIDE){
                ((CollideComponent)component).setCollidable(false);
            }
        }
    }

    public Vec2i getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(Vec2i mPos){
        this.mapPosition = mPos;
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
    }

    public TileType getType(){
        return this.type;
    }

}
