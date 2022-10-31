package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HasSprite extends Drawable {

    private Image image;
    private Vec2d subImagePos = null;
    private Vec2d subImageSize = null;
    private int animationIndex = 0;
    private boolean animated = false;
    private int frames = 1;
    private int animationSpeed = 1;
    private int ticksSinceUpdate = 0;

    private boolean proj = false;

    public static class SubImage {
        Vec2d position;
        Vec2d size;
        public SubImage(Vec2d size, Vec2d position){
            this.position = position;
            this.size = size;
        }

        public Vec2d getSize(){ return this.size; }
        public Vec2d getPosition(){ return this.position; }
    }

    public HasSprite(){
        super();
        this.tag = Tag.HAS_SPRITE;
    }

    public HasSprite(Image image){
        super();
        this.image = image;
        this.tag = Tag.HAS_SPRITE;
    }

    public HasSprite(Image image, boolean t){
        super();
        this.image = image;
        this.tag = Tag.HAS_SPRITE;
        this.proj = true;
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void setSubImage(Vec2d size, Vec2d pos){
        this.subImagePos = pos;
        this.subImageSize = size;
    }

    public void animate(Vec2d size, double y, double padding, int frames, int speed){
        this.subImageSize = size;
        this.subImagePos = new Vec2d(size.x*animationIndex + padding*animationIndex, y);
        this.animated = true;
        this.frames = frames;
        this.animationSpeed = speed;
    }

    public void stopAnimation(){
        this.animated = false;
        this.animationIndex = 0;
    }

    public void onDraw(GraphicsContext g, TransformComponent tC){
        this.ticksSinceUpdate++;
        if(this.proj){
            System.out.println("prjectile");
        }
        if(subImagePos == null || subImageSize == null){
            g.drawImage(image, tC.getPosition().x, tC.getPosition().y, tC.getSize().x, tC.getSize().y);
        } else {
            g.drawImage(image, subImagePos.x, subImagePos.y, subImageSize.x, subImageSize.y, tC.getPosition().x, tC.getPosition().y, tC.getSize().x, tC.getSize().y);
        }
        if(animated && ticksSinceUpdate >= animationSpeed){
            this.animationIndex = (this.animationIndex+1) % frames;
            this.ticksSinceUpdate = 0;
        }
        super.onDraw(g);
    }

}
