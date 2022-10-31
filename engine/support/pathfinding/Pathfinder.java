package engine.support.pathfinding;

import engine.support.graph.Graph;
import engine.support.graph.Node;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class Pathfinder {

    private Graph<?> graph;

    public Pathfinder(Graph g){
        this.graph = g;
    }

    private Stack<Node<?>> reconstructPath(HashMap<Node<?>,Node<?>> cameFrom, Node<?> current){
        Stack<Node<?>> totalPath = new Stack<>();
        totalPath.push(current);
        while(cameFrom.containsKey(current)){
            current = cameFrom.get(current);
            totalPath.push(current);
        }
        return totalPath;
    }

    public Stack<Node<?>> findPath(Node<?> start, Node<?> dest, Heuristic h){
        start = graph.get(start);
        dest = graph.get(dest);

        HashMap<Node<?>, Node<?>> cameFrom = new HashMap<>();

        Double infinity = Double.MAX_VALUE;

        HashMap<Node<?>, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        HashMap<Node<?>, Double> fScore = new HashMap<>();
        fScore.put(start, h.heur(start, dest));

        PathfinderHelper comp = new PathfinderHelper(fScore, infinity);
        PriorityQueue<Node<?>> openSet = new PriorityQueue<>(11, comp);
        openSet.add(start);

        while(openSet.size() > 0){
            Node<?> current = openSet.poll();
            if(current == dest){
                return this.reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            for(Node<?> neighbor : current.getNeighbors()){
                double tentativeGScore = gScore.getOrDefault(current, infinity) + 1;
                if(tentativeGScore < gScore.getOrDefault(neighbor, infinity)){
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + h.heur(neighbor, dest));
                    if(!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

}
