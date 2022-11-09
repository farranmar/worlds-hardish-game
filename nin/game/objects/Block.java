package nin.game.objects;

import engine.game.components.CollideComponent;
import engine.game.components.DrawComponent;
import engine.game.components.PhysicsComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block extends GameObject {

    protected boolean gravity;
    protected static final double mass = 5;
    protected final GravityRay[] gravRays = new GravityRay[2];
    protected final double restitution;
    private Color color;

    public Block(GameWorld world, Vec2d size, Vec2d position, double restitution){
        super(world, size, position);
        this.restitution = restitution;
        this.addComponents();
        this.gravity = true;
        this.color = Color.BLACK;
        GravityRay left = new GravityRay(this.gameWorld, this, new Vec2d(0, size.y/2 + 3), new Vec2d(position.x+3, position.y + size.y/2));
        GravityRay right = new GravityRay(this.gameWorld, this, new Vec2d(0, size.y/2 + 3), new Vec2d(position.x + size.x - 3, position.y + size.y/2));
        this.addChild(left);
        this.addChild(right);
        this.gravRays[0] = left;
        this.gravRays[1] = right;
        this.setMass(mass);
    }

    public Block(GameWorld world, Vec2d size, Vec2d position, double restitution, Color color){
        super(world, size, position);
        this.restitution = restitution;
        this.addComponents();
        this.gravity = true;
        this.color = color;
        GravityRay left = new GravityRay(this.gameWorld, this, new Vec2d(0, size.y/2 + 3), new Vec2d(position.x+3, position.y + size.y/2));
        GravityRay right = new GravityRay(this.gameWorld, this, new Vec2d(0, size.y/2 + 3), new Vec2d(position.x + size.x - 3, position.y + size.y/2));
        this.addChild(left);
        this.addChild(right);
        this.gravRays[0] = left;
        this.gravRays[1] = right;
        this.setMass(mass);
    }

    private void addComponents(){
        CollideComponent collide = new CollideComponent(new AAB(this.getSize(), this.getPosition()), this.restitution);
        this.add(collide);
        DrawComponent draw = new DrawComponent();
        this.add(draw);
        PhysicsComponent physics = new PhysicsComponent(this);
        this.add(physics);
        TickComponent tick = new TickComponent();
        this.add(tick);
    }

    public boolean getGravity(){
        return this.gravity;
    }

    public void setGravity(boolean g){
        this.gravity = g;
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        super.setPosition(newPosition);
        Vec2d size = this.getSize();
        this.gravRays[0].setPosition(new Vec2d(newPosition.x, newPosition.y + size.y/2));
        this.gravRays[1].setPosition(new Vec2d(newPosition.x + size.x, newPosition.y + size.y/2));
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(obj instanceof GravityRay){ return; }
        super.onCollide(obj, mtv);
    }

    public void onDraw(GraphicsContext g){
        g.setFill(this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        if(this.gravity){
            applyGravity();
        }
        this.gravity = true;
    }

}
