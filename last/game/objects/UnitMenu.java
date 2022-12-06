package last.game.objects;

import engine.display.uiElements.Text;
import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

import static engine.game.world.GameWorld.getTopElementsByTagName;
import static last.screens.EditorScreen.classMap;

public class UnitMenu extends GameObject {

    private Vec2d unitSize;
    private double padding = 32;
    private ArrayList<Text> labels = new ArrayList<>();

    public UnitMenu(GameWorld gameWorld, Color color, Vec2d size, Vec2d position){
        super(gameWorld);
        DrawComponent drawComponent = new DrawComponent(color);
        this.components.add(drawComponent);
        this.components.add(new ClickComponent());
        this.transformComponent = new TransformComponent(size, position);
        this.drawPriority = 1;
        this.unitSize = new Vec2d(size.x - padding*2);
    }

    public void add(GameObject block, String name){
        block.setSize(unitSize);
        Vec2d pos = this.getNextUnitPosition();
        block.setPosition(pos);
        block.setParent(this);
        this.addChild(block);
        Font font = new Font("Courier", 14);
        FontMetrics metrics = new FontMetrics(name, font);
        Text label = new Text(name, Color.WHITE, pos.plus(new Vec2d(unitSize.x/2 - metrics.width/2, unitSize.y+5+metrics.height)), font);
        this.labels.add(label);
        this.gameWorld.addToSystems(block);
    }

    private Vec2d getNextUnitPosition(){
        double maxY = this.getPosition().y - this.unitSize.y;
        for(GameObject child : children){
            double y = child.getPosition().y;
            maxY = Math.max(y, maxY);
        }
        return new Vec2d(this.getPosition().x + padding, maxY + this.unitSize.y + padding);
    }

    public void reset(){
        for(GameObject child : children){
            child.setChildren(new ArrayList<>());
        }
    }

    public void onDraw(GraphicsContext g){
        Color color = Color.rgb(0,0,0);
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.DRAW){
                color = ((DrawComponent)component).getColor();
            }
        }
        g.setFill(color);
        g.fillRect(this.transformComponent.getPosition().x, this.transformComponent.getPosition().y, this.transformComponent.getSize().x, this.transformComponent.getSize().y);

        g.setStroke(Color.rgb(255,255,255));
        g.setLineWidth(4);
        g.strokeRect(this.transformComponent.getPosition().x, this.transformComponent.getPosition().y, this.transformComponent.getSize().x, this.transformComponent.getSize().y);

        for(Text label : this.labels){
            label.onDraw(g);
        }

        super.onDraw(g);
    }

    @Override
    public void onMousePressed(double x, double y) {
        super.onMousePressed(x, y);
        for(GameObject child : children){
            if(child.inRange(x, y)){
                GameObject clone = child.clone();
                DragComponent dragComponent = new DragComponent(clone);
                clone.add(dragComponent);
                dragComponent.onMousePressed(x, y);
                this.gameWorld.addToAdditionQueue(clone);
            }
        }
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "UnitMenu");
        ele.setAttribute("padding", this.padding+"");
        Element unitSize = this.unitSize.toXml(doc, "UnitSize");
        ele.appendChild(unitSize);
        return ele;
    }

    public UnitMenu(Element ele, GameWorld world){
        super(ele, world, classMap);
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("UnitMenu")){ return; }
        this.unitSize = Vec2d.fromXml(getTopElementsByTagName(ele, "UnitSize").get(0));
        this.padding = Double.parseDouble(ele.getAttribute("padding"));
    }
}
