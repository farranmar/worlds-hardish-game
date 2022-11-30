package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week6Reqs;
import debugger.support.shapes.PolygonShapeDefine;

import java.util.ArrayList;

public class Week6 extends Week6Reqs {
    @Override
    public Vec2d collision(AABShape s1, AABShape s2) {
        boolean projX = (s1.getTopLeft().x <= s2.getTopLeft().x+s2.getSize().x) && (s1.getTopLeft().x+s1.size.x >= s2.getTopLeft().x);
        boolean projY = (s1.getTopLeft().y <= s2.getTopLeft().y+s2.getSize().y) && (s1.getTopLeft().y+s1.size.y >= s2.getTopLeft().y);
        if(!projX || !projY){
            return null;
        }
        Vec2d up = new Vec2d(0, s1.getTopLeft().y+s1.getSize().y - s2.getTopLeft().y);
        Vec2d down = new Vec2d(0, s1.getTopLeft().y - (s2.getTopLeft().y+s2.getSize().y));
        Vec2d left = new Vec2d(s1.getTopLeft().x+s1.getSize().x - s2.getTopLeft().x, 0);
        Vec2d right = new Vec2d(s1.getTopLeft().x - (s2.getTopLeft().x+s2.getSize().x), 0);
        return Vec2d.min(up, Vec2d.min(down, Vec2d.min(left, right))).reflect();
    }

    @Override
    public Vec2d collision(AABShape s1, CircleShape s2) {
        Vec2d maxes = new Vec2d(s1.getTopLeft().x + s1.getSize().x, s1.getTopLeft().y + s1.getSize().y);
        Vec2d nearestPoint = this.clamp(s2.getCenter(), s1.getTopLeft(), maxes);
        double dist = s2.getCenter().dist2(nearestPoint);
        if(dist > (s2.getRadius() * s2.getRadius())){ return null; }
        // if circle center in aab
        if(s2.getCenter().x >= s1.getTopLeft().x && s2.getCenter().x <= s1.getTopLeft().x+s1.getSize().x && s2.getCenter().y >= s1.getTopLeft().y && s2.getCenter().y <= s1.getTopLeft().y+s1.getSize().y){
            double left = s2.getCenter().x - s1.getTopLeft().x;
            double right = s1.getTopLeft().x+s1.getSize().x - s2.getCenter().x;
            double down = s1.getTopLeft().y+s1.getSize().y - s2.getCenter().y;
            double up = s2.getCenter().y - s1.getTopLeft().y;
            double min = Math.min(left, Math.min(right, Math.min(up, down)));
            if(min == left){ return new Vec2d(-1 * (s2.getRadius() + left), 0).reflect(); }
            else if(min == right){ return new Vec2d(right + s2.getRadius(), 0).reflect(); }
            else if(min == up){ return new Vec2d(0, -1 * (up + s2.getRadius())).reflect(); }
            else { return new Vec2d(0, s2.getRadius() + down).reflect(); }
        } else { // circle center not in aab
            double mag = s2.getRadius() - s2.getCenter().dist(nearestPoint);
            double angle = new Vec2d(s2.getCenter().x - nearestPoint.x, s2.getCenter().y - nearestPoint.y).angle();
            return Vec2d.fromPolar(angle, mag).reflect();
        }
    }

    @Override
    public Vec2d collision(AABShape s1, Vec2d s2) {
        if(s2.x < s1.getTopLeft().x || s2.x > s1.getTopLeft().x+s1.getSize().x || s2.y < s1.getTopLeft().y || s2.y > s1.getTopLeft().y+s1.getSize().y){
            return null;
        }
        double right = s1.getTopLeft().x + s1.getSize().x - s2.x;
        double left = s2.x - s1.getTopLeft().x;
        double up = s2.y - s1.getTopLeft().y;
        double down = s1.getTopLeft().y + s1.getSize().y - s2.y;
        double min = Math.min(right, Math.min(left, Math.min(up, down)));
        if(min == right){ return new Vec2d(-1 * right, 0); }
        else if(min == left){ return new Vec2d(left, 0); }
        else if(min == up){ return new Vec2d(0, -1 * up); }
        else { return new Vec2d(0, down); }
    }

    @Override
    public Vec2d collision(AABShape s1, PolygonShape s2) {
        ArrayList<Vec2d> edges = new ArrayList<>();
        edges.add(new Vec2d(0, s1.getSize().y));
        edges.add(new Vec2d(s1.getSize().x, 0));
        edges.add(new Vec2d(0, -1*s1.getSize().y));
        edges.add(new Vec2d(-1*s1.getSize().x, 0));
        for(int i = 0; i < s2.getNumPoints(); i++){
            Vec2d base = s2.getPoint(i);
            Vec2d end = s2.getPoint((i+1) % s2.getNumPoints());
            Vec2d v = end.minus(base);
            edges.add(v);
        }
        ArrayList<Vec2d> sepAxes = new ArrayList<>();
        for(Vec2d edge : edges){
            sepAxes.add(edge.perpendicular());
        }
        Vec2d mtv = new Vec2d(Double.MAX_VALUE);
        for(Vec2d axis : sepAxes){
            Vec2d[] proj1 = this.projectAabTo(s1, axis);
            Vec2d[] proj2 = this.projectPolyTo(s2, axis);
            Vec2d origin = new Vec2d(0);
            Vec2d interval1 = new Vec2d(proj1[0].dist(origin), proj1[1].dist(origin));
            Vec2d interval2 = new Vec2d(proj2[0].dist(origin), proj2[1].dist(origin));
            Double oneDimMtv = this.oneDimMtv(interval1, interval2);
            if(oneDimMtv == null){ return null; }
            Vec2d thisMtv = axis.normalize().smult(oneDimMtv);
            if(thisMtv.mag() < mtv.mag()){
                mtv = thisMtv;
            }
        }
        return mtv;
    }

    @Override
    public Vec2d collision(CircleShape s1, AABShape s2) {
        Vec2d f = collision(s2, s1);
        return f == null ? null : f.reflect();
    }

    @Override
    public Vec2d collision(CircleShape s1, CircleShape s2) {
        if(s1.getCenter().dist(s2.getCenter()) > s1.getRadius() + s2.getRadius()){ return null; }
        double mag = s1.getRadius() + s2.getRadius() - s1.getCenter().dist(s2.getCenter());
        double angle = new Vec2d(s1.getCenter().x - s2.getCenter().x, s1.getCenter().y - s2.getCenter().y).angle();
        return Vec2d.fromPolar(angle, mag);
    }

    @Override
    public Vec2d collision(CircleShape s1, Vec2d s2) {
        if(s1.getCenter().dist(s2) > s1.getRadius()){ return null; }
        double distToEdge = s1.getRadius() - (s1.getCenter().dist(s2));
        double angle = new Vec2d(s2.x - s1.getCenter().x, s2.y - s1.getCenter().y).angle();
        Vec2d vec = new Vec2d(distToEdge, 0);
        return vec.rotate(angle);
    }

    @Override
    public Vec2d collision(CircleShape s1, PolygonShape s2) {
        ArrayList<Vec2d> edges = new ArrayList<>();
        double minCircleDist = Double.MAX_VALUE;
        Vec2d minCirclePoint = s2.getPoint(0);
        for(int i = 0; i < s2.getNumPoints(); i++){
            Vec2d base = s2.getPoint(i);
            Vec2d end = s2.getPoint((i+1) % s2.getNumPoints());
            Vec2d v = end.minus(base);
            edges.add(v);
            if(base.dist(s1.getCenter()) < minCircleDist){
                minCirclePoint = base;
                minCircleDist = base.dist(s1.getCenter());
            }
        }
        Vec2d circleSepAxis = minCirclePoint.minus(s1.getCenter());
        ArrayList<Vec2d> sepAxes = new ArrayList<>();
        for(Vec2d edge : edges){
            sepAxes.add(edge.perpendicular());
        }
        sepAxes.add(circleSepAxis);
        Vec2d mtv = new Vec2d(Double.MAX_VALUE);
        for(Vec2d axis : sepAxes){
            Vec2d[] proj1 = this.projectCircleTo(s1, axis);
            Vec2d[] proj2 = this.projectPolyTo(s2, axis);
            Vec2d origin = new Vec2d(0);
            Vec2d interval1 = new Vec2d(proj1[0].dist(origin), proj1[1].dist(origin));
            Vec2d interval2 = new Vec2d(proj2[0].dist(origin), proj2[1].dist(origin));
            Double oneDimMtv = this.oneDimMtv(interval1, interval2);
            if(oneDimMtv == null){ return null; }
            Vec2d thisMtv = axis.normalize().smult(oneDimMtv);
            if(thisMtv.mag() < mtv.mag()){
                mtv = thisMtv;
            }
        }
        return mtv;
    }

    @Override
    public Vec2d collision(PolygonShape s1, AABShape s2) {
        Vec2d f = collision(s2, s1);
        return f == null ? null : f.reflect();
    }

    @Override
    public Vec2d collision(PolygonShape s1, CircleShape s2) {
        Vec2d f = collision(s2, s1);
        return f == null ? null : f.reflect();
    }

    @Override
    public Vec2d collision(PolygonShape s1, Vec2d s2) {
        boolean inside = true;
        for(int i = 0; i < s1.getNumPoints(); i++){
            Vec2d base = s1.getPoint(i);
            Vec2d end = s1.getPoint((i+1) % s1.getNumPoints());
            Vec2d v = end.minus(base);
            Vec2d p = s2.minus(base);
            if(v.cross(p) > 0){
                inside = false;
                break;
            }
        }
        if(!inside){ return null; }
        return new Vec2d(0);
    }

    @Override
    public Vec2d collision(PolygonShape s1, PolygonShape s2) {
        ArrayList<Vec2d> edges = new ArrayList<>();
        for(int i = 0; i < s1.getNumPoints(); i++){
            Vec2d base = s1.getPoint(i);
            Vec2d end = s1.getPoint((i+1) % s1.getNumPoints());
            Vec2d v = end.minus(base);
            edges.add(v);
        }
        for(int i = 0; i < s2.getNumPoints(); i++){
            Vec2d base = s2.getPoint(i);
            Vec2d end = s2.getPoint((i+1) % s2.getNumPoints());
            Vec2d v = end.minus(base);
            edges.add(v);
        }
        ArrayList<Vec2d> sepAxes = new ArrayList<>();
        for(Vec2d edge : edges){
            sepAxes.add(edge.perpendicular());
        }
        Vec2d mtv = new Vec2d(Double.MAX_VALUE);
        for(Vec2d axis : sepAxes){
            Vec2d[] proj1 = this.projectPolyTo(s1, axis);
            Vec2d[] proj2 = this.projectPolyTo(s2, axis);
            Vec2d origin = new Vec2d(0);
            Vec2d interval1 = new Vec2d(proj1[0].dist(origin), proj1[1].dist(origin));
            Vec2d interval2 = new Vec2d(proj2[0].dist(origin), proj2[1].dist(origin));
            Double oneDimMtv = this.oneDimMtv(interval1, interval2);
            if(oneDimMtv == null){ return null; }
            Vec2d thisMtv = axis.normalize().smult(oneDimMtv);
            if(thisMtv.mag() < mtv.mag()){
                mtv = thisMtv;
            }
        }
        return mtv;
    }

    @Override
    public float raycast(AABShape s1, Ray s2) {
        Vec2d topLeft = s1.getTopLeft();
        Vec2d botLeft = s1.getTopLeft().plus(new Vec2d(0, s1.getSize().y));
        Vec2d botRight = s1.getTopLeft().plus(s1.getSize());
        Vec2d topRight = s1.getTopLeft().plus(new Vec2d(s1.getSize().x, 0));
        PolygonShape polyAab = new PolygonShapeDefine(topLeft, botLeft, botRight, topRight);
        return this.raycast(polyAab, s2);
    }

    @Override
    public float raycast(CircleShape s1, Ray s2) {
        return -1;
    }

    @Override
    public float raycast(PolygonShape s1, Ray s2) {
        double minDist = Double.MAX_VALUE;
        Vec2d p = s2.src;
        Vec2d d = s2.dir;
        for(int i = 0; i < s1.getNumPoints(); i++){
            Vec2d a = s1.getPoint(i);
            Vec2d b = s1.getPoint((i+1) % s1.getNumPoints());
            if(straddles(s2, a, b)){
                Vec2d m = b.minus(a).normalize();
                Vec2d n = m.perpendicular().normalize();
                double t = b.minus(p).dot(n) / d.dot(n);
                Vec2d q = p.plus(d.smult(t));
                double dist = p.dist(q);
                if(dist < minDist){
                    minDist = dist;
                }
            }
        }
        if(minDist == Double.MAX_VALUE){ minDist = -1; }
        return (float)minDist;
    }

    private boolean straddles(Ray ray, Vec2d a, Vec2d b){
        Vec2d p = ray.src;
        Vec2d d = ray.dir;
        double leftCross = a.minus(p).cross(d);
        double rightCross = b.minus(p).cross(d);
        if(leftCross * rightCross > 0){
            return false;
        }
        return true;
    }

    private Vec2d[] projectPolyTo(PolygonShape s1, Vec2d axis){
        axis = axis.normalize();
        Vec2d min = new Vec2d(Double.MAX_VALUE);
        double minDist = min.dist(new Vec2d(0));
        Vec2d max = new Vec2d(0);
        double maxDist = max.dist(new Vec2d(0));
        for(int i = 0; i < s1.getNumPoints(); i++){
            Vec2d proj = s1.getPoint(i).projectOnto(axis);
            double dist = proj.dist(new Vec2d(0));
            if(dist < minDist){
                min = proj;
                minDist = dist;
            }
            if(dist > maxDist){
                max = proj;
                maxDist = dist;
            }
        }
        return new Vec2d[]{min, max};
    }

    private Vec2d[] projectCircleTo(CircleShape s1, Vec2d axis){
        axis = axis.normalize();
        Vec2d centerProj = s1.getCenter().projectOnto(axis);
        double rad = axis.angle();
        Vec2d radiusProj = Vec2d.fromPolar(rad, s1.getRadius());
        Vec2d min = centerProj.minus(radiusProj);
        Vec2d max = centerProj.plus(radiusProj);
        return new Vec2d[]{min, max};
    }

    private Vec2d[] projectAabTo(AABShape s1, Vec2d axis){
        axis = axis.normalize();
        Vec2d min = new Vec2d(Double.MAX_VALUE);
        double minDist = min.dist(new Vec2d(0));
        Vec2d max = new Vec2d(0);
        double maxDist = max.dist(new Vec2d(0));
        ArrayList<Vec2d> points = new ArrayList<>();
        points.add(s1.getTopLeft());
        points.add(s1.getTopLeft().plus(new Vec2d(s1.getSize().x, 0)));
        points.add(s1.getTopLeft().plus(new Vec2d(0, s1.getSize().y)));
        points.add(s1.getTopLeft().plus(s1.getSize()));
        for(Vec2d point : points){
            Vec2d proj = point.projectOnto(axis);
            double dist = proj.dist(new Vec2d(0));
            if(dist < minDist){
                min = proj;
                minDist = dist;
            }
            if(dist > maxDist){
                max = proj;
                maxDist = dist;
            }
        }
        return new Vec2d[]{min, max};
    }

    private Double oneDimMtv(Vec2d proj1, Vec2d proj2){
        // no overlap
        if((proj1.x < proj2.x && proj1.y < proj2.x) || (proj1.x > proj2.y && proj1.y > proj2.y)){
            return null;
        }
        //  ---------
        //    ----
        if((proj1.x >= proj2.x && proj1.y <= proj2.y) || (proj1.x <= proj2.x && proj1.y >= proj2.y)){
            double left = proj1.y - proj2.x;
            double right = proj2.y - proj1.x;
            return left < right ? -1*left : right;
        }
        //    ----------  proj2
        // -------        proj1
        if(proj1.x < proj2.x && proj1.y >= proj2.x){
            return -1*(proj1.y - proj2.x);
        }
        // -------        proj2
        //    ----------  proj1
        if(proj2.x <= proj1.x && proj2.y > proj1.x){
            return proj2.y-proj1.x;
        }
        return null;
    }

    private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
        double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
        double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
        return new Vec2d(xClamp, yClamp);
    }
}
