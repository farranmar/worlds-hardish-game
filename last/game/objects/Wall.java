package last.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import last.screens.EditorScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Wall extends GameObject {

    protected Color color;
    protected boolean showResizer = false;

    public Wall(GameWorld world, Vec2d size, Vec2d position){
        super(world, size, position);
        this.color = Color.rgb(126, 162, 170);
        Resizer resizer = new Resizer(world, this, this.getPosition().plus(this.getSize()));
        this.children.add(resizer);
        this.addComponents();
    }

    public Wall(GameWorld world, Vec2d size, Vec2d position, Color color){
        super(world, size, position);
        this.color = color;
        Resizer resizer = new Resizer(world, this, this.getPosition().plus(this.getSize()));
        this.children.add(resizer);
        this.addComponents();
    }

    public Wall clone(){
        Wall clone = new Wall(this.gameWorld, this.getSize(), this.getPosition(), this.color);
        clone.setShowResizer(true);
        clone.setCollidable(this.isCollidable());
        return clone;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShowResizer(boolean show){
        this.showResizer = show;
        assert(this.children.get(0) instanceof Resizer);
        ((Resizer)this.children.get(0)).setVisible(show);
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        super.setPosition(newPosition);
        assert(this.children.get(0) instanceof Resizer);
        this.children.get(0).setPosition(newPosition.plus(this.getSize()));
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()), true, 1);
        this.add(collide);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
    }

    public void delete(){
        this.gameWorld.addToRemovalQueue(this);
        for(GameObject child : children){
            assert(child instanceof Resizer);
            ((Resizer)child).delete(true);
            for(GameObject ppChild : child.getChildren()){
                assert(ppChild instanceof PathPoint);
                ((PathPoint)ppChild).delete(true);
            }
        }
    }

    public void delete(boolean justThis){
        super.delete();
    }

    @Override
    public void onCollide(GameObject obj, Vec2d mtv) {
        super.onCollide(obj, mtv);
    }

    @Override
    public void onMousePressed(double x, double y) {
        boolean resizing = false;
        for(GameObject child : children){
            assert(child instanceof Resizer);
            for(GameObject ppChild : child.getChildren()){
                assert(ppChild instanceof PathPoint);
                if(ppChild.inRange(x, y)){
                    resizing = true;
                }
            }
        }
        if(resizing){
            for(GameObject child : children){
                child.onMousePressed(x, y);
            }
        } else {
            super.onMousePressed(x, y);
        }
    }

    public void onDraw(GraphicsContext g){
        if(!(this instanceof EndPoint)){
            g.setFill(this.color);
            Vec2d size = this.getSize();
            Vec2d pos = this.getPosition();
            g.fillRect(pos.x, pos.y, size.x, size.y);
        }
        super.onDraw(g);
    }

    @Override
    public Element toXml(Document doc) {
        Element ele = super.toXml(doc);
        ele.setAttribute("class", "Wall");
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);
        ele.setAttribute("showResizer", this.showResizer+"");
        return ele;
    }

    public Wall(Element ele, GameWorld world){
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        this.showResizer = Boolean.parseBoolean(ele.getAttribute("showResizer"));

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);

        Element childrenEle = getTopElementsByTagName(ele, "Children").get(0);
        this.setChildrenXml(childrenEle, EditorScreen.getClassMap());
    }

}
