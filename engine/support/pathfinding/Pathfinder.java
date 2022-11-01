package engine.support.pathfinding;

import engine.support.graph.Graph;
import engine.support.graph.Node;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class Pathfinder<T> {

    private Graph<T> graph;

    public Pathfinder(Graph<T> g){
        this.graph = g;
    }

    private Stack<Node<T>> reconstructPath(HashMap<Node<T>,Node<T>> cameFrom, Node<T> current){
        Stack<Node<T>> totalPath = new Stack<>();
        totalPath.push(current);
        while(cameFrom.containsKey(current)){
            current = cameFrom.get(current);
            totalPath.push(current);
        }
        return totalPath;
    }

    public Stack<Node<T>> findPath(Node<T> start, Node<T> dest, Heuristic<T> h, Heuristic<T> c){
        start = graph.get(start);
        dest = graph.get(dest);

        HashMap<Node<T>, Node<T>> cameFrom = new HashMap<>();

        Double infinity = Double.MAX_VALUE;

        HashMap<Node<T>, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        HashMap<Node<T>, Double> fScore = new HashMap<>();
        fScore.put(start, h.heur(start, dest));

        PathfinderHelper<T> comp = new PathfinderHelper<>(fScore, infinity);
        PriorityQueue<Node<T>> openSet = new PriorityQueue<>(11, comp);
        openSet.add(start);

        while(openSet.size() > 0){
            Node<T> current = openSet.poll();
            if(current == dest){
                return this.reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            for(Node<T> neighbor : current.getNeighbors()){
                double tentativeGScore = gScore.getOrDefault(current, infinity) + c.heur(current, neighbor);
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
