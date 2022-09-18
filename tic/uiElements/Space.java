package tic.uiElements;

import engine.support.Vec2d;
import engine.uiElements.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Space extends Button {

    private Symbol symbol;
    private Color solidColor;
    private Color ghostColor;
    private boolean locked = false;

    public enum Symbol {
        SOLID_X,
        GHOST_X,
        SOLID_O,
        GHOST_O,
        EMPTY
    }

    public Space(Color solid, Color ghost, Vec2d p, Vec2d s) {
        super(solid, p, s);
        this.symbol = Symbol.EMPTY;
        this.solidColor = solid;
        this.ghostColor = ghost;
        this.makeOutlineInvisible();
    }

    public Symbol getSymbol(){
        return this.symbol;
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    public void onTick(long nanosSincePreviousTick){
        super.onTick(nanosSincePreviousTick);
    }

    public void onDraw(GraphicsContext g){
        if(this.symbol != Symbol.EMPTY){
            g.setLineWidth(8);
            if(this.symbol == Symbol.GHOST_X || this.symbol == Symbol.GHOST_O){
                g.setStroke(this.ghostColor);
            } else if(this.symbol == Symbol.SOLID_X || this.symbol == Symbol.SOLID_O){
                g.setStroke(this.solidColor);
            }

            int sp = 20; // spacing
            if(this.symbol == Symbol.GHOST_X || this.symbol == Symbol.SOLID_X){
                g.strokeLine(this.position.x+sp,this.position.y+sp,this.position.x+this.size.x-sp,this.position.y+this.size.y-sp);
                g.strokeLine(this.position.x+this.size.x-sp,this.position.y+sp,this.position.x+sp,this.position.y+this.size.y-sp);
            } else if(this.symbol == Symbol.GHOST_O || this.symbol == Symbol.SOLID_O){
                g.strokeOval(this.position.x+sp,this.position.y+sp,this.size.x-2*sp,this.size.y-2*sp);
            }

        }
        super.onDraw(g);
    }

    // returns whether or not a new symbol was locked on the space
    public boolean onMouseClicked(MouseEvent e, int turn){
        super.onMouseClicked(e);
        if(!this.inRange(e)) { return false; }
        if(turn == 0 && !this.locked) {
            this.locked = true;
            this.symbol = Symbol.SOLID_O;
            return true;
        }
        else if(turn == 1 && !this.locked) {
            this.locked = true;
            this.symbol = Symbol.SOLID_X;
            return true;
        }
        return false;
    }

    public void onMouseMoved(MouseEvent e, int turn){
        super.onMouseMoved(e);
        if(!this.inRange(e)) {
            if(!locked) { this.symbol = Symbol.EMPTY; }
            return;
        }
        if(turn == 0 && !locked) { this.symbol = Symbol.GHOST_O; }
        else if(turn == 1 && !locked){
            this.symbol = Symbol.GHOST_X;
        }
    }

}
