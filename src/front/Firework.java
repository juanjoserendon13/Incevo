package front;

import processing.core.PApplet;

public class Firework {	
	private float x, y, oldX, oldY, ySpeed, targetX, targetY, explodeTimer, flareWeight, flareAngle;
	private int flareAmount, duration;
	private boolean launched, exploded, hidden;
	private int flare;

	public Firework() {		
		launched = false;
		exploded = false;
		hidden = true;

	}

	void pintar(PApplet app) {
		app.smooth();
		if ((launched) && (!exploded) && (!hidden)) {
			launchMaths();
			app.strokeWeight(3);
			app.stroke(255);
			app.line(x, y, oldX, oldY);
		}
		if ((!launched) && (exploded) && (!hidden)) {
			explodeMaths();
			app.noStroke();
			app.strokeWeight(flareWeight);
			app.stroke(flare);
			for (int i = 0; i < flareAmount + 1; i++) {
				app.pushMatrix();
				app.translate(x, y);
				app.point(PApplet.sin(PApplet.radians(i * flareAngle)) * explodeTimer,
						PApplet.cos(PApplet.radians(i * flareAngle)) * explodeTimer);
				app.popMatrix();
			}
		}
		if ((!launched) && (!exploded) && (hidden)) {
			// do nothing
		}
		app.noStroke();

	}

	public void launch(PApplet app) {
		// REPLACING THE MOUSE
		float posX = app.random(10, app.width-10);
		float posY = app.random(10, app.height-10);

		x = oldX = posX + ((app.random(5) * 10) - 25);
		y = oldY = app.height;
		targetX = posX;
		targetY = posY;
		ySpeed = app.random(3) + 2;
		flare = app.color(app.random(3) * 50 + 105, app.random(3) * 50 + 105, app.random(3) * 50 + 105);
		flareAmount = PApplet.ceil(app.random(30)) + 20;
		flareWeight = PApplet.ceil(app.random(3));
		duration = PApplet.ceil(app.random(4)) * 20 + 30;
		flareAngle = 360 / flareAmount;
		launched = true;
		exploded = false;
		hidden = false;
	}

	public void launchMaths() {
		oldX = x;
		oldY = y;
		if (PApplet.dist(x, y, targetX, targetY) > 6) {
			x += (targetX - x) / 2;
			y += -ySpeed;
		} else {
			explode();
		}
	}

	public void explode() {
		explodeTimer = 0;
		launched = false;
		exploded = true;
		hidden = false;
	}

	public void explodeMaths() {
		if (explodeTimer < duration) {
			explodeTimer += 0.4;
		} else {
			hide();
		}
	}

	public void hide() {
		launched = false;
		exploded = false;
		hidden = true;
	}

	public boolean isHidden() {
		return hidden;
	}

}
