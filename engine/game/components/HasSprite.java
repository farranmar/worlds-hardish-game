package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HasSprite extends Drawable {

    private Image image;
    private Vec2d subImagePos = null;
    private Vec2d subImageSize = null;

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

    public void setSubImage(Vec2d size, Vec2d pos){
        this.subImagePos = pos;
        this.subImageSize = size;
    }

    public void onDraw(GraphicsContext g, TransformComponent tC){
        if(subImagePos == null || subImageSize == null){
            g.drawImage(image, tC.getPosition().x, tC.getPosition().y, tC.getSize().x, tC.getSize().y);
        } else {
            g.drawImage(image, subImagePos.x, subImagePos.y, subImageSize.x, subImageSize.y, tC.getPosition().x, tC.getPosition().y, tC.getSize().x, tC.getSize().y);
        }

        super.onDraw(g);
    }

}
