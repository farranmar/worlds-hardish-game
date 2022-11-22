package engine.game.components;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import nin.game.objects.Block;
import nin.game.objects.Platform;
import nin.game.objects.Player;
import nin.game.objects.Projectile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class PhysicsComponent extends GameComponent {

    private double mass = 0;
    private Vec2d force = new Vec2d(0);
    private Vec2d impulse = new Vec2d(0);
    private Vec2d velocity = new Vec2d(0);
    private Vec2d acceleration = new Vec2d(0);
    private GameObject obj;

    public PhysicsComponent(GameObject obj){
        super(ComponentTag.PHYSICS);
        this.obj = obj;
        this.tickable = true;
    }

    public PhysicsComponent(GameObject obj, double mass){
        super(ComponentTag.PHYSICS);
        this.obj = obj;
        this.tickable = true;
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Vec2d getForce() {
        return force;
    }

    public void setForce(Vec2d force) {
        this.force = force;
    }

    public Vec2d getImpulse() {
        return impulse;
    }

    public void setImpulse(Vec2d impulse) {
        this.impulse = impulse;
    }

    public Vec2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2d velocity) {
        this.velocity = velocity;
    }

    public Vec2d getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vec2d acceleration) {
        this.acceleration = acceleration;
    }

    public void applyForce(Vec2d newForce){
        this.force = this.force.plus(newForce);
    }

    public void applyImpulse(Vec2d newImpulse){
        this.impulse = this.impulse.plus(newImpulse);
    }

    public void applyGravity(){
        this.applyForce(new Vec2d(0, 25 * this.mass));
    }

    public void onTick(long nanosSincePreviousTick){
        double secSincePreviousTick = nanosSincePreviousTick * 0.000000001;
        this.velocity = this.velocity.plus(this.impulse.sdiv((float)this.mass)); // apply impulse
        this.velocity = this.velocity.plus(this.force.smult(secSincePreviousTick).sdiv((float)this.mass)); // apply force
        this.velocity = this.velocity.plus(this.acceleration.smult(secSincePreviousTick)); // acceleration
        this.force = new Vec2d(0);
        this.impulse = new Vec2d(0);
        Vec2d oldPos = obj.getPosition();
        obj.setPosition(oldPos.plus(this.velocity.smult(secSincePreviousTick * 10)));
    }

    public Element toXml(Document doc){
        Element ele = doc.createElement("Component");
        ele.setAttribute("tag", this.tag.toString());
        ele.setAttribute("tickable", this.tickable+"");
        ele.setAttribute("drawable", this.drawable+"");
        ele.setAttribute("keyInput", this.keyInput+"");
        ele.setAttribute("mouseInput", this.mouseInput+"");
        ele.setAttribute("mass", this.mass+"");
        Element force = this.force.toXml(doc, "Force");
        ele.appendChild(force);
        Element impulse = this.impulse.toXml(doc, "Impulse");
        ele.appendChild(impulse);
        Element velocity = this.velocity.toXml(doc, "Velocity");
        ele.appendChild(velocity);
        Element acceleration = this.acceleration.toXml(doc, "Acceleration");
        ele.appendChild(acceleration);
        return ele;
    }

    public static PhysicsComponent fromXml(Element ele, GameObject obj){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("PHYSICS")){ return null; }
        if(obj instanceof Projectile){
            System.out.println("bp");
        }
        PhysicsComponent physicsComponent = new PhysicsComponent(obj);
        physicsComponent.setConstants(ele);
        physicsComponent.setMass(Double.parseDouble(ele.getAttribute("mass")));
        Vec2d force = Vec2d.fromXml(getTopElementsByTagName(ele, "Force").get(0));
        physicsComponent.setForce(force);
        Vec2d impulse = Vec2d.fromXml(getTopElementsByTagName(ele, "Impulse").get(0));
        physicsComponent.setImpulse(impulse);
        Vec2d velocity = Vec2d.fromXml(getTopElementsByTagName(ele, "Velocity").get(0));
        physicsComponent.setVelocity(velocity);
        Vec2d acceleration = Vec2d.fromXml(getTopElementsByTagName(ele, "Acceleration").get(0));
        physicsComponent.setAcceleration(acceleration);
        return physicsComponent;
    }
}
