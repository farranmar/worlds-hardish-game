package wiz.game.objects;

import engine.game.components.Collidable;
import engine.game.components.HasSprite;
import engine.game.components.Tickable;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import wiz.resources.Resource;

public class Enemy extends GameObject {

    private String spriteFileName;
    private Map map;

    public Enemy(GameWorld gameWorld, Map map, Vec2d size, Vec2d position, String fileName){
        super(gameWorld, size, position);
        this.spriteFileName = fileName;
        this.map = map;
        this.addComponents();
        this.worldDraw = false;
    }

    private void addComponents(){
        this.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        this.add(new HasSprite(new Resource().get(this.spriteFileName)));
        this.add(new Tickable());
    }

    public void onCollide(GameObject obj){
        if(obj instanceof Projectile){
            this.map.removeEnemy(this);
        } else if(obj instanceof Player){
            ((Player)obj).die();
        }
    }

}
