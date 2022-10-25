package engine.game.systems;

import engine.game.objects.GameObject;

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
                if(obj1.collidesWith(obj2)){
                    if(collisions.get(obj1) == obj2 || collisions.get(obj2) == obj1){
                        continue;
                    }
                    collisions.put(obj1, obj2);
                    obj1.onCollide(obj2);
                }
            }
        }
    }

}
