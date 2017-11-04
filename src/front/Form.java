package front;

import org.jbox2d.common.Vec2;
import back.FormBack;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

public class Form extends FormBack {

	public Form(String name) {
		super(name);
	}

	public void show(PApplet app, Box2DProcessing box2d) {
		app.colorMode(PApplet.HSB, 255);
		app.stroke(app.color(hue, 255, 100));
		app.fill(app.color(hue, 255, 150));
		switch (name) {
		case "obstacle":
			showObstacle(app, box2d);
			break;
		case "checkpoint":
			showCircle(app, box2d);
			break;
		case "finish":
			display(app, box2d);

			break;
		}
	}

	public void showCircle(PApplet app, Box2DProcessing box2d) {
		app.ellipseMode(PApplet.CENTER);
		app.pushMatrix();
		app.translate(posCheck.x, posCheck.y);
		showCheckpoint(app);
		app.rotate(a);
		app.noFill();
		app.stroke(255);
		app.strokeWeight(2);
		app.ellipse(0, 0, r * 3, r * 3);
		app.noStroke();
		app.fill(app.color(hue, 255, 100));
		app.ellipse(0, 0, r * 2, r * 2);
		app.popMatrix();
		app.pushMatrix();
		app.translate(posCheck.x, posCheck.y - 100);
		if (display)
			showMessage(app, "Atrapar");
		app.popMatrix();
	}

	public void display(PApplet app, Box2DProcessing box2d) {
		// We look at each body and get its screen position
		Vec2 pos = box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		app.rectMode(PApplet.CENTER);
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(-a);
		app.stroke(255);
		app.noFill();
		app.rect(0, 0, size.x, size.y);
		app.fill(255);
		app.rect(0, 0, size.x - 20, size.y - 20);
		app.textSize(25);
		app.text("Meta", 0, size.y);
		if (name.equals("finish")) {
			if (display) {
				app.translate(0, -80);
				showMessage(app, "¡Llegada!");
			}	
		}
		app.popMatrix();
	}

	/// ------------------------------------------Elementos graficos HENDEL

	// obstaculo
	private int sa = 70;
	private int d = 10;
	private int num = 0;
	// checkpoint
	private int NUM = 24;

	public void showObstacle(PApplet app, Box2DProcessing box2d) {
		PVector[] pts = new PVector[3];
		Vec2 pos = box2d.getBodyPixelCoord(body);
		app.noStroke();
		// pintarSoportes(app, posX, posY, dir);
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		for (int i = 0; i < 3; i++) {
			pts[i] = new PVector(PApplet.sin(PApplet.radians(i * 120 + num)) * sa,
					PApplet.cos(PApplet.radians(i * 120 + num)) * sa);
		}
		for (int x = -sa; x <= sa; x += d) {
			for (int y = -sa; y <= sa; y += d) {
				if (crearTriangulo(new PVector(x, y), pts)) {
					app.fill(app.color(hue, 255, 100));
					app.ellipse(x, y, d / 1, d / 1);
				} else {
					app.fill(0, 0);
					app.ellipse(x, y, d / 1.5f, d / 1.5f);
				}
			}
		}
		num += 5;
		app.popMatrix();
	}

	private boolean crearTriangulo(PVector l, PVector[] pts) {
		PVector p1 = pts[0];
		PVector p2 = pts[1];
		PVector p3 = pts[2];
		float d = ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y));
		float a = ((p2.y - p3.y) * (l.x - p3.x) + (p3.x - p2.x) * (l.y - p3.y)) / d;
		float b = ((p3.y - p1.y) * (l.x - p3.x) + (p1.x - p3.x) * (l.y - p3.y)) / d;
		float c = 1 - a - b;
		return 0 <= a && a <= 1 && 0 <= b && b <= 1 && 0 <= c && c <= 1;
	}

	public void showCheckpoint(PApplet app) {
		app.pushMatrix();
		for (int i = 0; i < NUM; i++) {
			float angle = i * PApplet.TWO_PI / NUM;
			float v = PApplet.pow(PApplet.abs(PApplet.sin(angle / 2 + app.frameCount * 0.03f)), 4);
			float r = PApplet.map(v, 0, 1, 3, 5);
			app.fill(app.lerpColor(app.color(20, 150, 100), app.color(20 + 15, 150 + 10, 100 + 10), v));
			app.ellipse((50 + r) * PApplet.cos(angle), (50 + r) * PApplet.sin(angle), r * 2, r * 2);
		}
		app.popMatrix();
	}

	private float transpa = 255;
	private boolean fill;

	private void showMessage(PApplet app, String ms) {
		app.pushMatrix();
		if (p != null) {
			app.tint(255, transpa);
			app.imageMode(PApplet.CENTER);
			app.image(p, 0, 0, p.width / 2, p.height / 2);
			app.imageMode(PApplet.CORNER);
			app.noTint();
		}
		if (fill) {
			transpa += 5;
			if (transpa > 244) {
				fill = !fill;
			}
		} else {
			transpa -= 5;
			if (transpa < 60) {
				fill = !fill;
			}
		}
		app.fill(transpa);
		app.textSize(30);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text(ms, 0, -p.height / 2);
		app.popMatrix();
	}

	// ----Fin de la clase

}
