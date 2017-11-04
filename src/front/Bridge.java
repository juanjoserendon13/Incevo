package front;

import org.jbox2d.common.Vec2;

import back.BridgeBack;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;
import tuio.Reactivision;

public class Bridge extends BridgeBack {

	public Bridge(PApplet app, Box2DProcessing box2d, Reactivision react, float l, int n, int x, int y) {
		super(app, box2d, react, l, n, x, y);
	}

	// Draw the bridge
	public void display(Vec2 []v) {
		for (Point p : particles) {
			app.fill(255);
			p.display();
		}
		move(v);
	}
}
