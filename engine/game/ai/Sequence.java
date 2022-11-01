package engine.game.ai;

public class Sequence extends Composite {

    public Sequence(){}

    public BTStatus update(float seconds){
        int index = this.children.indexOf(this.prevRunningChild);
        for(int i = Math.max(index, 0); i < this.children.size(); i++){
            BTNode child = this.children.get(i);
            BTStatus status = child.update(seconds);
            if(status == BTStatus.RUNNING) {
                this.prevRunningChild = child;
                return status;
            } else if(status == BTStatus.FAIL){
                this.prevRunningChild = null;
                return status;
            }
        }
        return BTStatus.SUCCESS;
    }

}
