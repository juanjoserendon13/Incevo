package front;

import org.jbox2d.common.Vec2;

import back.PointBack;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class Point extends PointBack {
	public Point(PApplet app, Box2DProcessing box2d, float x, float y, float r_, boolean fixed) {
		super(app, box2d, x, y, r_, fixed);
	}

	public void display() {
		// We look at each body and get its screen position
		Vec2 pos = box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(a);
		app.stroke(0);
		app.strokeWeight(1);
		app.ellipse(0, 0, r * 2, r * 2);
		// Let's add a line so we can see the rotation
		app.line(0, 0, r, 0);
		app.popMatrix();			
	}

	


}
