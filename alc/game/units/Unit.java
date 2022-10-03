package alc.game.units;

import alc.resources.Resource;
import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

public class Unit extends GameObject {

    private static HashMap<Type, HashMap<Type, Type>> collisionMap = null;
    private String imageName;
    private Type type;


    public enum Type{
        AIR,
        DEAD_TREE,
        EARTH,
        FIRE,
        MUSHROOM,
        SPROUT,
        SUN,
        TREE,
        WATER,
    }

    public Unit(GameWorld gameWorld, String imageName){
        super(gameWorld);
        this.drawPriority = 2;
        this.imageName = imageName;
        this.addType(imageName);
        this.components.add(new HasSprite(new Resource().get(imageName)));
        this.components.add(new TransformComponent(new Vec2d(0), new Vec2d(0)));
        this.addComponentsAndHash();
    }

    public Unit(GameWorld gameWorld, Vec2d size, Vec2d position){
        super(gameWorld);
        this.drawPriority = 2;
        this.imageName = "";
        this.addType(imageName);
        this.components.add(new HasSprite());
        this.components.add(new TransformComponent(size, position));
        this.addComponentsAndHash();
    }

    public Unit(GameWorld gameWorld, String imageName, Vec2d size, Vec2d position){
        super(gameWorld);
        this.drawPriority = 2;
        this.imageName = imageName;
        this.addType(imageName);
        this.components.add(new HasSprite(new Resource().get(imageName)));
        this.components.add(new TransformComponent(size, position));
        this.addComponentsAndHash();
    }

    public Unit(Unit unit) {
        this(unit.gameWorld, unit.getSize(), unit.getPosition());
        this.setImageName(unit.getImageName());
    }

    private static HashMap<Type, HashMap<Type, Type>> constructHash() {
        HashMap<Type, HashMap<Type, Type>> hash = new HashMap<>();

        HashMap<Type, Type> airMap = new HashMap<>();
        airMap.put(Type.FIRE, Type.SUN);
        hash.put(Type.AIR, airMap);

        HashMap<Type, Type> earthMap = new HashMap<>();
        earthMap.put(Type.WATER, Type.SPROUT);
        hash.put(Type.EARTH, earthMap);

        HashMap<Type, Type> fireMap = new HashMap<>();
        fireMap.put(Type.AIR, Type.SUN);
        fireMap.put(Type.TREE, Type.DEAD_TREE);
        hash.put(Type.FIRE, fireMap);

        HashMap<Type, Type> waterMap = new HashMap<>();
        waterMap.put(Type.EARTH, Type.SPROUT);
        waterMap.put(Type.DEAD_TREE, Type.MUSHROOM);
        hash.put(Type.WATER, waterMap);

        HashMap<Type, Type> sproutMap = new HashMap<>();
        sproutMap.put(Type.SUN, Type.TREE);
        hash.put(Type.SPROUT, sproutMap);

        HashMap<Type, Type> sunMap = new HashMap<>();
        sunMap.put(Type.SPROUT, Type.TREE);
        hash.put(Type.SUN, sunMap);

        HashMap<Type, Type> treeMap = new HashMap<>();
        treeMap.put(Type.FIRE, Type.DEAD_TREE);
        hash.put(Type.TREE, treeMap);

        HashMap<Type, Type> deadMap = new HashMap<>();
        deadMap.put(Type.WATER, Type.MUSHROOM);
        hash.put(Type.DEAD_TREE, deadMap);

        hash.put(Type.MUSHROOM, new HashMap<>());
        return hash;
    }

    private void addType(String imageName){
        if(imageName.contains("air")){
            this.type = Type.AIR;
        } else if(imageName.contains("earth")){
            this.type = Type.EARTH;
        } else if(imageName.contains("fire")){
            this.type = Type.FIRE;
        } else if(imageName.contains("water")){
            this.type = Type.WATER;
        } else if(imageName.contains("sprout")){
            this.type = Type.SPROUT;
        } else if(imageName.contains("sun")){
            this.type = Type.SUN;
        } else if(imageName.contains("dead")){
            this.type = Type.DEAD_TREE;
        } else if(imageName.contains("tree")){
            this.type = Type.TREE;
        } else if(imageName.contains("shroom")){
            this.type = Type.MUSHROOM;
        }
    }

    private void addComponentsAndHash(){
        this.components.add(new Drawable());
        this.components.add(new Clickable());
        if(collisionMap == null){
            collisionMap = constructHash();
            System.out.println("collisionMap has been constructed with size "+collisionMap.size());
        }
    }

    private String getImageName(){
        return this.imageName;
    }

    private void setImageName(String imageName){
        this.imageName = imageName;
        this.addType(imageName);
        for(GameComponent component : components){
            if(component.getTag() == Tag.HAS_SPRITE){
                ((HasSprite)component).setImage(new Resource().get(imageName));
            }
        }
    }

    public Type getType(){
        return this.type;
    }

    public Unit clone(){
        Unit clone = new Unit(this);
        clone.setSize(this.getSize());
        clone.setPosition(this.getPosition());
        clone.setDrawPriority(this.drawPriority + 1);
        clone.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        return clone;
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
    }

    // assumes GameObject unit is a Unit, as those are the only collidable GameObjects in alc
    public void onCollide(GameObject obj){
        Unit unit = (Unit)obj;
        System.out.println("unit colliding");
        System.out.println("collisionMap.get("+this.type+") = "+collisionMap.get(this.type));
        Type newType = collisionMap.get(this.type).get(unit.getType());
        System.out.println("combining "+this.type+" with "+unit.getType()+" to form "+newType);
    }

}
