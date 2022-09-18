package engine;

import engine.support.Vec2d;
import engine.uiElements.UIElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class Screen {

    protected ArrayList<UIElement> uiElements;
    protected Vec2d windowSize = new Vec2d(960,540);
    protected Vec2d screenSize = new Vec2d(960,540);
    protected boolean visible;
    protected boolean active; // whether clicks, button presses, etc are registered

    public Screen(){
        this.uiElements = new ArrayList<>();
        this.visible = false;
        this.active = false;
    }

    public Screen(ArrayList<UIElement> uiElements) {
        this.uiElements = uiElements;
        this.visible = false;
        this.active = false;
    }

    public void makeVisible(){
        this.visible = true;
    }

    public void makeInvisible(){
        this.visible = false;
    }

    public boolean isActive(){
        return this.active == true;
    }

    public void activate(){
        this.active = true;
    }

    public void inactivate(){
        this.active = false;
    }

    public void add(UIElement uiElement){
        this.uiElements.add(uiElement);
        uiElement.setScreen(this);
    }

    public void onTick(long nanosSincePreviousTick){
        for(UIElement ele : uiElements){
            ele.onTick(nanosSincePreviousTick);
        }
    }

    public void onDraw(GraphicsContext g){
        if(visible) {
            for (UIElement ele : uiElements) {
                ele.onDraw(g);
            }
        }
    }

    public void onResize(Vec2d newSize){
        this.windowSize = newSize;
        if(newSize.x/newSize.y == 16.0/9.0){
            this.screenSize = newSize;
        } else if(newSize.x/newSize.y < 16.0/9.0){
            double x = newSize.x;
            double y = newSize.x * (9.0/16.0);
            this.screenSize = new Vec2d(x,y);
        } else {
            double x = newSize.y * (16.0/9.0);
            double y = newSize.y;
            this.screenSize = new Vec2d(x,y);
        }

        for(UIElement ele : uiElements){
            ele.onResize(this.windowSize, this.screenSize);
        }
    }

    public void onMouseClicked(MouseEvent e){
        for(UIElement ele : uiElements){
            ele.onMouseClicked(e);
        }
    }

    public void onMouseMoved(MouseEvent e){
        for(UIElement ele : uiElements){
            ele.onMouseMoved(e);
        }
    }

}
