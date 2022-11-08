package engine.game.components;

import javafx.scene.paint.Color;

public class DrawComponent extends GameComponent {

    protected Color color;

    public DrawComponent(){
        super(ComponentTag.DRAW);
        this.drawable = true;
        this.color = Color.rgb(0,0,0);
    }

    public DrawComponent(Color color){
        super(ComponentTag.DRAW);
        this.drawable = true;
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color newColor){
        this.color = newColor;
    }

}
