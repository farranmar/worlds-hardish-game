package engine.game.ai;

import java.util.ArrayList;

public abstract class BTNode {

    protected ArrayList<BTNode> children = new ArrayList<>();
    protected BTStatus status;

    public enum BTStatus{
        SUCCESS,
        FAIL,
        RUNNING;
    }

    public abstract BTStatus update(float seconds);

    public void setStatus(BTStatus status){
        this.status = status;
    }

    public void addChild(BTNode child){
        this.children.add(child);
    }

    public void reset(){
        status = null;
        for(BTNode child : children){
            child.reset();
        }
    }

}
