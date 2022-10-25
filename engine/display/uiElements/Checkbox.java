package engine.display.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Checkbox extends UIElement {

    private boolean checked;
    private boolean highlighted;

    public Checkbox(String name, Color c, Vec2d p, Vec2d s) {
        super(name, c, p, s);
    }

    public void setChecked(boolean c){
        this.checked = c;
    }

    public boolean isChecked(){
        return this.checked;
    }

    public void onDraw(GraphicsContext g){
        g.setLineWidth(3);
        if(highlighted){
            g.setStroke(Color.rgb(255, 255, 255));
            g.setFill(Color.rgb(255,255,255));
        }
        else {
            g.setStroke(this.color);
            g.setFill(this.color);
        }
        if(checked){
            g.fillRoundRect(this.position.x, this.position.y, this.size.x, this.size.y, 10, 10);
            g.setStroke(Color.rgb(69,69,69));
            Vec2d bottom = new Vec2d(this.position.x + this.size.x/3, this.position.y + this.size.y - 5);
            Vec2d left = new Vec2d(this.position.x+5, this.position.y + 2*this.size.y/3);
            Vec2d right = new Vec2d(this.position.x+this.size.x-5, this.position.y+5);
            g.strokeLine(left.x, left.y, bottom.x, bottom.y);
            g.strokeLine(bottom.x, bottom.y, right.x, right.y);
        } else {
            g.strokeRoundRect(this.position.x, this.position.y, this.size.x, this.size.y, 10, 10);
        }
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        if(this.inRange(e)){
            checked = !checked;
        }
    }

    public void onMouseMoved(MouseEvent e){
        super.onMouseMoved(e);
        if(!this.inRange(e)) {
            this.highlighted = false;
            return;
        }
        this.highlighted = true;
    }

}
