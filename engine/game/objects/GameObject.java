package engine.game.objects;

import engine.game.components.*;
import engine.game.objects.shapes.Shape;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import nin.game.objects.Block;
import nin.game.objects.Platform;
import nin.game.objects.Player;
import nin.game.objects.Projectile;

import java.util.ArrayList;

public class GameObject {

    private static int numObjs;
    protected ArrayList<GameComponent> components = new ArrayList<>();
    protected TransformComponent transformComponent;
    protected int drawPriority;
    protected GameObject parent = null;
    protected ArrayList<GameObject> children = new ArrayList<>();
    protected GameWorld gameWorld;
    protected boolean worldDraw = true; // whether drawing is handled by world or not
    protected boolean floating = false;

    public GameObject(GameWorld gameWorld){
        numObjs++;
        this.drawPriority = 10+numObjs;
        this.gameWorld = gameWorld;
        this.transformComponent = new TransformComponent(new Vec2d(0), new Vec2d(0));
    }

    public GameObject(GameWorld gameWorld, Vec2d size, Vec2d position){
        numObjs++;
        this.drawPriority = 10+numObjs;
        this.gameWorld = gameWorld;
        this.transformComponent = new TransformComponent(size, position);
    }

    public void add(GameComponent component){
        this.components.add(component);
    }

    public void remove(GameComponent component){
        this.components.remove(component);
    }

    public void remove(ComponentTag tagToRemove){
        components.removeIf(component -> component.getTag() == (tagToRemove));
    }

    public GameComponent get(ComponentTag tagToGet){
        for(GameComponent component : components){
            if(component.getTag() == tagToGet){
                return component;
            }
        }
        return null;
    }

    public void reset(){
        for(GameObject child : children){
            child.reset();
        }
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public boolean isFloating(){
        return this.floating;
    }

    public void setFloating(boolean f){
        this.floating = f;
    }

    public void setWorldDraw(boolean wd){
        this.worldDraw = wd;
    }

    public boolean getWorldDraw(){
        return this.worldDraw;
    }

    // does NOT set parent or children
    public GameObject clone(){
        GameObject clone = new GameObject(this.gameWorld);
        for(GameComponent component : components){
            clone.add(component);
        }
        clone.setSize(this.getSize());
        clone.setPosition(this.getPosition());
        clone.setDrawPriority(this.drawPriority);
//        this.gameWorld.addToAdditionQueue(clone);
        return clone;
    }

    public int getDrawPriority(){
        return this.drawPriority;
    }

    public void setDrawPriority(int dp){
        this.drawPriority = dp;
    }

    public TransformComponent getTransform(){
        return this.transformComponent;
    }

    public Vec2d getSize(){
        return this.transformComponent.getSize();
    }

    public void setSize(Vec2d newSize){
        this.transformComponent.setSize(newSize);
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                ((CollideComponent)component).setSize(newSize);
            }
        }
    }

    public Vec2d getPosition(){
        return this.transformComponent.getPosition();
    }

    public void setPosition(Vec2d newPosition){
        this.transformComponent.setPosition(newPosition);
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                ((CollideComponent)component).setPosition(newPosition);
            }
        }
    }

    public void translate(double dx, double dy){
        this.setPosition(new Vec2d(this.getPosition().x + dx, this.getPosition().y + dy));
    }

    public GameObject getParent(){
        return this.parent;
    }

    public void setParent(GameObject parent){
        this.parent = parent;
    }

    public void addChild(GameObject child){
        this.children.add(child);
        this.gameWorld.add(child);
    }

    public void setChildren(ArrayList<GameObject> children){
        this.children = children;
    }

    public ArrayList<GameObject> getChildren(){
        return this.children;
    }

    public void setCollidable(boolean collidable){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                ((CollideComponent)component).setCollidable(collidable);
            }
        }
    }

    public boolean isStatic(){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                return ((CollideComponent)component).isStatic();
            }
        }
        return false;
    }

    public void setSubImage(SpriteComponent.SubImage subImage){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.SPRITE){
                ((SpriteComponent)component).setSubImage(subImage.getSize(), subImage.getPosition());
            }
        }
    }

    public void animate(Vec2d size, double y, double padding, int frames, int speed){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.SPRITE){
                ((SpriteComponent)component).animate(size, y, padding, frames, speed);
            }
        }
    }

    public boolean isCollidable(){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                return ((CollideComponent)component).getCollidable();
            }
        }
        return false;
    }

    public Shape getCollisionShape(){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                return ((CollideComponent)component).getShape();
            }
        }
        return null;
    }

    public Vec2d collidesWith(GameObject obj){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.COLLIDE){
                return ((CollideComponent)component).collidesWith(obj);
            }
        }
        return null;
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(!this.isStatic() && obj.isStatic()){
            Vec2d newPosition = this.getPosition().plus(mtv.smult(-1));
            this.setPosition(newPosition);
        } else if(!this.isStatic()) {
            Vec2d newPosition = this.getPosition().plus(mtv.sdiv(2).smult(-1));
            this.setPosition(newPosition);
        }

        mtv = mtv.mag() != 0 ? mtv.normalize().smult(-1) : new Vec2d(0);

        boolean thisHasPhysics = this.get(ComponentTag.PHYSICS) != null;
        boolean objHasPhysics = obj.get(ComponentTag.PHYSICS) != null;
        if(!thisHasPhysics && !objHasPhysics){ return; }
        double cor = Math.sqrt(obj.getRestitution() * this.getRestitution());
        double ma = this.getMass();
        double mb = obj.getMass();
        Vec2d ua = mtv.mag() != 0 ? this.getVelocity().projectOnto(mtv) : new Vec2d(0);
        Vec2d ub = mtv.mag() != 0 ? obj.getVelocity().projectOnto(mtv.smult(-1)) : new Vec2d(0);
        if(thisHasPhysics  && objHasPhysics){
            Vec2d ia = obj.isStatic() ? ub.minus(ua).smult(mb*(1+cor)) : ub.minus(ua).smult(ma*mb*(1+cor)).sdiv((float)(ma+mb));
            this.applyImpulse(ia);
        } else if(thisHasPhysics && obj.isStatic()){
            Vec2d ia = ub.minus(ua).smult(ma*(1+cor));
            this.applyImpulse(ia);
        }
    }

    public void onTick(long nanosSincePreviousTick){
        for(GameComponent component : components){
            if(component.isTickable()){
                component.onTick(nanosSincePreviousTick);
            }
        }
        for(GameObject child : children){
            child.onTick(nanosSincePreviousTick);
        }
    }

    public void onDraw(GraphicsContext g){
        for(GameComponent component : components){
            if(component.getTag() == ComponentTag.SPRITE){
                ((SpriteComponent)component).onDraw(g, this.transformComponent);
            } else if(component.isDrawable()){
                component.onDraw(g);
            }
        }
        for(GameObject child : children){
            child.onDraw(g);
        }
    }

    public boolean inRange(double x, double y){
        boolean inX = this.getPosition().x <= x && x <= this.getPosition().x + this.getSize().x;
        boolean inY = this.getPosition().y <= y && y <= this.getPosition().y + this.getSize().y;
        return inX && inY;
    }

    public void onKeyPressed(KeyEvent e){}

    public void onKeyReleased(KeyEvent e){}

    public void onMousePressed(double x, double y){
        if(inRange(x, y)){
            for(GameComponent component : components){
                if(component.takesMouseInput()){
                    component.onMousePressed(x, y);
                }
            }
        }
        for(GameObject child : children){
            child.onMousePressed(x, y);
        }
    }

    public void onMouseReleased(double x, double y){
        // don't check whether it's in range so if you're off when you release, it still counts as a release
        for(GameComponent component : components){
            if(component.takesMouseInput()){
                component.onMouseReleased(x, y);
            }
        }
        for(GameObject child : children){
            child.onMouseReleased(x, y);
        }
    }

    public void onMouseDragged(double x, double y){
        // don't check for in range bc if they move the mouse super fast, you want to be able to catch up
        for(GameComponent component : components){
            if(component.takesMouseInput()){
                component.onMouseDragged(x, y, this);
            }
        }
        for(GameObject child : children){
            child.onMouseDragged(x, y);
        }
    }

    public double getMass() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getMass();
            }
        }
        return 0;
    }

    public void setMass(double mass) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setMass(mass);
            }
        }
    }

    public Vec2d getForce() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getForce();
            }
        }
        return new Vec2d(0);
    }

    public void setForce(Vec2d force) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setForce(force);
            }
        }
    }

    public Vec2d getImpulse() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getImpulse();
            }
        }
        return new Vec2d(0);
    }

    public void setImpulse(Vec2d impulse) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setImpulse(impulse);
            }
        }
    }

    public Vec2d getVelocity() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getVelocity();
            }
        }
        return new Vec2d(0);
    }

    public void setVelocity(Vec2d velocity) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setVelocity(velocity);
            }
        }
    }

    public Vec2d getAcceleration() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                return ((PhysicsComponent)component).getAcceleration();
            }
        }
        return new Vec2d(0);
    }

    public void setAcceleration(Vec2d acceleration) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).setAcceleration(acceleration);
            }
        }
    }

    public double getRestitution() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.COLLIDE){
                return ((CollideComponent)component).getRestitution();
            }
        }
        return 0;
    }

    public void setRestitution(double restitution) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.COLLIDE){
                ((CollideComponent)component).setRestitution(restitution);
            }
        }
    }

    public void applyForce(Vec2d force) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).applyForce(force);
            }
        }
    }

    public void applyImpulse(Vec2d impulse) {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).applyImpulse(impulse);
            }
        }
    }

    public void applyGravity() {
        for(GameComponent component : this.components){
            if(component.getTag() == ComponentTag.PHYSICS){
                ((PhysicsComponent)component).applyGravity();
            }
        }
    }

}
