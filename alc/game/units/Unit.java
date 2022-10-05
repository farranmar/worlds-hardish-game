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
    private static HashMap<Type, String> typeNameMap = null;
    private Type type;
    private static int index = 2;

    public enum Type{
        AIR,
        DEAD_TREE,
        EARTH,
        FIRE,
        MUSHROOM,
        SPROUT,
        SUN,
        TRASH,
        TREE,
        WATER,
    }

    public Unit(GameWorld gameWorld, Type type){
        super(gameWorld);
        this.constructTypeNameMap();
        this.drawPriority = index;
        this.type = type;
        this.components.add(new HasSprite(new Resource().get(typeNameMap.get(type))));
        this.components.add(new TransformComponent(new Vec2d(0), new Vec2d(0)));
        this.addComponentsAndHash();
        index++;
    }

    public Unit(GameWorld gameWorld, Vec2d size, Vec2d position){
        super(gameWorld);
        this.constructTypeNameMap();
        this.drawPriority = index;
        this.type = null;
        this.components.add(new HasSprite());
        this.components.add(new TransformComponent(size, position));
        this.addComponentsAndHash();
        index++;
    }

    public Unit(GameWorld gameWorld, Type type, Vec2d size, Vec2d position){
        super(gameWorld);
        this.constructTypeNameMap();
        this.drawPriority = index;
        this.type = type;
        this.components.add(new HasSprite(new Resource().get(typeNameMap.get(type))));
        this.components.add(new TransformComponent(size, position));
        this.addComponentsAndHash();
        index++;
    }

    public Unit(Unit unit) {
        this(unit.gameWorld, unit.getSize(), unit.getPosition());
        this.setType(unit.getType());
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

    private void constructTypeNameMap(){
        HashMap<Type, String> tnMap = new HashMap<>();
        tnMap.put(Type.AIR, "air_ph.jpg");
        tnMap.put(Type.EARTH, "dirt_ph.png");
        tnMap.put(Type.FIRE, "fire_ph.jpg");
        tnMap.put(Type.WATER, "water_ph.png");
        tnMap.put(Type.SPROUT, "sprout_ref.png");
        tnMap.put(Type.SUN, "sun_ph.jpg");
        tnMap.put(Type.TREE, "tree_ph.png");
        tnMap.put(Type.DEAD_TREE, "deadtree_ph.png");
        tnMap.put(Type.MUSHROOM, "mushroom_ph.jpg");
        tnMap.put(Type.TRASH, "trash_ph.png");
        typeNameMap = tnMap;
    }

    private void addComponentsAndHash(){
        this.components.add(new Drawable());
        this.components.add(new Clickable());
        if(collisionMap == null){
            collisionMap = constructHash();
        }
    }

    private String getImageName(){
        return typeNameMap.get(this.type);
    }

    private void setType(Type type){
        this.type = type;
        for(GameComponent component : components){
            if(component.getTag() == Tag.HAS_SPRITE){
                ((HasSprite)component).setImage(new Resource().get(typeNameMap.get(type)));
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
        clone.add(new Collidable(new AAB(this.getSize(), this.getPosition())));
        return clone;
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
    }

    // assumes GameObject unit is a Unit, as those are the only collidable GameObjects in alc
    public void onCollide(GameObject obj){
        Unit unit = (Unit)obj;
        Type newType = collisionMap.get(this.type).get(unit.getType());
        if(newType == null){ return; }
        Unit newUnit = new Unit(this.gameWorld, newType);
        newUnit.add(new Draggable(newUnit));
        Vec2d thisPosition = this.getPosition();
        Vec2d objPosition = obj.getPosition();
        Vec2d newPosition = new Vec2d((thisPosition.x + objPosition.x)/2, (thisPosition.y + objPosition.y)/2);
        Vec2d thisSize = this.getSize();
        Vec2d objSize = obj.getSize();
        Vec2d newSize = new Vec2d((thisSize.x + objSize.x)/2, (thisSize.y + objSize.y)/2);
        AAB aab = new AAB(newSize, newPosition);
        newUnit.add(new Collidable(aab));
        newUnit.setPosition(newPosition);
        newUnit.setSize(newSize);
        this.gameWorld.addToAdditionQueue(newUnit);
        this.gameWorld.addToRemovalQueue(obj);
        this.gameWorld.addToRemovalQueue(this);
    }

    public String toString(){
        return super.toString() + "-"+ this.type+"(dp="+this.drawPriority+")";
    }

}
