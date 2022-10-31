package engine.display.uiElements;

import engine.display.screens.Screen;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class UIElement {

    protected Color color;
    protected Vec2d position;
    protected Vec2d size;
    protected UIElement parent;
    protected Screen screen;
    protected ArrayList<UIElement> children = new ArrayList<>();
    protected Vec2d windowSize = new Vec2d(960,540);
    protected Vec2d screenSize = new Vec2d(960,540);
    protected boolean centered = false;
    protected String name;

    public UIElement(String name){
        this.name = name;
        this.color = Color.rgb(0,0,0);
        this.position = new Vec2d(0);
        this.size = new Vec2d(0);
    }

    public UIElement(String name, Color c, Vec2d p, Vec2d s) {
        this.color = c;
        this.position = p;
        this.size = s;
        this.name = name;
    }

    public UIElement(String name, Color c, double y, Vec2d s) {
        this.color = c;
        double x = (this.windowSize.x/2) - (s.x/2);
        this.position = new Vec2d(x,y);
        this.size = s;
        this.centered = true;
        this.name = name;
    }

    public void setStageSizes(Vec2d windowSize, Vec2d screenSize){
        this.windowSize = windowSize;
        this.screenSize = screenSize;
    }

    public void addChild(UIElement child){
        children.add(child);
    }

    public void setParent(UIElement parent){
        this.parent = parent;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        for(UIElement child : children){
            child.setScreen(screen);
        }
    }

    public Vec2d getPosition(){
        return this.position;
    }

    public Vec2d getSize(){
        return this.size;
    }

    public void setPosition(Vec2d p){
        this.position = p;
    }

    public void setSize(Vec2d s){
        this.size = s;
    }

    public void setColor(Color c){
        this.color = c;
    }

    public void setColor(Color[] colors){
        this.color = colors[0];
    }

    public void reset(){

    }

    public Vec2d getWindowSize(){
        return this.windowSize;
    }

    public Vec2d getScreenSize(){
        return this.screenSize;
    }

    public String getName(){
        return this.name;
    }

    public void onTick(long nanosSincePreviousTick){
        if(children != null){
            for(UIElement child : children){
                child.onTick(nanosSincePreviousTick);
            }
        }
    }

    public void onLateTick(){
        if(children != null){
            for(UIElement child : children){
                child.onLateTick();
            }
        }
    }

    public void onDraw(GraphicsContext g){
        for(UIElement child : children){
            child.onDraw(g);
        }
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        double width = newScreenSize.x * (this.size.x / this.screenSize.x);
        double height = newScreenSize.y * (this.size.y / this.screenSize.y);
        this.size = new Vec2d(width, height);

        double x;
        if(centered){
            x = (this.windowSize.x/2) - (this.size.x/2);
        } else {
            double leftSpacing = (this.windowSize.x - this.screenSize.x) / 2;
            double newLeftSpacing = (newWindowSize.x - newScreenSize.x) / 2;
            x = (this.position.x - leftSpacing) / this.screenSize.x * newScreenSize.x + newLeftSpacing;
        }
        double topSpacing = (this.windowSize.y - this.screenSize.y) / 2;
        double newTopSpacing = (newWindowSize.y - newScreenSize.y) / 2;
        double y = (this.position.y - topSpacing) / this.screenSize.y * newScreenSize.y + newTopSpacing;
        this.position = new Vec2d(x,y);

        this.windowSize = newWindowSize;
        this.screenSize = newScreenSize;

        for(UIElement child : children){
            child.onResize(newWindowSize, newScreenSize);
        }
    }

    public boolean inRange(MouseEvent e){
        boolean inX = (e.getX() >= this.position.x) && (e.getX() <= this.position.x+this.size.x);
        boolean inY = (e.getY() >= this.position.y) && (e.getY() <= this.position.y+this.size.y);
        return inX && inY;
    }

    public void onKeyPressed(KeyEvent e){
        for(UIElement child : children){
            child.onKeyPressed(e);
        }
    }

    public void onKeyReleased(KeyEvent e){
        for(UIElement child : children){
            child.onKeyReleased(e);
        }
    }

    public void onMouseClicked(MouseEvent e){
        for(UIElement child : children){
            child.onMouseClicked(e);
        }
    }

    public void onMouseMoved(MouseEvent e){
        for(UIElement child : children){
            child.onMouseMoved(e);
        }
    }

    public void onMousePressed(MouseEvent e){
        for(UIElement child : children){
            child.onMousePressed(e);
        }
    }

    public void onMouseReleased(MouseEvent e){
        for(UIElement child : children){
            child.onMouseReleased(e);
        }
    }

    public void onMouseDragged(MouseEvent e){
        for(UIElement child : children){
            child.onMouseDragged(e);
        }
    }

    public void onMouseWheelMoved(ScrollEvent e){
        for(UIElement child : children){
            child.onMouseWheelMoved(e);
        }
    }

}
