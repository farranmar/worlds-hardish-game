package alc.game.units;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends GameObject {

    public Square(Color color){
        this.drawPriority = 2;
        this.components.add(new Drawable(color));
        this.components.add(new TransformComponent(new Vec2d(0), new Vec2d(0)));
        this.components.add(new Clickable());
    }

    public Square(Color color, Vec2d size, Vec2d position){
        this.drawPriority = 2;
        this.components.add(new Drawable(color));
        this.components.add(new TransformComponent(size, position));
        this.components.add(new Clickable());
    }

    public Square(Square square){
        this(square.getColor(), square.getSize(), square.getPosition());
        this.drawPriority = 2;
    }

    public Color getColor(){
        for(GameComponent component : this.components){
            if(component.getTag() == Tag.DRAWABLE){
                return ((Drawable)component).getColor();
            }
        }
        return null;
    }

    public Square clone(){
        Square clone = new Square(this);
        clone.setSize(this.getSize());
        clone.setPosition(this.getPosition());
        clone.setDrawPriority(this.drawPriority + 1);
        return clone;
    }

    public void onDraw(GraphicsContext g){
        Color color = Color.rgb(0,0,0);
        for(GameComponent component : this.components){
            if(component.getTag() == Tag.DRAWABLE){
                color = ((Drawable)component).getColor();
            }
        }
        g.setStroke(color);
        g.setLineWidth(2);
        g.strokeRect(this.transformComponent.getPosition().x, this.transformComponent.getPosition().y, this.transformComponent.getSize().x, this.transformComponent.getSize().y);
        super.onDraw(g);
    }

}
