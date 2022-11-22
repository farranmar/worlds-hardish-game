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
import nin.game.NinWorld;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static engine.game.world.GameWorld.getTopElementsByTagName;

public class Block extends GameObject {

    protected boolean gravity = false;
    protected static final double mass = 5;
    protected double restitution = 1;
    protected ArrayList<Projectile> projectiles = new ArrayList<>();
    protected Color color;

    public Block(GameWorld world){
        super(world, new Vec2d(0), new Vec2d(0));
    }

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
        this.setMass(mass);
    }

    public Block() {
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

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setPosition(Vec2d newPosition) {
        super.setPosition(newPosition);
        Vec2d size = this.getSize();
        this.children.get(0).setPosition(new Vec2d(newPosition.x, newPosition.y + size.y/2));
        this.children.get(1).setPosition(new Vec2d(newPosition.x + size.x, newPosition.y + size.y/2));
    }

    public void fireProjectile(Projectile.Direction direction){
        Vec2d curPos = this.getPosition();
        Vec2d curSize = this.getSize();
        Vec2d projSize = new Vec2d(curSize.x/2, curSize.y/2);
        Vec2d projPos;
        Vec2d impulse;
        if(direction == Projectile.Direction.LEFT){
            projPos = new Vec2d(curPos.x - projSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
            impulse = new Vec2d(-30, 0);
        } else if (direction == Projectile.Direction.RIGHT){
            projPos = new Vec2d(curPos.x+curSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
            impulse = new Vec2d(30, 0);
        } else if (direction == Projectile.Direction.DOWN){
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y+curSize.y);
            impulse = new Vec2d(0, 30);
        } else {
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y);
            impulse = new Vec2d(0, -30);
        }
        Projectile proj = new Projectile(this.gameWorld, this, projSize, projPos, direction, Color.rgb(204, 40, 28));
        proj.applyImpulse(impulse);
        this.projectiles.add(proj);
//        this.gameWorld.addToAdditionQueue(proj);
        this.gameWorld.addToSystems(proj);
    }

    public void removeProjectile(Projectile proj){
        this.gameWorld.addToRemovalQueue(proj);
        this.projectiles.remove(proj);
    }

    public void onCollide(GameObject obj, Vec2d mtv){
        if(obj instanceof GravityRay){ return; }
        super.onCollide(obj, mtv);
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        g.setFill(this.color);
        System.out.println("coolro:"+this.color);
        Vec2d size = this.getSize();
        Vec2d pos = this.getPosition();
        g.fillRect(pos.x, pos.y, size.x, size.y);
        for(Projectile proj : this.projectiles){
            proj.onDraw(g);
        }
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        for(Projectile proj : this.projectiles){
            proj.onTick(nanosSinceLastTick);
        }
        if(this.gravity){
            applyGravity();
        }
        this.gravity = true;
    }

    @Override
    public Element toXml(Document doc) {
        Element ele =  super.toXml(doc);
        ele.setAttribute("class", "Block");
        ele.setAttribute("gravity", this.gravity+"");
        ele.setAttribute("restitution", this.restitution+"");
        Element color = colorToXml(doc, this.color);
        ele.appendChild(color);

        Element projs = doc.createElement("Projectiles");
        for(Projectile proj : this.projectiles){
            Element projEle = proj.toXml(doc);
            projs.appendChild(projEle);
        }
        ele.appendChild(projs);

        return ele;
    }

    public Block (Element ele, GameWorld world){
        if(!ele.getTagName().equals("GameObject")){ return; }
        if(!ele.getAttribute("class").equals("Block")){ return; }
        this.gameWorld = world;
        this.setConstantsXml(ele);
        this.setGravity(Boolean.parseBoolean(ele.getAttribute("gravity")));
        this.setRestitution(Double.parseDouble(ele.getAttribute("restitution")));
        Color color = colorFromXml(getTopElementsByTagName(ele, "Color").get(0));
        this.setColor(color);

        Element componentsEle = getTopElementsByTagName(ele, "Components").get(0);
        this.addComponentsXml(componentsEle);

        Element projsEle = getTopElementsByTagName(ele,"Projectiles").get(0);
        this.addGravRaysAndProjs(projsEle);

        Element childrenEle = getTopElementsByTagName(ele, "Children").get(0);
        this.setChildrenXml(childrenEle, NinWorld.getClassMap());
        assert(this.children.size() == 2);
        assert(this.children.get(0) instanceof GravityRay);
        assert(this.children.get(1) instanceof GravityRay);
    }

    protected void addGravRaysAndProjs(Element projsEle){
        List<Element> projsList = getTopElementsByTagName(projsEle, "GameObject");
        for(int i = 0; i < projsList.size(); i++){
            Element projEle = projsList.get(i);
            assert(projEle.getAttribute("class").equals("Projectile"));
            Projectile proj = new Projectile(projEle, this.gameWorld);
            proj.setParent(this);
            this.projectiles.add(proj);
        }
    }
}
