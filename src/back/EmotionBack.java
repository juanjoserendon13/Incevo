package back;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ddf.minim.AudioSample;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class EmotionBack {
	protected float w, h;
	protected Box2DProcessing box2d;
	protected Body body;
	protected AudioSample sound;
	protected Vec2 pos;		
	protected int f, numEsf = 10, vari = 10;
	protected float sz, posX , posY, posXM, posYM;
	protected int col[] = new int[numEsf];

	public EmotionBack(PApplet app,Box2DProcessing box2d, AudioSample sound, Vec2 pos, float w, float h) {
		this.pos = pos;
		this.w = w;
		this.h = h;
		this.box2d = box2d;
		this.sound = sound;
		createBody();
		
		for (int i = 0; i < numEsf; i++) {
			col[i] = (int) app.random(360);
		}
	}

	private void createBody() {
		// ---- posicionamiento de objeto en coordenadas de box
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		
		bd.position.set(box2d.coordPixelsToWorld(pos.x, pos.y));

		// Creacion del body
		body = box2d.createBody(bd);

		// --Creacion del shape
		CircleShape cir = new CircleShape();
		cir.m_radius = box2d.scalarPixelsToWorld(w / 2);

		// ----- Creacion de FIxture
		FixtureDef fd = new FixtureDef();
		fd.shape = cir;

		/// --caracteristicas
		fd.density = 0.7f;
		fd.friction = 0.3f;
		fd.restitution = 0.1f;

		// --Unir forma al cuerpo con sus caracteristicas
		body.createFixture(fd);
	}
	
	public void killBody(Box2DProcessing box2d) {
		if (body != null) {
			box2d.destroyBody(body);
		}
	}

	public void restartPosition(Vec2 start) {
		body.setTransform(new Vec2(box2d.coordPixelsToWorld(start.x, start.y+60)), 0);
	}

	public void soundPlayer() {
		sound.trigger();
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public void setW(float w) {
		this.w = w;
	}

	public void setH(float h) {
		this.h = h;
	}

	public Vec2 getPos() {
		return pos;
	}

	public void setPos(Vec2 pos) {
		body.setTransform(new Vec2(box2d.coordPixelsToWorld(pos.x, pos.y)), 0);
	}

}
