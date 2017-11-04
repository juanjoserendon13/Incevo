package back;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import processing.core.PApplet;

public class SoundController {

	private Minim minim;
	private AudioSample player, checkpoint,finish, lost, win;
	private AudioPlayer soundBack; 

	
	public SoundController(PApplet app) {
		minim = new Minim(app);	
		loadSounds();
	}

	public void play() {
		soundBack.loop();		
	}

	private void clear() {
		player = null;
		soundBack = null;
		checkpoint = null;
		finish = null;
	}

	public void stop() {
		soundBack.pause();		
		clear();
	}

	private void loadSounds() {
		finish = minim.loadSample("data/sounds/spaceblast.aif");
		checkpoint = minim.loadSample("data/sounds/zapkick.aif");
		player = minim.loadSample("data/sounds/tabla.mp3");
		lost = minim.loadSample("data/sounds/lost.mp3");
		win = minim.loadSample("data/sounds/win.wav");
		soundBack = minim.loadFile("data/sounds/soundback.mp3");
		soundBack.loop();		
	}


	public AudioSample getPlayer() {
		return player;
	}

	public void setPlayer(AudioSample player) {
		this.player = player;
	}

	public AudioSample getCheckpoint() {
		return checkpoint;
	}

	public AudioSample getFinish() {
		return finish;
	}

	public void setCheckpoint(AudioSample checkpoint) {
		this.checkpoint = checkpoint;
	}

	public void setFinish(AudioSample finish) {
		this.finish = finish;
	}

	public AudioSample getLost() {
		return lost;
	}

	public AudioSample getWin() {
		return win;
	}

	public void setLost(AudioSample lost) {
		this.lost = lost;
	}

	public void setWin(AudioSample win) {
		this.win = win;
	}

}
