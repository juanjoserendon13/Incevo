package front;

import processing.core.*;

public class Particle {

	private float life = 1;
	private float lifeRate;
	private float angle;
	private float hue;
	private float maxScale;
	// float maxScale = max(0.05, abs(cos(radians(frameCount*5))*1.5));
	private float rotateRate;
	private float maxOffset;
	private PVector pos;
	// boolean inverse = false;
	private int control;

	public Particle(float x, float y, PApplet app) {
		pos = new PVector(x, y);
		hue = 0;
		lifeRate = app.random(0.005f, 0.02f);
		angle = PApplet.map(PApplet.cos(PApplet.radians(app.frameCount * 5)), -1, 1, -180, 180);
		maxScale = PApplet.max(0.25f, PApplet.abs(PApplet.sin(PApplet.radians(app.frameCount * 5)) * 1.5f));
		rotateRate = app.random(-200, 200);
		maxOffset = app.random(50, 300);
	}

	public void pintar(PApplet app) {
		app.colorMode(PApplet.HSB, 255);
		float offset = PApplet.map(life, 1, 0, 0, maxOffset);
		// Scales from particle's origin pivot.
		float s;
		switch (control) {
		case 0:
			s = PApplet.map(life, 1, 0, 0, maxScale);
			break;
		case 1:
			s = PApplet.map(life, 1, 0, maxScale, 0);
			break;
		case 2:
			s = app.noise(PApplet.map(life, 1, 0, 10, 0));
			break;
		default:
			s = PApplet.map(life, 1, 0, 0, maxScale);
			break;
		}
		float t = PApplet.map(life, 1, 0, 0, 1);
		float opacity = PApplet.map(life, 1, 0, 255, 0);
		app.strokeWeight(5);		
		app.stroke(app.color(hue, 255, 100, (float) (opacity * 0.5)));
		app.fill(app.color(hue, 255, 150, (float) (opacity * 0.8)));		
		app.pushMatrix();		
		app.translate(pos.x, pos.y);
		app.rotate(PApplet.radians(angle + t * rotateRate));
		app.scale(s);
		app.ellipse(offset, 0, 20, 20);
		app.popMatrix();
		life -= lifeRate;
		app.colorMode(PApplet.RGB, 255);
		app.noStroke();
	}

	public float getLife() {
		return life;
	}

	public void setControl(int cont) {
		control = cont;
	}

	public void setHue(float h) {
		hue = h;
	}
}
