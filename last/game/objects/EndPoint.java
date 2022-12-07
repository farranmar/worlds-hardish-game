package last.game.objects;

import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EndPoint extends Wall {

    public EndPoint(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position, Color.rgb(139, 232, 203));
    }

    public EndPoint(GameWorld world, Vec2d size, Vec2d position, Color color){
        super(world, size, position, color);
    }

    public EndPoint clone(){
        EndPoint clone = new EndPoint(this.gameWorld, this.getSize(), this.getPosition(), this.color);
        clone.setShowResizer(true);
        return clone;
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        this.gameWorld.setResult(GameWorld.Result.VICTORY);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "EndPoint");
        return ele;
    }

    public EndPoint(Element ele, GameWorld world){
        super(ele, world);
    }

}
