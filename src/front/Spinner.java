package front;

import java.util.Observable;
import back.Time;
import processing.core.*;

public class Spinner extends Observable {

	private float mill = 0;
	private int prevSec, time;
	private Time tm, tmB;
	public Spinner() {
		prevSec = 0;
		tm = new Time();
		tmB = new Time();
		tmB.Count(1);
		tm.Count(6.6f);
	}

	public void show(PApplet app, boolean winner) {
		app.colorMode(PApplet.RGB, 255);
		app.rectMode(PApplet.CORNER);
		app.fill(10, 10, 10, 20);
		app.rect(0, 0, app.width, app.height);	
		app.smooth();
		app.noStroke();
		time = tm.getSeconds();
		// If this second (30) minus the previous second (29) is less than 1
		// than add one (milliseconds) or if seconds are 0
		if (time - prevSec < 1 || mill == 0) {
			// Adding 1 will count up to 60
			mill += 2;
		} else if (time - prevSec == 1) {
			// If we have reached the next second, restart the milliseconds and
			// make prevSec equal to the second
			mill = 0;
			prevSec = time;
		} else {
			mill += 2;
		}
		// If we have completed the second, reset millis timer
		if (mill >= 120)
			mill = 0;

		// If we have reached the end of the minute (It never reaches 60, after
		// 59 it goes straight to 0)
		// and we are at the start of the second, reset millis timer. We need to
		// do this manually because
		// our original if statement says if second - previous second. But
		// because our second is will be 0 and our
		// previous second will be 59 we will skip the animation of the first
		// second.
		if (time == 59 && mill == 0) {
			prevSec = 0;
		}
		app.pushMatrix();
		app.translate(app.width / 2, app.height / 2);
		app.fill(255);
		app.textSize(40);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text(tmB.getSeconds(), 0, 0);
		for (int i = 0; i < 360; i += 6) {
			float angle = 0; // sin(radians(i*3+frameCount)) * 30;
			float x = PApplet.sin(PApplet.radians(i));
			float y = PApplet.cos(PApplet.radians(i));
			app.fill(PApplet.map(i, 0, 360, 0, 255), PApplet.map(i, 0, 360, 255, 0), 0);
			/* Moving ellipse from center to posision */
			// if (a*3 == i) clockDot(x*(mill*3-angle), y*(mill*3-angle), 6, 30,
			// i);
			/* Show the ellipse if the second has passed */
			if (i < time * 6) {
				clockDot(app, x * (120 - angle), y * (120 - angle), 6, 30, i);
			}
		}

		finish();
		app.textSize(50);
		app.fill(200);
		if (!winner) {
			app.text("¡Juego terminado :( !" + "\n" + "Espera para volver a intentarlo", 0, -250);
		} else {
			app.text("¡Felicitaciones :D !" + "\n" + "Espera para volver a intentarlo", 0, -250);
		}
		app.popMatrix();
		

	}

	public void finish() {
		if (tmB.getSeconds() >= 10) {
			setChanged();
			notifyObservers();
			clearChanged();
		}
	}

	void clockDot(PApplet app, float x, float y, float w, float h, float angle) {
		app.pushMatrix();
		app.translate(x, y);
		app.rotate(PApplet.radians(-angle));
		app.rect(0, 0, w, h, w);
		app.popMatrix();
	}

}
