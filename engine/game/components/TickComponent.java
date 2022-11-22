package engine.game.components;

import org.w3c.dom.Element;

public class TickComponent extends GameComponent {

    public TickComponent(){
        super(ComponentTag.TICK);
        this.tickable = true;
    }

    public static TickComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("TICK")){ return null; }
        TickComponent tickComponent = new TickComponent();
        tickComponent.setConstants(ele);
        return tickComponent;
    }

}
