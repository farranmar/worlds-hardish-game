package engine.display.uiElements;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BackButton extends Button {

    public BackButton(Color c, Vec2d p, Vec2d s) {
        super("Back Button", c, p, s);
        this.outline = false;
    }

    public void onDraw(GraphicsContext g){
        if(this.highlighted) { g.setFill(Color.rgb(255,255,255)); }
        else { g.setFill(this.color); }
        double leftX = this.position.x;
        double midX = this.position.x + 0.866*this.size.y;
        double rightX = this.position.x + this.size.x;
        double topY = this.position.y;
        double midTopY = this.position.y + this.size.y/4;
        double midY = this.position.y + (this.size.y/2);
        double midBotY = this.position.y + 3*this.size.y/4;
        double botY = this.position.y + this.size.y;
        g.fillPolygon(new double[]{leftX,midX,midX,rightX,rightX,midX,midX},
                      new double[]{midY,topY,midTopY,midTopY,midBotY,midBotY,botY}, 7);
        this.text.onDraw(g);
        super.onDraw(g);
    }

}
