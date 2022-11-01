package wiz.game.ai;

import engine.game.ai.BTNode;
import engine.game.ai.Selector;
import engine.game.ai.Sequence;
import wiz.game.objects.Enemy;
import wiz.game.objects.Map;

public class BTHandler {

    private Blackboard blackboard;
    private Map map;
    private Enemy enemy;
    private BTNode root;

    public BTHandler(Map map, Enemy enemy){
        this.blackboard = new Blackboard();
        this.map = map;
        this.enemy = enemy;
        this.construct();
    }

    private void construct(){
        InRange inRange = new InRange(blackboard, map, enemy);
        CanExitRange canExitRange = new CanExitRange(blackboard, map, enemy);
        MoveAway moveAway = new MoveAway(blackboard, map, enemy);
        Sequence defense = new Sequence();
        defense.addChild(inRange);
        defense.addChild(canExitRange);
        defense.addChild(moveAway);

        EnemyNear enemyNear = new EnemyNear(blackboard, map, enemy);
        MoveTo moveTo = new MoveTo(blackboard, map, enemy);
        Sequence offense = new Sequence();
        offense.addChild(enemyNear);
        offense.addChild(moveTo);

        Selector root = new Selector();
        root.addChild(defense);
        root.addChild(offense);
        this.root = root;
    }

    public void update(float seconds){
        root.update(seconds);
    }

}
