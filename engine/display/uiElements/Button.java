package engine.display.uiElements;

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
        super("Button", c, p, s);
        this.text = new Text("");
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, Vec2d p, Vec2d s, Text t) {
        super(t+" Button", c, p, s);
        this.text = t;
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, double y, Vec2d s) {
        super("Button", c, new Vec2d(270,0), s);
        double x = (this.windowSize.x/2) - (s.x/2);
        this.setPosition(new Vec2d(x,y));
        this.centered = true;
        this.text = new Text("");
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(Color c, double y, Vec2d s, Text t) {
        super(t.getText()+" Button" , c, new Vec2d(270,0), s);
        double x = (this.windowSize.x/2) - (s.x/2);
        this.setSize(new Vec2d(x,y));
        this.centered = true;
        this.text = t;
        this.children.add(this.text);
        this.text.setParent(this);
    }

    public Button(String name, Color c, Vec2d p, Vec2d s) {
        super(name, c, p, s);
        this.text = new Text("");
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
        this.text.setText(t);
        this.text.setColor(this.color);
        this.text.setPosition(new Vec2d(x,y));
        this.text.setFont(f);
        this.name = t+" Button";
    }

    public Text getText(){
        return this.text;
    }

    public String getTextString(){
        return this.text.getText();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
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
