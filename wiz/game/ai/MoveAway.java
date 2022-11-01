package wiz.game.ai;

import engine.game.ai.Action;
import engine.game.ai.BTNode;
import engine.support.graph.Node;
import wiz.game.objects.Enemy;
import wiz.game.objects.Map;
import wiz.game.objects.Tile;

import java.util.Stack;

public class MoveAway extends Action {

    private Map map;
    private Enemy enemy;
    private Stack<Node<Tile>> path;
    private Blackboard blackboard;

    public MoveAway(Blackboard blackboard, Map map, Enemy enemy){
        this.blackboard = blackboard;
        this.map = map;
        this.enemy = enemy;
    }

    public void setStatus(BTNode.BTStatus leafStatus) {
        this.status = leafStatus;
    }

    public BTStatus update(float seconds){
        this.path = this.blackboard.getAwayPath();
        this.run();
        return BTStatus.SUCCESS;
    }

    public void run(){
        this.enemy.setPath(this.path);
    }
}
