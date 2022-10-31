package engine.support.pathfinding;

import engine.support.graph.Node;

public interface Heuristic<T> {

    double heur(T start, T dest);

}
