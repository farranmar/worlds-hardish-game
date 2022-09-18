package tic.screens;

import engine.Screen;
import engine.support.Vec2d;
import engine.uiElements.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tic.uiElements.Board;

import java.util.ArrayList;

public class GameScreen extends Screen {

    private Color primaryColor;
    private Color secondaryColor;
    private Vec2d screenSize;
    private Timer timer;
    private Board board;
    private Text botText;
    private int turn; // 0 = O's turn, 1 = X's turn

    public GameScreen(){
        super();
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.turn = 1;
        this.addStandardElements();
    }

    public GameScreen(ArrayList<UIElement> uiElements) {
        super(uiElements);
        this.primaryColor = Color.rgb(0,0,0);
        this.secondaryColor = Color.rgb(255,255,255);
        this.screenSize = new Vec2d(960,540);
        this.turn = 1;
        this.addStandardElements();
    }

    public GameScreen(Color primaryColor, Color secondaryColor, Vec2d size){
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.screenSize = size;
        this.turn = 1;
        this.addStandardElements();
    }

    public void onTick(long nanosSincePreviousTick){
        super.onTick(nanosSincePreviousTick);
        if(this.board.getResult() != GameOverScreen.Result.UNFINISHED){
            this.primaryColor = Color.rgb(91, 62, 82);
            this.secondaryColor = Color.rgb(61, 64, 82);
            this.inactivate();
            // show game over screen
        } else {
            if(this.timer.done() || board.hasNewSymbol()){
                 // switch turns if timer runs out
                this.nextTurn();
            }
        }
    }

    public void nextTurn(){
        this.turn = -1 * (this.turn - 1);
        this.board.setTurn(this.turn);
        String player = this.turn == 1 ? "X" : "O";
        this.botText.setText("now playing: "+player);
        this.timer.restart();
    }

    private void addStandardElements(){
        Board board = new Board(primaryColor, new Vec2d(270,50), new Vec2d(420));
        BackButton backButton = new BackButton(primaryColor, new Vec2d(30,30), new Vec2d(50,30));
        Text nowPlaying = new Text("now playing: X", primaryColor, 510, new Font("Courier", 24));
        Timer timer = new Timer(primaryColor, new Vec2d(790, 120), new Vec2d(25,300), true, 15);
        this.timer = timer;
        this.board = board;
        this.botText = nowPlaying;
        this.add(board);
        this.add(backButton);
        this.add(nowPlaying);
        this.add(timer);
    }

    public void setColors(Color primary, Color secondary){
        this.primaryColor = primary;
        this.secondaryColor = secondary;
    }

    public Timer getTimer(){
        return this.timer;
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
    }
}
