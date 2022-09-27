package engine.game.components;

import javafx.scene.paint.Color;

public class Drawable extends GameComponent {

    private boolean fixed = false; // whether this is fixed in the viewport
    protected Color color;

    public Drawable(){
        this.tag = Tag.DRAWABLE;
        this.drawable = true;
        this.color = Color.rgb(0,0,0);
    }

    public Drawable(Color color){
        this.tag = Tag.DRAWABLE;
        this.drawable = true;
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color newColor){
        this.color = newColor;
    }

    public void fix(){
        this.fixed = true;
    }

    public void unfix(){
        this.fixed = false;
    }

}
