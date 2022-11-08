package engine.game.components;

public class TickComponent extends GameComponent {

    public TickComponent(){
        super(ComponentTag.TICK);
        this.tickable = true;
    }

}
