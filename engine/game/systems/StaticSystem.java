package engine.game.systems;

import engine.game.components.ComponentTag;
import engine.game.objects.GameObject;

public class StaticSystem extends GameSystem {

    public StaticSystem(){

    }

    public boolean attemptAdd(GameObject obj){
        if(this.tickable && obj.get(ComponentTag.TICK) != null){
            return false;
        } else if(this.drawable && obj.get(ComponentTag.DRAW) != null){
            return false;
        } else if(this.takesInput && obj.get(ComponentTag.DRAG) != null){
            return false;
        }
        this.gameObjects.add(obj);
        return true;
    }

}
