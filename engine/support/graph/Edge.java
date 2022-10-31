package engine.support.graph;

public class Edge<T> {

    private Node<T> left;
    private Node<T> right;

    public Edge(Node<T> left, Node<T> right){
        this.left = left;
        this.right = right;
    }

    public Node<T> getLeft(){ return this.left; }

    public Node<T> getRight(){ return this.right; }

    public Node<T> getNot(Node<T> node){
        if(left == node){
            return right;
        } else if(right == node){
            return left;
        } else {
            return null;
        }
    }

    public boolean equals(Edge edge){
        if(this.left == edge.getLeft() && this.right == edge.getRight()){ return true; }
        if(this.left == edge.getRight() && this.right == edge.getLeft()){ return true; }
        return false;
    }

}
