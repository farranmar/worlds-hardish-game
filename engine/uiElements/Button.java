package engine.uiElements;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Button extends UIElement {

    protected Text text;
    protected boolean outline = true;
    protected boolean highlighted = false;

    public Button(Color c, Vec2d p, Vec2d s) {
        super(c, p, s);
        this.text = new Text("");
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, Vec2d p, Vec2d s, Text t) {
        super(c, p, s);
        this.text = t;
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, double y, Vec2d s) {
        super(c, new Vec2d(270,0), s);
        double x = (this.windowSize.x/2) - (s.x/2);
        this.setPosition(new Vec2d(x,y));
        this.centered = true;
        this.text = new Text("");
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, double y, Vec2d s, Text t) {
        super(c, new Vec2d(270,0), s);
        double x = (this.windowSize.x/2) - (s.x/2);
        this.setSize(new Vec2d(x,y));
        this.centered = true;
        this.text = t;
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public void makeOutlineVisible(){
        this.outline = true;
    }

    public void makeOutlineInvisible(){
        this.outline = false;
    }

    public void onTick(long nanosSincePreviousTick){
        super.onTick(nanosSincePreviousTick);
    }

    public void onDraw(GraphicsContext g){
        if(outline) {
            if(this.highlighted) {
                text.setHighlighted(true);
                g.setStroke(Color.rgb(255,255,255));
            }
            else {
                text.setHighlighted(false);
                g.setStroke(this.color);
            }
            g.setLineWidth(3);
            g.strokeRoundRect(this.position.x,this.position.y,this.size.x,this.size.y, 20, 20);
        }
        super.onDraw(g);
    }

    public void setText(String t){
        Font font = new Font("Monospace", this.size.y - 10);
        this.setText(t, font);
    }

    public void setText(String t, String fontName){
        Font font = new Font(fontName, this.size.y - 10);
        this.setText(t, font);
    }

    public void setText(String t, Font f){
        FontMetrics metrics = new FontMetrics(t, f);
        double x = this.position.x + (this.size.x/2) - (metrics.width/2);
        double y = this.position.y + metrics.height + (this.size.y-metrics.height)/2 - 4;
//        System.out.println("button size is " + this.size.x + ", " + this.size.y+" and position is "+this.position.x+", "+this.position.y);
//        System.out.println("text y value is "+y+" = "+this.position.y+" + "+metrics.height+" + ("+this.size.y+" - "+metrics.height+") / 2");
        Text newText = new Text(t, this.color, new Vec2d(x,y), f);
        this.text = newText;
    }

    public void onMouseMoved(MouseEvent e){
        super.onMouseMoved(e);
        if(!this.inRange(e)) {
            this.highlighted = false;
            return;
        }
        this.highlighted = true;
        text.onMouseMoved(e);
    }

}
