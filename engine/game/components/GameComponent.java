package engine.game.components;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameComponent {

    protected ComponentTag tag;
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean keyInput = false;
    protected boolean mouseInput = false;

    public GameComponent(ComponentTag tag){
        this.tag = tag;
    }

    public ComponentTag getTag(){ return this.tag; }

    public void setTickable(boolean tickable) {
        this.tickable = tickable;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public boolean isKeyInput() {
        return keyInput;
    }

    public void setKeyInput(boolean keyInput) {
        this.keyInput = keyInput;
    }

    public boolean isMouseInput() {
        return mouseInput;
    }

    public void setMouseInput(boolean mouseInput) {
        this.mouseInput = mouseInput;
    }

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

    protected void setConstants(Element ele){
        this.setTickable(Boolean.parseBoolean(ele.getAttribute("tickable")));
        this.setDrawable(Boolean.parseBoolean(ele.getAttribute("drawable")));
        this.setKeyInput(Boolean.parseBoolean(ele.getAttribute("keyInput")));
        this.setMouseInput(Boolean.parseBoolean(ele.getAttribute("mouseInput")));
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        return ele;
    }

    public static GameComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        GameComponent gameComponent = new GameComponent(ComponentTag.valueOf(ele.getAttribute("tag")));
        gameComponent.setTickable(Boolean.parseBoolean(ele.getAttribute("tickable")));
        gameComponent.setDrawable(Boolean.parseBoolean(ele.getAttribute("drawable")));
        gameComponent.setKeyInput(Boolean.parseBoolean(ele.getAttribute("keyInput")));
        gameComponent.setMouseInput(Boolean.parseBoolean(ele.getAttribute("mouseInput")));
        return gameComponent;
    }

}
