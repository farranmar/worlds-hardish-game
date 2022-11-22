package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week2Reqs;

public class Week2 extends Week2Reqs {
    @Override
    public boolean isColliding(AABShape s1, AABShape s2) {
        return false;
    }

    @Override
    public boolean isColliding(AABShape s1, CircleShape s2) {
        return false;
    }

    @Override
    public boolean isColliding(AABShape s1, Vec2d s2) {
        return false;
    }

    @Override
    public boolean isColliding(CircleShape s1, AABShape s2) {
        return false;
    }

    @Override
    public boolean isColliding(CircleShape s1, CircleShape s2) {
        return false;
    }

    @Override
    public boolean isColliding(CircleShape s1, Vec2d s2) {
        return false;
    }
}
