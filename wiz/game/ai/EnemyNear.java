package wiz.game.ai;

import engine.game.ai.Condition;
import engine.support.graph.Node;
import engine.support.pathfinding.Heuristic;
import engine.support.pathfinding.Pathfinder;
import wiz.game.objects.Enemy;
import wiz.game.objects.Map;
import wiz.game.objects.Tile;

import java.util.Stack;

public class EnemyNear extends Condition {

    private Blackboard blackboard;
    private Map map;
    private Enemy enemy;

    public EnemyNear(Blackboard blackboard, Map map, Enemy enemy){
        this.blackboard = blackboard;
        this.map = map;
        this.enemy = enemy;
    }

    @Override
    public BTStatus update(float seconds) {
        Pathfinder<Tile> pathfinder = new Pathfinder<>(this.map.getGraph());
        Tile enemyTile = map.getTile(enemy.getMapPosition());
        Tile playerTile = map.getTile(map.getPlayer().getMapPosition());
        Heuristic<Tile> heur = (node1, node2) -> {
            if(node1 == null || node2 == null){ return Double.MAX_VALUE; }
            Tile tile1 = node1.getValue();
            Tile tile2 = node2.getValue();
            int xDif = tile1.getMapPosition().x - tile2.getMapPosition().y;
            int yDif = tile1.getMapPosition().y - tile2.getMapPosition().y;
            return Math.abs(xDif) + Math.abs(yDif);
        };
        Heuristic<Tile> cost = (node1, node2) -> {
            if(node1 == null || node2 == null){ return Double.MAX_VALUE; }
            Tile tile2 = node2.getValue();
            if(tile2.getMapPosition().x == playerTile.getMapPosition().x || tile2.getMapPosition().y == playerTile.getMapPosition().y){
                return 5d;
            }
            return 1d;
        };
        Stack<Node<Tile>> path = pathfinder.findPath(new Node<>(enemyTile), new Node<>(playerTile), heur, cost);
        if(path.size() <= 10){
            this.blackboard.setToPath(path);
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAIL;
    }
}
