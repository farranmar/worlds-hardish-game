package engine.game.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HasSprite extends Drawable {

    private Image image;

    public HasSprite(){
        super();
        this.tag = Tag.HAS_SPRITE;
    }

    public HasSprite(Image image){
        super();
        this.image = image;
        this.tag = Tag.HAS_SPRITE;
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void onDraw(GraphicsContext g, TransformComponent tC){
        g.drawImage(image, tC.getPosition().x, tC.getPosition().y, tC.getSize().x, tC.getSize().y);
        super.onDraw(g);
    }

}
