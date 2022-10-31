package engine.support.pathfinding;

import engine.game.objects.GameObject;
import engine.support.graph.Node;

import java.util.Comparator;
import java.util.HashMap;

public class PathfinderHelper implements Comparator<Node<?>> {

    private HashMap<Node<?>, Double> fScore;
    private Double defaultValue;

    public PathfinderHelper(HashMap<Node<?>, Double> fScore, Double defaultValue){
        this.fScore = fScore;
        this.defaultValue = defaultValue;
    }

    @Override
    public int compare(Node<?> node1, Node<?> node2) {
        if(fScore.getOrDefault(node1, this.defaultValue) < fScore.getOrDefault(node2, this.defaultValue)){
            return -1;
        } else if(fScore.getOrDefault(node1, this.defaultValue) > fScore.getOrDefault(node2, this.defaultValue)){
            return 1;
        }
        return 0;
    }

}
