package front;

import back.Time;
import processing.core.PApplet;
import processing.core.PImage;

public class Message {

	private PImage img[];
	private Time t;
	private boolean display;

	public Message(PApplet app) {
		display = true;
		t = new Time();
		t.Count(1);
		img = new PImage[2];
		img[0] = app.loadImage("data/prueba.jpg");
		img[1] = app.loadImage("data/intentos.jpg");
	}

	public void show(PApplet app, int n) {
		app.pushMatrix();	
		if (display) {
			app.image(img[n], 0, 0);
		}
		if (t.getSeconds() > 3 && display) {
			display = false;
		}
		app.popMatrix();
	}

	public void next() {
		t.setSeconds(0);
		display = true;
	}

	public void clear() {
		t = null;
		img = null;
	}

}
