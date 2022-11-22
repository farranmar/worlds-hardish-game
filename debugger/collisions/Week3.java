package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week3Reqs;

public class Week3 extends Week3Reqs {
    @Override
    public Vec2d collision(AABShape s1, AABShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(AABShape s1, CircleShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(AABShape s1, Vec2d s2) {
        return null;
    }

    @Override
    public Vec2d collision(CircleShape s1, AABShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(CircleShape s1, CircleShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(CircleShape s1, Vec2d s2) {
        return null;
    }
}
