package engine.game.ai;

public class Selector extends Composite {

    public Selector(){}

    public BTNode.BTStatus update(float seconds){
        for(BTNode child : this.children){
            BTStatus status = child.update(seconds);
            if(status == BTStatus.RUNNING){
                if(this.children.indexOf(child) < this.children.indexOf(prevRunningChild)){
                    prevRunningChild.reset();
                }
                this.prevRunningChild = child;
                return status;
            } else if(status == BTStatus.SUCCESS){
                this.prevRunningChild = null;
                return status;
            }
        }
        return BTStatus.FAIL;
    }

}
