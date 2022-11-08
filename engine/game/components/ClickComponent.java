package engine.game.components;

public class ClickComponent extends GameComponent {

    public ClickComponent(){
        super(ComponentTag.CLICK);
        this.mouseInput = true;
    }

}
