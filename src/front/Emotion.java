package front;

import org.jbox2d.common.Vec2;

import back.EmotionBack;
import ddf.minim.AudioSample;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class Emotion extends EmotionBack {

	public Emotion(PApplet app, Box2DProcessing box2d, AudioSample sound, Vec2 pos, float w, float h) {
		super(app, box2d, sound, pos, w, h);
	}

	public void show(PApplet app) {
		pos = box2d.getBodyPixelCoord(body);
		float a = body.getAngle();
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(-a);
		draw(app);
		app.popMatrix();
	}

	private void draw(PApplet app) {
		for (int i = 0; i < numEsf; i++) {
			app.noFill();
			posX = app.random(-vari, vari);
			posY = app.random(-vari, vari);
			app.stroke(col[i], 241, 187);
			app.strokeWeight(2);
			sz = 1;
			app.ellipse(posX + posXM, posY + posYM, w, w);
		}
	}
}
