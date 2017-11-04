package front;

import org.jbox2d.common.Vec2;

import back.GoingBack;
import principal.CONFIG;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class Going extends GoingBack {

	public Going(PApplet app, Box2DProcessing box2d, Vec2 pos, int numPoints) {
		super(app, box2d, pos, numPoints);
	}

	public void show(PApplet app) {
		showTie(app);
		createJoint();
		bridgePosition(app);
	}

	private void showTie(PApplet app) {
		app.pushMatrix();
		app.fill(255);
		for (int i = 0; i < particlesLeft.size(); i++) {
			Point p = particlesLeft.get(i);
			p.display();
		}
		for (int i = 0; i < particlesRight.size(); i++) {
			Point p = particlesRight.get(i);
			p.display();
		}
		if (table != null)
			table.display(app, box2d);
		app.popMatrix();
	}

	private boolean tam, a;
	private float sumTam, alpha = 140;

	private void bridgePosition(PApplet app) {
		app.pushMatrix();
		app.noFill();
		app.stroke(255);
		app.rectMode(PApplet.CORNER);
		app.rect(pos.x - sumTam, pos.y - sumTam, CONFIG.sensibleAreaW + sumTam * 2, CONFIG.sensibleAreaH + sumTam * 2);
		if (displayText)
			text(app, sumTam);
		app.popMatrix();
		if (tam) {
			sumTam += 0.2;
			if (sumTam > 10) {
				tam = !tam;
			}
		} else {
			sumTam -= 0.2;
			if (sumTam < 1) {
				tam = !tam;
			}
		}
	}

	private void text(PApplet app, float col) {
		app.textSize(25);
		app.fill(255);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text("Todos aquí para atrapar la emoción", pos.x + CONFIG.sensibleAreaW / 2,
				pos.y + CONFIG.sensibleAreaH / 4);
		app.noFill();
		showOrder(app);
		if (a) {
			alpha += 3;
			if (alpha > 220) {
				a = !a;
			}
		} else {
			alpha -= 3;
			if (alpha < 110) {
				a = !a;
			}
		}
	}

	private void showOrder(PApplet app) {
		app.pushMatrix();
		app.noStroke();
		app.ellipseMode(PApplet.CENTER);
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				app.fill(100, 255, 255, alpha);
				app.ellipse(pos.x + CONFIG.sensibleAreaW / 5 + (150 * i), pos.y + (CONFIG.sensibleAreaH / 4) * 3, 25,
						25);
				break;
			case 1:
				app.fill(150, 255, 255, alpha);
				app.ellipse(pos.x + CONFIG.sensibleAreaW / 5 + (150 * i), pos.y + (CONFIG.sensibleAreaH / 4) * 3, 25,
						25);
				break;
			case 2:
				app.fill(0, 255, 255, alpha);
				app.ellipse(pos.x + CONFIG.sensibleAreaW / 5 + (150 * i), pos.y + (CONFIG.sensibleAreaH / 4) * 3, 25,
						25);
				break;
			}

		}
		app.popMatrix();
	}

	public boolean checkPosition(Vec2 a, Vec2 b, Vec2 c) {
		boolean temp = false;
		if ((a.x > pos.x && a.x <= pos.x + CONFIG.sensibleAreaW && a.y > pos.y && a.y <= pos.y + CONFIG.sensibleAreaH)
				&& (b.x > pos.x && b.x <= pos.x + CONFIG.sensibleAreaW && b.y > pos.y
						&& b.y <= pos.y + CONFIG.sensibleAreaH)
				&& (c.x > pos.x && c.x <= pos.x + CONFIG.sensibleAreaW && c.y > pos.y
						&& c.y <= pos.y + CONFIG.sensibleAreaH)) {
			return true;
		}
		return temp;
	}
}
