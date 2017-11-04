package background;

import processing.core.PApplet;
import processing.core.PVector;

public class Pendulum {

	private PVector pos;
	private PVector vel;
	private PVector acc, parent;
	private float mass, baseHue;
	private float restLength;
	private float elasticity = 0.1f;

	// Attaches to another object and acts as a bouncy pendulum.
	public Pendulum(float x, float y, PVector parent) {
		pos = new PVector(x, y);
		setVel(new PVector(0, 0));
		acc = new PVector(0, 0);
		mass = 2;
		this.parent = parent;
		setBaseHue(0);
		restLength = PApplet.dist(pos.x, pos.y, parent.x, parent.y);
	}



	public synchronized void move() {
		float gravityPull = 2;
		// Push down with gravity.
		PVector gravity = new PVector(0, gravityPull);
		gravity.div(mass);
		acc.add(gravity);

		// Add air-drag.
		getVel().mult(1 - 0.2f);
		getVel().limit(5);

		// Move it.
		getVel().add(acc);
		pos.add(getVel());
		acc.mult(0);

		// Adjust its spring.
		float currentLength = pos.dist(parent);

		PVector spring = new PVector(pos.x, pos.y);
		spring.sub(parent);
		spring.normalize();

		float stretchLength = currentLength - restLength;
		spring.mult(-elasticity * stretchLength);
		spring.div(this.mass);
		acc.add(spring);
	}

	public void display(PApplet app) {
		if (parent != null) {
			app.strokeWeight(1);
			app.stroke(0, 360, 360);
			app.line(parent.x, parent.y, pos.x, pos.y);
		}

		app.strokeWeight(5);
		app.stroke(100, 360, 360);
		app.point(pos.x, pos.y);
	}

	public PVector getPos() {
		return pos;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public PVector getVel() {
		return vel;
	}

	public void setVel(PVector vel) {
		this.vel = vel;
	}

	public float getBaseHue() {
		return baseHue;
	}

	public void setBaseHue(float baseHue) {
		this.baseHue = baseHue;
	}

	public PVector getAcc() {
		return acc;
	}

	public void setAcc(PVector acc) {
		this.acc = acc;
	}

}
