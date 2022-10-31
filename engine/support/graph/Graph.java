package engine.support.graph;

import java.util.ArrayList;

public class Graph<T> {

    ArrayList<Node<T>> nodes = new ArrayList<>();

    public Graph(){}

    public void addNode(Node<T> node){
        System.out.println("nodes size: "+nodes.size());
        if(nodes.size()>0){ System.out.println("nodes.get(nodes.size()-1)==node? "+(nodes.get(nodes.size()-1)==node)+", nodes.get(nodes.size()-1).equals(node)? "+(nodes.get(nodes.size()-1).equals(node))); }
        if(this.nodes.contains(node)){
            System.out.println("already added");
            return;
        }
        this.nodes.add(node);
        System.out.println("nodes size: "+nodes.size());
    }

    public void connect(Node<T> node1, Node<T> node2){
        if(this.get(node1) == null){ this.addNode(node1); }
        if(this.get(node2) == null){ this.addNode(node2); }
        node1 = this.get(node1);
        node2 = this.get(node2);
        node1.addNeighbor(node2);
    }

    public Node<T> get(Node<?> n){
        for(Node<T> node : nodes){
            if(n.equals(node)){ return node; }
        }
        return null;
    }

}
