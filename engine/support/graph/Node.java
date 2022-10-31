package engine.support.graph;

import java.util.ArrayList;

public class Node<T> {

    ArrayList<Edge> edges = new ArrayList<>();
    T value;

    public Node(T v){
        this.value = v;
    }

    public T getValue(){
        return this.value;
    }

    public ArrayList<Node<T>> getNeighbors(){
        ArrayList<Node<T>> ret = new ArrayList<>();
        for(Edge<T> edge : edges){
            Node<T> neighbor = edge.getNot(this);
            if(neighbor != null){
                ret.add(neighbor);
            }
        }
        return ret;
    }

    public void addNeighbor(Node<T> node){
        if(this.getNeighbors().contains(node)){ return; }
        Edge newEdge = new Edge(this, node);
        this.addEdge(newEdge);
        node.addEdge(newEdge);
    }

    public void addEdge(Edge edge){
        if(edges.contains(edge)){ return; }
        this.edges.add(edge);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Node)){ return false; }
        return this.value.equals(((Node)obj).getValue());
    }

}
