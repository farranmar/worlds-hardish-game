package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week3Reqs;

/**
 * Fill this class in during Week 3. Make sure to also change the week variable in Display.java.
 */
public final class Week3 extends Week3Reqs {

	// AXIS-ALIGNED BOXES
	
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
	
	// CIRCLES

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

	private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
		double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
		double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
		return new Vec2d(xClamp, yClamp);
	}

}
