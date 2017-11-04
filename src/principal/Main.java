package principal;

import org.jbox2d.dynamics.contacts.Contact;
import front.Display;
import processing.core.PApplet;
import processing.core.PFont;
import shiffman.box2d.Box2DProcessing;
import tuio.Reactivision;

public class Main extends PApplet {

	Display dp;
	Box2DProcessing box2d;
	PFont fon;

	public static void main(String[] args) {
		String[] appletArgs = new String[] { "principal.Main" };
		if (args != null) {
			PApplet.main(concat(appletArgs, args));
		} else {
			PApplet.main(appletArgs);
		}
	}

	public void settings() {
		size(CONFIG.width, CONFIG.height);
	}

	public void setup() {
		String[] args = { "VentanaReactivision" };
		fon = createFont("data/HelveticaLTStd-Roman.otf", 14);
		Reactivision react = new Reactivision();
		PApplet.runSketch(args, react);
		/// ---
		box2d = new Box2DProcessing(this);
		box2d.createWorld();
		box2d.listenForCollisions();
		/// ---
		dp = new Display(this, box2d, react);
		smooth();
	}

	public void draw() {
		textFont(fon);
		dp.show(this);
		box2d.step();		
	}

	public void keyPressed() {
		dp.key(this);
	}

	public void mousePressed() {
		dp.clic(this);	
	}


	public void beginContact(Contact cp) {
		dp.beginCon(cp);
	}

	public void endContact(Contact cp) {
	}

}
