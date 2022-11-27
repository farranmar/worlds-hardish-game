package engine.game.systems;

import engine.game.objects.GameObject;
import engine.support.Vec2d;

import java.util.HashMap;

public class CollisionSystem extends GameSystem {

    public CollisionSystem(){
        this.collidable = true;
        this.tickable = true;
    }

    public void onTick(long nanosSinceLastTick){
        HashMap<GameObject, GameObject> collisions = new HashMap<>();
        for(GameObject obj1 : gameObjects){
            if(obj1.isStatic() || !obj1.isCollidable()){ continue; }
            for(GameObject obj2 : gameObjects){
                if(obj1 == obj2 || !obj2.isCollidable()){ continue; }
                Vec2d mtv = obj1.collidesWith(obj2);
                if(mtv != null){
                    if(collisions.get(obj1) == obj2 || collisions.get(obj2) == obj1){
                        continue;
                    }
                    collisions.put(obj1, obj2);
                    obj1.onCollide(obj2, mtv);
                    obj2.onCollide(obj1, mtv.smult(-1));
                }
            }
        }
    }

}
