package front;

import back.InstructionBack;
import processing.core.PApplet;

public class Instruction extends InstructionBack {
	public Instruction(PApplet app) {
		super(app);
	}

	public void show(PApplet app) {		
		if (!endTime()) {
			app.background(0);
			if(img!=null)app.image(img, 0, 0);
			showTime(app);			
		}		
	}

	private void showTime(PApplet app) {
		app.fill(255);
		app.textSize(25);		
		app.text(tim.getSeconds(), app.width - 150, app.height - 150);
	}

}
