package last.game.objects;

import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import last.game.LastWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Checkpoint extends Wall {

    private Vec2d savePos;

    public Checkpoint(GameWorld world, Vec2d size, Vec2d position, Vec2d savePos){
        super(world, size, position, Color.rgb(139, 232, 203));
        this.savePos = savePos;
    }

    public Checkpoint(GameWorld world, Vec2d size, Vec2d position, Color color, Vec2d savePos){
        super(world, size, position, color);
        this.savePos = savePos;
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        return;
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Checkpoint");
        ele.appendChild(this.savePos.toXml(doc, "SaveSpot"));
        return ele;
    }

    public Vec2d getSaveSpot() {
        return this.savePos;
    }

    public Checkpoint(Element ele, GameWorld world){
        super(ele, world);
        this.savePos = Vec2d.fromXml(getTopElementsByTagName(ele, "SaveSpot").get(0));
    }


}
