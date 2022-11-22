package engine.game.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClickComponent extends GameComponent {

    public ClickComponent(){
        super(ComponentTag.CLICK);
        this.mouseInput = true;
    }

    public static ClickComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("CLICK")){ return null; }
        ClickComponent clickComponent = new ClickComponent();
        clickComponent.setConstants(ele);
        return clickComponent;
    }

}
