package engine.game.components;

public enum ComponentTag {
    CLICK("CLICK"),
    COLLIDE("COLLIDE"),
    DRAG("DRAG"),
    DRAW("DRAW"),
    KEY("KEY"),
    PHYSICS("PHYSICS"),
    SLIDE("SLIDE"),
    SPRITE("SPRITE"),
    TICK("TICK"),
    TRANSFORM("TRANSFORM");
    
    private String string;
    ComponentTag(String s){
        string = s;
    }

    @Override
    public String toString() {
        return string;
    }
}
