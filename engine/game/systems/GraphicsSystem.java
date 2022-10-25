package engine.game.systems;

import javafx.scene.canvas.GraphicsContext;

import java.util.TreeSet;

public class GraphicsSystem extends GameSystem {

    public GraphicsSystem(){
        this.drawable = true;
    }

    public void onDraw(GraphicsContext g, TreeSet draworder){
        super.onDraw(g, draworder);
    }

}
