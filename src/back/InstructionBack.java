package back;

import java.util.Observable;

import principal.CONFIG;
import processing.core.PApplet;
import processing.core.PImage;

public class InstructionBack extends Observable {

	protected Time tim;
	protected PImage img;

	public InstructionBack(PApplet app) {
		tim = new Time();
		tim.Count(1);
		img = app.loadImage("data/context_ui.png");
	}

	protected boolean endTime() {
		if (tim.getSeconds() > CONFIG.instructionTime) {
			setChanged();
			notifyObservers();
			clearChanged();
			return true;
		}
		return false;
	}
}
