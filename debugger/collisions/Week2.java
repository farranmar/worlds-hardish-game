package debugger.collisions;

import debugger.support.Vec2d;
import debugger.support.interfaces.Week2Reqs;

/**
 * Fill this class in during Week 2.
 */
public final class Week2 extends Week2Reqs {

	// AXIS-ALIGNED BOXES
	
	@Override
	public boolean isColliding(AABShape s1, AABShape s2) {
		boolean projX = (s1.getTopLeft().x <= s2.getTopLeft().x+s2.getSize().x) && (s1.getTopLeft().x+s1.size.x >= s2.getTopLeft().x);
		boolean projY = (s1.getTopLeft().y <= s2.getTopLeft().y+s2.getSize().y) && (s1.getTopLeft().y+s1.size.y >= s2.getTopLeft().y);
		return projX && projY;
	}

	@Override
	public boolean isColliding(AABShape s1, CircleShape s2) {
		Vec2d maxes = new Vec2d(s1.getTopLeft().x + s1.getSize().x, s1.getTopLeft().y + s1.getSize().y);
		Vec2d nearestPoint = this.clamp(s2.getCenter(), s1.getTopLeft(), maxes);
		double dist = s2.getCenter().dist2(nearestPoint);
		return dist < (s2.getRadius() * s2.getRadius());
	}

	@Override
	public boolean isColliding(AABShape s1, Vec2d s2) {
		boolean inX = s1.getTopLeft().x <= s2.x && s1.getTopLeft().x+s1.getSize().x >= s2.x;
		boolean inY = s1.getTopLeft().y <= s2.y && s1.getTopLeft().y+s1.getSize().y >= s2.y;
		return inX && inY;
	}

	// CIRCLES
	
	@Override
	public boolean isColliding(CircleShape s1, AABShape s2) {
		return isColliding(s2, s1);
	}

	@Override
	public boolean isColliding(CircleShape s1, CircleShape s2) {
		double distCenters = s1.getCenter().dist2(s2.getCenter());
		double dif = s1.getRadius() + s2.getRadius();
		double distRadius = dif*dif;
		return distCenters <= distRadius;
	}

	@Override
	public boolean isColliding(CircleShape s1, Vec2d s2) {
		double dist = s1.getCenter().dist2(s2);
		return dist < (s1.getRadius() * s1.getRadius());
	}

	private Vec2d clamp(Vec2d value, Vec2d mins, Vec2d maxes){
		double xClamp = Math.max(mins.x, Math.min(maxes.x, value.x));
		double yClamp = Math.max(mins.y, Math.min(maxes.y, value.y));
		return new Vec2d(xClamp, yClamp);
	}
	
}
