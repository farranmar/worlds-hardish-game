package engine.display.uiElements;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;

public class TextBox extends UIElement {

    private Text prompt;
    private Text entry;
    private int typingIndex = 0;
    private boolean cursorVisible = true;
    private int nanosTillSwitch = 500000000;
    private int maxChars;
    private ArrayList<String> illegalChars = new ArrayList<>(Arrays.asList("#", "%", "&", "{", "}", "/", "\\", "<", ">",
            "*", "?", "$", "'", "\"", ":", "@", "`", "=", "|", "\u0008", "\n", "\r"));

    public TextBox(String prompt, Color color, Vec2d position, Font font, int maxChars){
        super(prompt + "TextBox");
        this.color = color;
        this.position = position;
        this.prompt = new Text(prompt, color, position.plus(new Vec2d(0, -1 * font.getSize())), font);
        this.entry = new Text("", color, position, font);
        this.maxChars = maxChars;
    }

    public TextBox(String prompt, Color color, double y, Font font, int maxChars){
        super(prompt + "TextBox");
        this.color = color;
        double width = new FontMetrics(prompt, font).width;
        this.position = new Vec2d(this.screenSize.x/2 - width/2);
        this.prompt = new Text(prompt, color, y - 1 - font.getSize(), font);
        this.entry = new Text("", color, y, font);
        this.maxChars = maxChars;
    }

    public String getValue(){
        return this.entry.getText();
    }

    public void onKeyPressed(KeyEvent e){
        super.onKeyPressed(e);
        if(e.getCode() == KeyCode.LEFT){
            this.typingIndex = Math.max(0, this.typingIndex-1);
            this.cursorVisible = true;
            this.nanosTillSwitch = 500000000;
        } else if(e.getCode() == KeyCode.RIGHT){
            this.typingIndex = Math.min(this.entry.getText().length(), this.typingIndex+1);
            this.cursorVisible = true;
            this.nanosTillSwitch = 500000000;
        }
        if(e.getCode() == KeyCode.BACK_SPACE){
            if(typingIndex > 0){
                this.typingIndex--;
                this.entry.removeCharAt(this.typingIndex);
            }
        }
        if(e.getCode() == KeyCode.DELETE){
            if(this.typingIndex < this.entry.getText().length()){
                this.entry.removeCharAt(this.typingIndex);
            }
        }
    }

    public void onKeyTyped(KeyEvent e){
        super.onKeyTyped(e);
        if(this.illegalChars.contains(e.getCharacter()) || e.getCharacter().equals(KeyEvent.CHAR_UNDEFINED)
                || e.getCharacter().length() == 0 || this.entry.getText().length() >= maxChars){ return; }
        this.entry.insertText(this.typingIndex, e.getCharacter());
        this.typingIndex++;
    }

    public void onDraw(GraphicsContext g) {
        super.onDraw(g);
        this.prompt.onDraw(g);
        this.entry.onDraw(g);
        if(this.cursorVisible){
            g.setStroke(this.color);
            g.setLineWidth(2);
            int i = Math.min(this.typingIndex, this.entry.getText().length());
            FontMetrics metrics = new FontMetrics(this.entry.getText().substring(0, i), this.entry.getFont());
            double leftDist = metrics.width;
            Vec2d pos = this.entry.getPosition();
            Vec2d bot = new Vec2d(pos.x + leftDist + 1, pos.y + 3);
            Vec2d top = new Vec2d(pos.x + leftDist + 1, pos.y - metrics.height + 5);
            g.strokeLine(bot.x, bot.y, top.x, top.y);
        }
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        super.onResize(newWindowSize, newScreenSize);
        this.prompt.onResize(newWindowSize, newScreenSize);
        this.entry.onResize(newWindowSize, newScreenSize);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        this.nanosTillSwitch -= nanosSincePreviousTick;
        if(this.nanosTillSwitch <= 0){
            this.nanosTillSwitch = 500000000;
            this.cursorVisible = !this.cursorVisible;
        }
    }
}
