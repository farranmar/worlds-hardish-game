package engine.game.world;

import engine.game.objects.GameObject;

import java.util.Comparator;

public class DrawOrderHelper implements Comparator<GameObject> {

    @Override
    public int compare(GameObject o1, GameObject o2) {
        if(o1.getDrawPriority() < o2.getDrawPriority()){
            return -1;
        } else if(o1.getDrawPriority() > o2.getDrawPriority()){
            return 1;
        }
        return 0;
    }

}
