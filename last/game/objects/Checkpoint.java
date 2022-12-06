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

    public Checkpoint clone(){
        return new Checkpoint(this.gameWorld, this.getSize(), this.getPosition(), this.color, this.savePos);
    }

    public Vec2d getSaveSpot() {
        return this.savePos;
    }

    public void centerSaveSpot(Vec2d playerSize){
        double x = this.getPosition().x + this.getSize().x/2 - playerSize.x/2;
        double y = this.getPosition().y + this.getSize().y/2 - playerSize.y/2;
        this.savePos = new Vec2d(x, y);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Checkpoint");
        ele.appendChild(this.savePos.toXml(doc, "SaveSpot"));
        return ele;
    }

    public Checkpoint(Element ele, GameWorld world){
        super(ele, world);
        this.savePos = Vec2d.fromXml(getTopElementsByTagName(ele, "SaveSpot").get(0));
    }


}
