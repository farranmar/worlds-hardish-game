package engine.game.objects;

import engine.game.components.ComponentTag;
import engine.game.components.DrawComponent;
import engine.game.components.GameComponent;
import engine.game.components.TransformComponent;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BackgroundObject extends GameObject {

    public BackgroundObject(GameWorld gameWorld, Color color, Vec2d size){
        super(gameWorld);
        DrawComponent drawComponent = new DrawComponent(color);
        this.components.add(drawComponent);
        this.transformComponent = new TransformComponent(size, new Vec2d(0,0));
        this.drawPriority = -1000;
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
        super.onDraw(g);
    }

}
