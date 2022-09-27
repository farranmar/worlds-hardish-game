package engine.game.systems;

import engine.game.components.Tag;
import engine.game.objects.GameObject;

public class StaticSystem extends GameSystem {

    public StaticSystem(){

    }

    public boolean attemptAdd(GameObject obj){
        if(this.tickable && obj.get(Tag.TICKABLE) != null){
            return false;
        } else if(this.drawable && obj.get(Tag.DRAWABLE) != null){
            return false;
        } else if(this.takesInput && obj.get(Tag.DRAGGABLE) != null){
            return false;
        }
        this.gameObjects.add(obj);
        return true;
    }

}
