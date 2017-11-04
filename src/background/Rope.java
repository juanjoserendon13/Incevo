package background;

import java.util.ArrayList;

import processing.core.PApplet;

public class Rope extends Thread {

	Spring spg;
	ArrayList<Pendulum> pends;
	private boolean debugMode = false;

	// Uses a series of dynamic objects but displays them as a single line.
	public Rope(float x, float y, float count) {
		pends = new ArrayList<>();
		spg = new Spring(x, y, 1);
		spg.start();
		for (int i = 0; i < count; i++) {
			if (pends.isEmpty()) {
				Pendulum p = new Pendulum(x, y + i * 15, spg.getPos());
			//	p.start();
				pends.add(p);
			} else {
				Pendulum p = new Pendulum(x, y + i * 15, pends.get(i - 1).getPos());
		//		p.start();
				pends.add(p);
			}
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				move();
				Thread.sleep(24);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private synchronized void move() {	
		for (int i = 0; i < pends.size(); i++) {
			Pendulum p = pends.get(i);	
			p.move();
			if (i == 0) {
				continue;
			}			
			if (!debugMode) {		
				float mag = p.getVel().mag();			
				p.setBaseHue(p.getBaseHue() + mag * 0.05f);
				if (p.getBaseHue() > 360) {
					p.setBaseHue(p.getBaseHue() - 360);
				}				
			}
		}
	}

	public synchronized void display(PApplet app) {
		app.colorMode(PApplet.HSB, 255);
		for (int i = 0; i < pends.size(); i++) {
			Pendulum p = pends.get(i);
			if (i == 0) {
				continue;
			}
			if (debugMode) {
				p.display(app);
			} else {
				float mag = p.getVel().mag();
				// Velocity drives alpha and size.
				app.stroke(p.getBaseHue(), 255, 255, mag * 15);
				app.strokeWeight(mag * 1.5f);
				app.point(p.getPos().x, p.getPos().y);
			}
		}
	}

	public ArrayList<Pendulum> getPends() {
		return pends;
	}

	public void setPends(ArrayList<Pendulum> pends) {
		this.pends = pends;
	}
}
