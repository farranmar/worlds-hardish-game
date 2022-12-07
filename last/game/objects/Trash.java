package last.game.objects;

import engine.display.uiElements.Text;
import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;
import static last.screens.EditorScreen.classMap;

public class Trash extends GameObject {

    Color color;
    Text label;

    public Trash(GameWorld gameWorld, Vec2d size, Vec2d position){
        super(gameWorld);
        this.color = Color.rgb(0, 0, 0);
        this.transformComponent = new TransformComponent(size, position);
        this.addComponents();
        this.constructLabel();
    }

    public Trash(GameWorld gameWorld, Vec2d size, Vec2d position, Color color){
        super(gameWorld);
        this.color = color;
        this.transformComponent = new TransformComponent(size, position);
        this.addComponents();
        this.constructLabel();
    }

    private void addComponents(){
        this.components.add(new DrawComponent());
        this.components.add(new CollideComponent(new AAB(this.getSize(), this.getPosition())));
    }

    private void constructLabel(){
        Vec2d position = this.getPosition();
        Vec2d size = this.getSize();
        Font font = new Font("Courier", 14);
        FontMetrics metrics = new FontMetrics("trash", font);
        this.label = new Text("trash", Color.rgb(255, 255, 255),
                new Vec2d(position.x + size.x/2 - metrics.width/2, position.y + size.y - 10), font);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void onDraw(GraphicsContext g){
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
        g.setStroke(Color.rgb(255, 255, 255));
        g.strokeRect(pos.x, pos.y, size.x, size.y);
        label.onDraw(g);
        super.onDraw(g);
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        obj.delete();
    }

    public Element toXml(Document doc){
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Trash");
        Element colorEle = colorToXml(doc, this.color);
        ele.appendChild(colorEle);
        return ele;
    }

    public Trash(Element ele, GameWorld world){
        super(ele, world, classMap);
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Trash")){ return; }
        Color color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        this.setColor(color);
        this.constructLabel();
    }

}
