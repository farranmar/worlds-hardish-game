package engine.display.uiElements;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text extends UIElement {

    private Font font;
    private String text;
    private boolean highlighted = false;

    public Text(String text) {
        super(text, Color.BLACK, new Vec2d(0,0), new Vec2d(0,0));
        this.font = new Font("Calibri", 0);
        this.text = text;
    }

    public Text(String text, Color c, Vec2d p, Font font) {
        super(text, c, p, new Vec2d(new FontMetrics(text, font).width, new FontMetrics(text, font).height));
        this.font = font;
        this.text = text;
    }

    public Text(String text, Color c, double y, Font font){
        super(text, c, new Vec2d(480- new FontMetrics(text,font).width/2,y), new Vec2d(new FontMetrics(text, font).width, new FontMetrics(text, font).height));
        FontMetrics metrics = new FontMetrics(text, font);
        double x = (this.windowSize.x/2) - (metrics.width/2);
        this.setPosition(new Vec2d(x,y));
        this.centered = true;
        this.text = text;
        this.font = font;
    }

    public void setHighlighted(boolean h){
        this.highlighted = h;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String newText){
        this.text = newText;
        this.onResize(this.windowSize, this.screenSize);
        this.center();
    }

    public void appendToText(String newText){
        this.text = this.text + newText;
        this.center();
    }

    public void insertText(int index, String newText){
        this.text = this.text.substring(0, index) + newText + this.text.substring(index);
        this.center();
    }

    public void removeCharAt(int index){
        this.text = this.text.substring(0, index) + this.text.substring(index+1);
        this.center();
    }

    public void setFont(Font f){
        this.font = f;
    }

    public Font getFont(){
        return this.font;
    }

    public void center(){
        if(!this.centered){ return; }
        FontMetrics metrics = new FontMetrics(this.text, this.font);
        this.setPosition(new Vec2d(this.screenSize.x/2 - metrics.width/2, this.getPosition().y));
    }

    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
    }

    public void onDraw(GraphicsContext g) {
        if(this.highlighted) { g.setFill(Color.rgb(255,255,255)); }
        else { g.setFill(this.color); }
        System.out.println("drawing text "+this.text+", color ="+this.color);
        g.setFont(this.font);
        g.fillText(this.text, this.position.x, this.position.y);
        super.onDraw(g);
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        double newFontSize = newScreenSize.y * (this.font.getSize() / this.screenSize.y);
        Font newFont = new Font(this.font.getName(), newFontSize);
        FontMetrics metrics = new FontMetrics(this.text, newFont);
        this.font = newFont;
        this.size = new Vec2d(metrics.width, metrics.height);
        if(centered){
            double x = (newWindowSize.x/2) - (metrics.width/2);
            double topSpacing = (this.windowSize.y - this.screenSize.y) / 2;
            double newTopSpacing = (newWindowSize.y - newScreenSize.y) / 2;
            double y = (this.position.y - topSpacing) / this.screenSize.y * newScreenSize.y + newTopSpacing;
            this.position = new Vec2d(x,y);
            this.windowSize = newWindowSize;
            this.screenSize = newScreenSize;
        } else {
            super.onResize(newWindowSize, newScreenSize);
        }

    }
}
