package engine.game.components;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameComponent {

    protected Tag tag = null;
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean keyInput = false;
    protected boolean mouseInput = false;

    public Tag getTag(){ return this.tag; }

    public boolean isTickable(){ return this.tickable; }

    public boolean isDrawable(){ return this.drawable; }

    public boolean takesKeyInput(){ return this.keyInput; }

    public boolean takesMouseInput(){ return this.mouseInput; }

    public void onTick(long nanosSincePreviousTick){ return; }

    public void onLateTick(){ return; }

    public void onDraw(GraphicsContext g){ return; }

    public void onKeyTyped(KeyEvent e){ return; }

    public void onKeyPressed(KeyEvent e){ return; }

    public void onKeyReleased(KeyEvent e){ return; }

    public void onMouseClicked(MouseEvent e){ return; }

    public void onMousePressed(double x, double y){
        return; }

    public void onMouseReleased(double x, double y){ return; }

    public void onMouseDragged(double x, double y, GameObject obj){ return; }

    public void onMouseMoved(MouseEvent e){ return; }

    public void onMouseWheelMoved(MouseEvent e){ return; }

    public void onFocusChanged(boolean newVal){ return; }

    public void onResize(Vec2d newSize){ return; }

}
