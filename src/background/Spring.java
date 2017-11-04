package background;

import processing.core.PApplet;
import processing.core.PVector;

public class Spring extends Thread{
	float maxForce;
	PVector pos;
	PVector vel;
	PVector acc;
	PVector target;

	// Seeks after a target and creates a spring effect.
	public Spring(float x, float y, float maxForce) {
		pos = new PVector(x, y);
		vel = new PVector(0, 0);
		acc = new PVector(0, 0);
		target = new PVector(x, y);
		this.maxForce = maxForce;
	}
	
	public void run() {	
		while (true) {
			try {
				move();
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private synchronized void move() {
		int distThreshold = 20;
		// Move towards the target.
		PVector push = new PVector(target.x, target.y);
		float distance = PApplet.dist(pos.x, pos.y, target.x, target.y);
		float force = PApplet.map(PApplet.min(distance, distThreshold), 0, distThreshold, 0, this.maxForce);
		push.sub(this.pos);
		push.normalize();
		push.mult(force);
		this.acc.add(push);

		// Add air-drag.
		this.vel.mult(1 - 0.2f);

		// Move it.
		this.vel.add(this.acc);
		this.pos.add(this.vel);
		this.acc.mult(0);
	}

	public PVector getPos() {
		return pos;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}
	

}
