package back;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import org.jbox2d.common.Vec2;
import TUIO.TuioObject;
import background.DisplayBackground;
import front.Bridge;
import front.Emotion;
import front.Firework;
import front.Form;
import front.Going;
import front.Instruction;
import front.Message;
import front.Particle;
import front.Spinner;
import principal.CONFIG;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;
import tuio.Reactivision;

public class DisplayBack implements Observer {

	// ---Variables
	protected Reactivision react;
	protected Bridge bridge;
	protected PApplet app;
	protected Box2DProcessing box2d;
	protected SoundController sc;
	protected ArrayList<Form> forms;
	protected Emotion emo;
	protected Video v;
	protected Spinner spin;
	protected boolean winner, once;
	protected int attempts;
	protected int state;
	protected int timer;
	protected Instruction inst;
	protected Going go;
	protected boolean practicelevel;
	protected Vec2 startPostionTemp;
	protected boolean insideSensibleArea;
	protected boolean players;
	protected ArrayList<Particle> particles = new ArrayList<Particle>();
	protected DisplayBackground dispb;
	protected int numbersChekPoint;
	protected boolean arrow;
	private int globalControl;
	protected Message ms;
	protected int numberMs;
	protected Firework[] fs;
	// --------------

	public DisplayBack(PApplet app, Reactivision react, Box2DProcessing box2d) {
		this.react = react;
		this.app = app;
		this.box2d = box2d;
		video();
	}

	private void video() {
		v = new Video(app, "tutorial");
		v.loop();
		players = false;
	}

	protected void startIntruction() {
		app.clear();
		spin = null;
		inst = new Instruction(app);
		inst.addObserver(this);
		state = CONFIG.state;
	}

	protected void startGame(int l) {
		app.clear();
		inst = null;
		winner = false;
		fs = new Firework[CONFIG.fireworks];
		deleteBridge();
		players = false;
		switch (l) {
		case 1:
			practicelevel = true;
			numbersChekPoint = 1;
			break;
		case 2:
			attempts = CONFIG.attempts;
			practicelevel = false;
			numbersChekPoint = 1;
			for (int i = 0; i < forms.size(); i++) {
				forms.get(i).killBody(box2d);
			}
			forms.clear();
			restarEmotion(false, new Vec2(startPostionTemp.x, startPostionTemp.y + 10));
			break;
		}
		startLevel(l);
	}

	protected void gameOver() {
		if (winner) {
			sc.getWin().trigger();
			createFireworks();
		} else {
			sc.getLost().trigger();
		}
		destroyGame();
		if (spin == null) {
			spin = new Spinner();
			spin.addObserver(this);
		}
	}

	protected void moveParticle() {
		float posXran = 0, posYran = 0;
		if (app.millis() - timer >= 3000) {
			timer = app.millis();
			posXran = randomWithRange(10, 1270);
			posYran = randomWithRange(10, 710);
		}
		particles.add(new Particle(posXran, posYran, app));
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).setControl(globalControl);
			if (particles.get(i).getLife() <= 0) {
				particles.remove(i);
			}
		}
	}

	protected boolean createBridge(int numPoints, int x, int y) {
		int lenght = numPoints * 10;
		if (bridge == null) {
			bridge = new Bridge(app, box2d, react, lenght, numPoints, x, y);
			return true;
		}
		return false;
	}

	private boolean bridgeCreated(Vec2[] v) {
		if (bridge == null) {
			for (int i = 0; i < react.getTuioClient().getTuioObjectList().size(); i++) {
				TuioObject tobj = react.getTuioClient().getTuioObjectList().get(i);
				int a = 250;
				if (v[0] != null && v[1] != null && v[2] != null) {
					if ((tobj.getSymbolID() == 1) && (validador(v[0].x, v[1].x, v[2].x))
							&& (go.checkPosition(v[0], v[1], v[2]))) {
						int point = 20;
						//tobj.getScreenX(app.width) - (point * 10 )-20
						return createBridge(point, 225, a);
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

	protected void showArrow() {
		for (int i = 0; i < forms.size(); i++) {
			Form f = forms.get(i);
			if (!f.getName().equals("finish"))
				f.setDisplay(arrow);
			else if (numbersChekPoint < 1)
				f.setDisplay(arrow);
		}
		go.setDisplayText(!arrow);
	}

	protected Vec2[] showBridge() {
		Vec2 v[] = allowBridge();
		if (bridgeCreated(v)) {
			if (v[0] != null && v[1] != null && v[2] != null) {
				if (go.checkPosition(v[0], v[1], v[2]) && !insideSensibleArea) {
					go.actionJoint();
					arrow = true;
					insideSensibleArea = true;
				} else if (!go.checkPosition(v[0], v[1], v[2])) {
					insideSensibleArea = false;
				}
			}
			// ---
			bridge.display(v);
		}
		return v;
	}

	protected boolean interactionForms(Form f) {
		if ((f.catchChekpoin(emo.getPos()))) {
			numbersChekPoint--;
			f.getSound().trigger();
			forms.remove(f);
			return true;
		}

		if (((f.finishLevel(box2d, emo.getPos()))) && numbersChekPoint < 1) {
			if (practicelevel) {
				f.getSound().trigger();
				startGame(2);
				return true;
			} else {
				state = 3;
				winner = true;
				gameOver();
			}
		}

		return false;
	}

	private float playersX = 0;

	/**
	 * pediente del mapeo para las posiciones porque puede varias dependiendo de
	 * la posicion de la camara y la distancia en la que esta la personas
	 * 
	 * @return
	 */

	private Vec2[] allowBridge() {
		Vec2 v[] = new Vec2[3];
		if (react.getTuioClient() != null) {
			float y = 0;
			float x = 0;
			for (int i = 0; i < react.getTuioClient().getTuioObjectList().size(); i++) {
				TuioObject tobj = react.getTuioClient().getTuioObjectList().get(i);
				if (tobj != null) {
					if (tobj.getSymbolID() == 1) {
						x = tobj.getScreenX(app.width);
						playersX = PApplet.map(x, 300, 1100, 0, 1280);
					}
					switch (tobj.getSymbolID()) {
					case 0:
						y = PApplet.map(tobj.getScreenY(app.height), CONFIG.maxDown, CONFIG.maxUp, 0, 720);
						v[0] = new Vec2(playersX - 150, y);
						break;
					case 1:
						v[1] = new Vec2(playersX, 360);
						break;
					case 2:
						y = PApplet.map(tobj.getScreenY(app.height), CONFIG.maxDown, CONFIG.maxUp, 0, 720);
						v[2] = new Vec2(playersX + 150, y);
						break;
					}
				}
			}
		}
		return v;
	}

	protected void showPeople(Vec2[] v) {
		app.colorMode(PApplet.HSB, 255);
		app.noStroke();
		for (int i = 0; i < v.length; i++) {
			if (v[i] != null) {
				switch (i) {
				case 0:
					app.fill(100, 255, 255);
					app.ellipse(v[i].x, v[i].y, 20, 20);
					break;
				case 1:
					app.fill(160, 255, 255);
					app.ellipse(v[i].x, v[i].y, 20, 20);
					break;
				case 2:
					app.fill(0, 255, 255);
					app.ellipse(v[i].x, v[i].y, 20, 20);
					break;
				}
			}
		}
	}

	protected void startLevel(int l) {
		if (ms == null) {
			ms = new Message(app);
		}
		messageState(l);
		makeObjects(l);
		// -----
		if (emo == null) {
			emo = new Emotion(app, box2d, sc.getPlayer(), startPostionTemp, 50, 50);
		}
		if (go == null) {
			go = new Going(app, box2d, startPostionTemp, 10);
		}
		// -----
		if (dispb == null) {
			dispb = new DisplayBackground(app, emo);
			dispb.start();
		}
		state = 2;
	}

	private void messageState(int l) {
		numberMs = l - 1;
		if (numberMs > 0) {
			ms.next();
		}
	}

	public void makeObjects(int l) {
		if (sc == null) {
			sc = new SoundController(app);
		}
		if (forms == null) {
			forms = new ArrayList<Form>();
		}
		// -------
		String[] data = app.loadStrings("data/levels/level" + l + ".txt");
		String[] names = getName(data);
		for (int i = 1; i < names.length; i++) {
			Form f = new Form(names[i]);
			switch (names[i]) {
			case "checkpoint":
				f.makeCircleBody(app, box2d, data[i]);
				f.setP(app.loadImage("data/arrow.png"));
				f.setSound(sc.getCheckpoint());
				break;
			case "obstacle":
				f.makeRectBody(box2d, data[i], new Vec2(100, 100), true);
				break;
			case "finish":
				f.makeRectBody(box2d, data[i], new Vec2(150, 50), true);
				f.setP(app.loadImage("data/arrow.png"));
				f.setSound(sc.getFinish());
				break;
			case "start":
				String[] temp = data[i].split(",");
				startPostionTemp = new Vec2(Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
				break;
			}
			forms.add(f);
		}
	}

	private String[] getName(String[] data) {
		String[] a = new String[data.length];
		for (int i = 1; i < data.length; i++) {
			String[] temp = data[i].split(",");
			a[i] = temp[0];
		}
		return a;
	}

	protected void restarEmotion(boolean statusGame, Vec2 pos) {
		emo.restartPosition(pos);
		if (statusGame) {
			attempts--;
			if (attempts < 1) {
				gameOver();
				winner = false;
			}
		}
		arrow = false;
	}

	protected int getRandom(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	protected void destroyGame() {
		deleteBridge();
		sc.stop();
		sc = null;
		emo.killBody(box2d);
		emo = null;
		go.killBody(box2d);
		go = null;
		ms.clear();
		ms = null;
		dispb.clear();
		if (forms != null) {
			for (int i = 0; i < forms.size(); i++) {
				forms.get(i).killBody(box2d);
			}
			forms.clear();
		}
		dispb = null;
		app.clear();
		state = 3;
	}

	private void deleteBridge() {
		if (bridge != null) {
			bridge.killBody(box2d);
			bridge = null;
		}
	}

	private boolean validador(float xUno, float xDos, float xTres) {
		if (xUno < xDos && xUno < xTres && xDos > xUno && xDos < xTres && xTres > xUno && xTres > xDos) {
			return true;
		}
		return false;
	}

	private void startVideo() {
		clearFireworks();
		spin = null;
		app.clear();
		state = 0;
		v.loop();
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (obs instanceof Spinner) {
			startVideo();
		}
		if (obs instanceof InstructionBack) {
			startGame(1);
		}
	}

	protected void ready() {
		if (playGame() && !players) {
			players = true;
			v.stop();
			startIntruction();
		}
	}

	protected boolean playGame() {
		Vec2[] v = allowBridge();
		if (v[0] != null && v[1] != null && v[2] != null) {
			return true;
		}
		return false;
	}

	// particulas
	public int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	protected int stateNumber;

	protected void changeStateBackgroundEffect(Emotion emo, int y) {
		if (emo.getPos().y < y / 3 && stateNumber != 0) {
			stateNumber = 0;
			for (int i = particles.size() - 1; i > -1; i--) {
				particles.get(i).setControl(stateNumber);
				;
			}
		}
		// --------
		if (emo.getPos().y > y / 3 && stateNumber != 1) {
			stateNumber = 1;
			for (int i = particles.size() - 1; i > -1; i--) {
				particles.get(i).setControl(stateNumber);
				;
			}
		}
		// --
		if (emo.getPos().y > (y / 3) * 2 && stateNumber != 2) {
			stateNumber = 2;
			for (int i = particles.size() - 1; i > -1; i--) {
				particles.get(i).setControl(stateNumber);
				;
			}
		}
	}

	protected void createFireworks() {
		for (int i = 0; i < fs.length; i++) {
			fs[i] = new Firework();
		}
	}

	protected void clearFireworks() {
		for (int i = 0; i < fs.length; i++) {
			fs[i] = null;
		}
	}

}
