package engine.game.systems;

import engine.game.objects.GameObject;

public class CollisionSystem extends GameSystem {

    public CollisionSystem(){
        this.collidable = true;
        this.tickable = true;
    }

    public void onTick(long nanosSinceLastTick){
        for(GameObject obj1 : gameObjects){
            for(GameObject obj2 : gameObjects){
                if(obj1 == obj2){ continue; }
                if(obj1.collidesWith(obj2)){
//                    obj1.onCollide(obj2);
                }
            }
        }
    }

}
