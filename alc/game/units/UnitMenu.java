package alc.game.units;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class UnitMenu extends GameObject {

    private ArrayList<GameObject> units = new ArrayList<>();
    private Vec2d unitSize;
    private double padding = 12;

    public UnitMenu(GameWorld gameWorld, Color color, Vec2d size, Vec2d position){
        super(gameWorld);
        DrawComponent drawComponent = new DrawComponent(color);
        this.components.add(drawComponent);
        this.components.add(new ClickComponent());
        this.transformComponent = new TransformComponent(size, position);
        this.drawPriority = 1;
        this.unitSize = new Vec2d(size.x - padding*2);
    }

    public void add(GameObject unit){
        unit.setSize(unitSize);
        unit.setPosition(this.getNextUnitPosition());
        unit.setParent(this);
        this.addChild(unit);
        this.gameWorld.addToAdditionQueue(unit);
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
}
