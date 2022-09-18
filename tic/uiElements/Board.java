package tic.uiElements;

import engine.support.Vec2d;
import engine.uiElements.UIElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import tic.screens.GameOverScreen;

import java.util.ArrayList;

public class Board extends UIElement {

    private Space[] spaces = new Space[9];
    private int turn = 1;
    private boolean hasNewSymbol;

    public Board(Color c, Vec2d p, Vec2d s) {
        super("Board", c, p, s);
        for(int i = 0; i < 9; i++){
            double x = this.position.x + (i%3) * this.size.x/3.0;
            double y;
            if(i < 3) { y = this.position.y; }
            else if(i < 6) { y = this.position.y + this.size.y/3.0; }
            else { y = this.position.y + 2*this.size.y/3.0; }
            Space space = new Space(Color.rgb(128,147,241), Color.rgb(75, 81,112), new Vec2d(x,y), new Vec2d(this.size.x/3));
            spaces[i] = space;
            space.setParent(this);
            this.children.add(space);
        }
    }

    public void setTurn(int t) {
        this.turn = t;
    }

    public void setColor(Color[] colors){
        this.color = colors[0];
        if(colors.length > 2) {
            for(Space space : spaces) { space.setColor(new Color[]{colors[1], colors[2]}); }
        } else if(colors.length > 1){
            for(Space space : spaces) { space.setColor(new Color[]{colors[1], colors[1]}); }
        }
    }

    public void reset(){
        for(Space space : spaces){
            space.setSymbol(Space.Symbol.EMPTY);
            space.setColor(new Color[]{Color.rgb(128,147,241), Color.rgb(75, 81,112)});
            space.unlock();
        }
    }

    public boolean hasNewSymbol(){
        boolean ret = this.hasNewSymbol;
        this.hasNewSymbol = false;
        return ret;
    }

    public GameOverScreen.Result getResult(){
        boolean boardFull = true;
        for(int i = 0; i < 3; i++){
            if(spaces[i*3].getSymbol() == spaces[i*3+1].getSymbol() && spaces[i*3].getSymbol() == spaces[i*3+2].getSymbol()){
                if(spaces[i*3].getSymbol() == Space.Symbol.SOLID_X) {
                    return GameOverScreen.Result.X;
                } else if(spaces[i*3].getSymbol() == Space.Symbol.SOLID_O) {
                    return GameOverScreen.Result.O;
                }
            }
            if(spaces[i].getSymbol() == spaces[i+3].getSymbol() && spaces[i].getSymbol() == spaces[i+6].getSymbol()){
                if(spaces[i].getSymbol() == Space.Symbol.SOLID_X) {
                    return GameOverScreen.Result.X;
                } else if(spaces[i].getSymbol() == Space.Symbol.SOLID_O) {
                    return GameOverScreen.Result.O;
                }
            }
            if((spaces[i*3].getSymbol() == Space.Symbol.EMPTY || spaces[i*3].getSymbol() == Space.Symbol.GHOST_X || spaces[i*3].getSymbol() == Space.Symbol.GHOST_O) ||
                    (spaces[i*3+1].getSymbol() == Space.Symbol.EMPTY || spaces[i*3+1].getSymbol() == Space.Symbol.GHOST_X || spaces[i*3+1].getSymbol() == Space.Symbol.GHOST_O) ||
                    (spaces[i*3+2].getSymbol() == Space.Symbol.EMPTY || spaces[i*3+2].getSymbol() == Space.Symbol.GHOST_X || spaces[i*3+2].getSymbol() == Space.Symbol.GHOST_O)) {
                boardFull = false;
            }
        }
        if(spaces[0].getSymbol() == spaces[4].getSymbol() && spaces[0].getSymbol() == spaces[8].getSymbol()){
            if(spaces[0].getSymbol() == Space.Symbol.SOLID_X) {
                return GameOverScreen.Result.X;
            } else if(spaces[0].getSymbol() == Space.Symbol.SOLID_O) {
                return GameOverScreen.Result.O;
            }
        }
        if(spaces[6].getSymbol() == spaces[4].getSymbol() && spaces[6].getSymbol() == spaces[2].getSymbol()){
            if(spaces[6].getSymbol() == Space.Symbol.SOLID_X) {
                return GameOverScreen.Result.X;
            } else if(spaces[6].getSymbol() == Space.Symbol.SOLID_O) {
                return GameOverScreen.Result.O;
            }
        }
        if(boardFull) {
            return GameOverScreen.Result.DRAW; }
        return GameOverScreen.Result.UNFINISHED;
    }

    public void onDraw(GraphicsContext g){
        double vSpace = this.size.y/3.0;
        double hSpace = this.size.x/3.0;
        g.setStroke(this.color);
        g.setLineWidth(6);
        g.strokeLine(this.position.x + hSpace, this.position.y, this.position.x+hSpace, this.position.y+this.size.y);
        g.strokeLine(this.position.x + 2*hSpace, this.position.y, this.position.x+2*hSpace, this.position.y+this.size.y);
        g.strokeLine(this.position.x, this.position.y+vSpace, this.position.x+this.size.x, this.position.y+vSpace);
        g.strokeLine(this.position.x, this.position.y+2*vSpace, this.position.x+this.size.x, this.position.y+2*vSpace);
        super.onDraw(g);
    }

    public void onMouseClicked(MouseEvent e){
        for(Space space : spaces){
            if(space.onMouseClicked(e, this.turn)) { this.hasNewSymbol = true; }
        }
    }

    public void onMouseMoved(MouseEvent e){
        for(Space space : spaces){
            space.onMouseMoved(e, this.turn);
        }
    }

}
