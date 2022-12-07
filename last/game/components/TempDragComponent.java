package last.game.components;

import engine.game.components.DragComponent;
import engine.game.objects.GameObject;
import last.game.objects.DeathBall;

public class TempDragComponent extends DragComponent {

    public TempDragComponent(GameObject obj){
        super(obj);
    }

    @Override
    public void onMouseReleased(double x, double y) {
        super.onMouseReleased(x, y);
        assert(obj instanceof DeathBall);
        ((DeathBall)obj).addSlideComponent();
    }
}
