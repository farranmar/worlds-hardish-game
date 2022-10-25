package engine.game.systems;

import javafx.scene.canvas.GraphicsContext;

import java.util.TreeSet;

public class GraphicsSystem extends GameSystem {

    public GraphicsSystem(){
        this.drawable = true;
    }

    public void onDraw(GraphicsContext g, TreeSet draworder){
        System.out.println("onDraw in GraphicsSystem, objs are "+this.gameObjects+" and draworder is "+draworder);
        super.onDraw(g, draworder);
    }

}
