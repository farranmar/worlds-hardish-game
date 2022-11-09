package nin.game.objects;

import engine.game.components.CollideComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.Ray;
import engine.game.world.GameWorld;
import engine.support.Vec2d;

public class GravityRay extends GameObject {

    Ray ray;
    Block block;

    public GravityRay(GameWorld world, Block block, Vec2d size, Vec2d position) {
        super(world, size, position);
        this.ray = new Ray(size, position);
        this.block = block;
        this.add(new CollideComponent(this.ray));
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        super.setPosition(newPosition);
        this.ray.setPosition(newPosition);
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        if(obj instanceof Player){ return; }
        super.onCollide(obj, mtv);
        if(obj instanceof Platform){
            block.setGravity(false);
        }
    }
}
