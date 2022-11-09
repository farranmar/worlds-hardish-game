package engine.game.components;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import nin.game.objects.Block;
import nin.game.objects.Platform;
import nin.game.objects.Player;

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
        if(obj instanceof Block && !(obj instanceof Player) && !impulse.equals(new Vec2d(0))){
            System.out.println("getsf");
        }
        double secSincePreviousTick = nanosSincePreviousTick * 0.000000001;
        if(!impulse.equals(new Vec2d(0))){
            System.out.println("grea");
        }
        this.velocity = this.velocity.plus(this.impulse.sdiv((float)this.mass)); // apply impulse
        this.velocity = this.velocity.plus(this.force.smult(secSincePreviousTick).sdiv((float)this.mass)); // apply force
        this.velocity = this.velocity.plus(this.acceleration.smult(secSincePreviousTick)); // acceleration
        this.force = new Vec2d(0);
        this.impulse = new Vec2d(0);
        Vec2d oldPos = obj.getPosition();
        obj.setPosition(oldPos.plus(this.velocity.smult(secSincePreviousTick * 10)));
    }
}
