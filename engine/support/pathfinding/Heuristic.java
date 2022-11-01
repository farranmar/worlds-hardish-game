package engine.support.pathfinding;

import engine.support.graph.Node;

public interface Heuristic<T> {

    double heur(Node<T> start, Node<T> dest);

}
