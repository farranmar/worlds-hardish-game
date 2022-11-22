package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week6Reqs;

public class Week6 extends Week6Reqs {
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
    public Vec2d collision(AABShape s1, PolygonShape s2) {
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

    @Override
    public Vec2d collision(CircleShape s1, PolygonShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(PolygonShape s1, AABShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(PolygonShape s1, CircleShape s2) {
        return null;
    }

    @Override
    public Vec2d collision(PolygonShape s1, Vec2d s2) {
        return null;
    }

    @Override
    public Vec2d collision(PolygonShape s1, PolygonShape s2) {
        return null;
    }

    @Override
    public float raycast(AABShape s1, Ray s2) {
        return 0;
    }

    @Override
    public float raycast(CircleShape s1, Ray s2) {
        return 0;
    }

    @Override
    public float raycast(PolygonShape s1, Ray s2) {
        return 0;
    }
}
