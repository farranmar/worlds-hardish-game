package engine.game.components;

import org.w3c.dom.Element;

public class KeyComponent extends GameComponent {

    public KeyComponent(){
        super(ComponentTag.KEY);
        this.keyInput = true;
    }

    public static KeyComponent fromXml(Element ele){
        if(!ele.getTagName().equals("Component")){ return null; }
        if(!ele.getAttribute("tag").equals("KEY")){ return null; }
        KeyComponent keyComponent = new KeyComponent();
        keyComponent.setConstants(ele);
        return keyComponent;
    }

}
