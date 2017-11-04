package back;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ddf.minim.AudioSample;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import shiffman.box2d.Box2DProcessing;

public class FormBack {

	protected Vec2 size;
	protected Body body;
	protected String name = "";
	protected float r,a;
	protected PShape s;
	protected Vec2 posCheck;	
	protected float hue;
	protected PImage p;
	protected boolean display;
	private AudioSample sound;


	public FormBack(String name) {
		this.name = name;
	}
	
	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public void restartPosition(Box2DProcessing box2d, Vec2 start) {
		body.setTransform(new Vec2(box2d.coordPixelsToWorld(start.x, start.y - 100)), 0);
	}

	public void killBody(Box2DProcessing box2d) {
		if (body != null) {
			box2d.destroyBody(body);
		}
	}

	public boolean catchChekpoin(Vec2 posB) {
		if (posCheck != null) {
			if (PApplet.dist(posCheck.x, posCheck.y, posB.x, posB.y) < r*2 && name.equals("checkpoint")) {
				return true;
			}
		}
		return false;
	}

	public boolean finishLevel(Box2DProcessing box2d, Vec2 posB) {
		boolean temp = false;
		if (body != null) {
			Vec2 posA = box2d.getBodyPixelCoord(body);
			if (((posB.x >= posA.x - size.x / 2) && (posB.x <= posA.x + size.x / 2) && (posB.y <= posA.y + size.y / 2)
					&& (posB.y >= (posA.y - size.y / 2) - 50)) && (name.equals("finish"))) {				
				return true;
			}
		}
		return temp;
	}

	public void makeRectBody(Box2DProcessing box2d, String data, Vec2 s, boolean lock) {
		this.size = s;
		// Define a polygon (this is what we use for a rectangle)
		PolygonShape sd = new PolygonShape();
		float box2dW = box2d.scalarPixelsToWorld(s.x / 2);
		float box2dH = box2d.scalarPixelsToWorld(s.y / 2);
		sd.setAsBox(box2dW, box2dH);
		FixtureDef fd = new FixtureDef();
		fd.shape = sd;
		// Parameters that affect physics
		fd.density = 1;
		fd.friction = 0.3f;
		fd.restitution = 0.5f;
		// Define the body and make it from the shape
		BodyDef bd = new BodyDef();
		if (lock)
			bd.type = BodyType.STATIC;
		else
			bd.type = BodyType.DYNAMIC;

		bd.position.set(box2d.coordPixelsToWorld(readPos(data)));
		body = box2d.createBody(bd);
		body.createFixture(fd);
	}

	private Vec2 readPos(String st) {
		String[] pos = st.split(",");
		Vec2 v = new Vec2(Integer.valueOf(pos[1]), Integer.valueOf(pos[2]));
		return v;
	}

	private float getRad(String st) {
		String[] pos = st.split(",");
		float r = Float.valueOf(pos[3]);
		return r;
	}

	public void makeCircleBody(PApplet app, Box2DProcessing box2d, String data) {
		posCheck = readPos(data);
		r = getRad(data);
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public PImage getP() {
		return p;
	}

	public void setP(PImage p) {
		this.p = p;
	}

	public AudioSample getSound() {
		return sound;
	}

	public void setSound(AudioSample sound) {
		this.sound = sound;
	}

	public String getName() {
		return name;
	}

	public PShape getS() {
		return s;
	}

	public void setName(String name) {
		this.name = name;
	}

	


}
