package wiz.game.ai;

import engine.support.graph.Node;
import wiz.game.objects.Tile;

import java.util.Stack;

public class Blackboard {

    private Stack<Node<Tile>> awayPath;
    private Stack<Node<Tile>> toPath;

    public Blackboard(){}

    public Stack<Node<Tile>> getAwayPath() {
        return awayPath;
    }

    public void setAwayPath(Stack<Node<Tile>> awayPath) {
        this.awayPath = awayPath;
    }

    public Stack<Node<Tile>> getToPath() {
        return toPath;
    }

    public void setToPath(Stack<Node<Tile>> toPath) {
        this.toPath = toPath;
    }
}
