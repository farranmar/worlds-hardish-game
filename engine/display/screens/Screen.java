package engine.display.screens;

import engine.display.Viewport;
import engine.display.uiElements.UIElement;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Screen {

    protected ArrayList<UIElement> uiElements;
    protected static final Vec2d DEFAULT_SCREEN_SIZE = new Vec2d(960, 540);
    protected Vec2d windowSize = new Vec2d(960,540);
    protected Vec2d screenSize = new Vec2d(960,540);
    protected boolean visible;
    protected boolean active; // whether clicks, button presses, etc are registered
    protected ScreenName name;
    protected ScreenName nextScreen = null;
    protected Color[] palette = new Color[0];
    protected Viewport viewport;

    public Screen(ScreenName name){
        this.uiElements = new ArrayList<>();
        this.visible = false;
        this.active = false;
        this.name = name;
    }

    public Screen(ScreenName name, ArrayList<UIElement> uiElements) {
        this.uiElements = uiElements;
        this.visible = false;
        this.active = false;
        this.name = name;
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

    public ScreenName getName(){
        return this.name;
    }

    // this function resets the nextScreen value, so save the return value and be careful about not calling it twice in a row
    public ScreenName getNextScreen(){
        ScreenName ret = this.nextScreen;
        if(ret != null) {
            System.out.println("getting next screen, this is "+this.getName()+" and nextscreen is "+this.nextScreen);
            this.nextScreen = null;
        }
        return ret;
    }

    public void setNextScreen(ScreenName name){
        this.nextScreen = name;
    }

    public void add(UIElement uiElement){
        this.uiElements.add(uiElement);
        uiElement.setScreen(this);
    }

    public void remove(UIElement uiElement){
        this.uiElements.remove(uiElement);
    }

    public void setColor(Color[] palette){
        this.palette = palette;
    }

    public void reset() {
        for(UIElement ele : uiElements){
            ele.reset();
        }
    }

    public void onTick(long nanosSincePreviousTick){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onTick(nanosSincePreviousTick);
        }
    }

    public void onLateTick(){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onLateTick();
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

    public void onKeyPressed(KeyEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onKeyPressed(e);
        }
    }

    public void onKeyReleased(KeyEvent e){
        for(UIElement ele : uiElements){
            ele.onKeyReleased(e);
        }
    }

    public void onMouseClicked(MouseEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onMouseClicked(e);
        }
    }

    public void onMouseMoved(MouseEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onMouseMoved(e);
        }
    }

    public void onMousePressed(MouseEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onMousePressed(e);
        }
    }

    public void onMouseReleased(MouseEvent e){
        for(UIElement ele : uiElements){
            ele.onMouseReleased(e);
        }
    }

    public void onMouseDragged(MouseEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onMouseDragged(e);
        }
    }

    public void onMouseWheelMoved(ScrollEvent e){
        if(!active){ return; }
        for(UIElement ele : uiElements){
            ele.onMouseWheelMoved(e);
        }
    }

    public void onShutdown(){}

}
