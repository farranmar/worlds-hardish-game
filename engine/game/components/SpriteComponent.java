package engine.game.components;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class SpriteComponent extends DrawComponent {

    // @TODO: convert Image to be String of filename
    private Image image;
    private Vec2d subImagePos = null;
    private Vec2d subImageSize = null;
    private int animationIndex = 0;
    private boolean animated = false;
    private int frames = 1;
    private int animationSpeed = 1;
    private int ticksSinceUpdate = 0;

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

    public SpriteComponent(){
        super();
        this.tag = ComponentTag.SPRITE;
    }

    public SpriteComponent(Image image){
        super();
        this.image = image;
        this.tag = ComponentTag.SPRITE;
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

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void setTicksSinceUpdate(int ticksSinceUpdate) {
        this.ticksSinceUpdate = ticksSinceUpdate;
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        Element subImagePos = this.subImagePos.toXml(doc, "SubImagePosition");
        ele.appendChild(subImagePos);
        Element subImageSize = this.subImageSize.toXml(doc, "SubImageSize");
        ele.appendChild(subImageSize);
        ele.setAttribute("animationIndex", this.animationIndex+"");
        ele.setAttribute("animated", this.animated+"");
        ele.setAttribute("frames", this.frames+"");
        ele.setAttribute("animationSpeed", this.animationSpeed+"");
        ele.setAttribute("ticksSinceUpdate", this.ticksSinceUpdate+"");
        return ele;
    }

    public static SpriteComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("SPRITE")){ return null; }
        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.setConstants(ele);
        Vec2d subImagePos = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "SubImagePosition").item(0)));
        Vec2d subImageSize = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "SubImageSize").item(0)));
        spriteComponent.setSubImage(subImageSize, subImagePos);
        spriteComponent.setAnimationIndex(Integer.parseInt(ele.getAttribute("animationIndex")));
        spriteComponent.setAnimated(Boolean.parseBoolean(ele.getAttribute("animated")));
        spriteComponent.setFrames(Integer.parseInt(ele.getAttribute("frames")));
        spriteComponent.setAnimationSpeed(Integer.parseInt(ele.getAttribute("animationSpeed")));
        spriteComponent.setTicksSinceUpdate(Integer.parseInt(ele.getAttribute("ticksSinceUpdate")));
        return spriteComponent;
    }

}
