package last.game.objects;

import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EndPoint extends Wall {

    private Color secondaryColor;
    private static final Vec2d patternSize = new Vec2d(20);

    public EndPoint(GameWorld world, Vec2d size, Vec2d position){
        this(world, size, position, Color.rgb(100, 224, 185), Color.rgb(164, 237, 214));
    }

    public EndPoint(GameWorld world, Vec2d size, Vec2d position, Color pColor, Color sColor){
        super(world, size, position, pColor);
        this.secondaryColor = sColor;
    }

    public EndPoint clone(){
        EndPoint clone = new EndPoint(this.gameWorld, this.getSize(), this.getPosition(), this.color, this.secondaryColor);
        clone.setShowResizer(true);
        clone.setCollidable(this.isCollidable());
        return clone;
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        if(!(obj instanceof Player)){ return; }
        this.gameWorld.setResult(GameWorld.Result.VICTORY);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        Vec2d size = this.getSize();
        Vec2d position = this.getPosition();
        int numVert = (int)Math.ceil(size.y / patternSize.y);
        Vec2d lilSize = new Vec2d(size.y / numVert);
        int numHor = (int)Math.ceil(size.x / lilSize.x);
        for(int r = 0; r < numVert; r++){
            Color even, odd;
            if(r % 2 == 0){
                even = this.color;
                odd = this.secondaryColor;
            } else {
                even = this.secondaryColor;
                odd = this.color;
            }
            for(int c = 0; c < numHor; c++){
                if(c % 2 == 0) { g.setFill(even); }
                else { g.setFill(odd); }
                Vec2d pos = new Vec2d(position.x + c*lilSize.x, position.y + r*lilSize.y);
                double width, height;
                if(c == numHor-1){ width = size.x - (lilSize.x * (numHor-1)); }
                else { width = lilSize.x+1; }
                if(r == numVert-1){ height = lilSize.y; }
                else { height = lilSize.y+1; }
                g.fillRect(pos.x, pos.y, width, height);
            }
        }
        for(GameObject child : children){
            child.onDraw(g);
        }
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "EndPoint");
        ele.appendChild(colorToXml(doc, this.secondaryColor, "SecondaryColor"));
        return ele;
    }

    public EndPoint(Element ele, GameWorld world){
        super(ele, world);
        this.secondaryColor = colorFromXml(GameWorld.getTopElementsByTagName(ele, "SecondaryColor").get(0));
    }

}
