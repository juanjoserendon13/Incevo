package back;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import front.Form;
import front.Point;
import principal.CONFIG;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class GoingBack {

	protected ArrayList<Point> particlesLeft;
	protected ArrayList<Point> particlesRight;
	protected Form table;
	private DistanceJoint jointB;
	private int numPoints;
	private boolean stateJoint = true;
	protected boolean displayText=true;
	public boolean isStateJoint() {
		return stateJoint;
	}

	public void setStateJoint(boolean stateJoint) {
		this.stateJoint = stateJoint;
	}	

	private Time tm;
	protected Box2DProcessing box2d;
	protected Vec2 pos;
	private DistanceJoint dj;

	public GoingBack(PApplet app, Box2DProcessing box2d, Vec2 pos, int numPoints) {
		this.box2d = box2d;
		this.pos = new Vec2(pos.x - CONFIG.sensibleAreaW / 2, pos.y + 200);
		particlesLeft = new ArrayList<Point>();
		particlesRight = new ArrayList<Point>();
		this.numPoints = numPoints;
		int distance = 50;
		int x = (int) pos.x;
		int y = (int) pos.y;
		tm = new Time();
		createTie(app, box2d, particlesLeft, x - distance, y, numPoints * 10, numPoints);
		createTie(app, box2d, particlesRight, x + distance, y, numPoints * 10, numPoints);
		createTable(app, box2d, x, y, numPoints, distance);
	}

	private void createTable(PApplet app, Box2DProcessing box2d, int x, int y, int numPoints, int distance) {
		table = new Form("table");
		table.makeRectBody(box2d, new String("table," + x + "," + (y + numPoints * 12)), new Vec2(distance * 2, 20),
				false);
		Point pA = particlesLeft.get(particlesLeft.size() - 1);
		Point pB = particlesRight.get(particlesRight.size() - 1);
		joint(box2d, pA, numPoints, -4);
		jointB = joint(box2d, pB, numPoints, 4);
	}

	private DistanceJoint joint(Box2DProcessing box2d, Point p, int numPoints, int bodyY) {
		DistanceJointDef djd = new DistanceJointDef();
		djd.bodyA = table.getBody();
		djd.bodyB = p.body;
		djd.localAnchorA.set(bodyY, 0);
		djd.length = box2d.scalarPixelsToWorld(numPoints);
		djd.frequencyHz = 0;
		djd.dampingRatio = 0;
		DistanceJoint djR = (DistanceJoint) box2d.world.createJoint(djd);
		return djR;
	}

	public void createTie(PApplet app, Box2DProcessing box2d, ArrayList<Point> particles, int x, int y, int totalLength,
			int numPoints) {
		float len = totalLength / numPoints;

		// Here is the real work, go through and add particles to the chain
		// itself
		for (int i = 0; i < numPoints + 1; i++) {
			// Make a new particle
			Point p = null;

			// First and last particles are made with density of zero
			if (i == 0 || i == numPoints)
				p = new Point(app, box2d, x, y + i * len, 4, true);
			else
				p = new Point(app, box2d, x, y + i * len, 4, false);
			particles.add(p);

			// Connect the particles with a distance joint
			if (i > 0) {
				DistanceJointDef djd = new DistanceJointDef();
				Point previous = particles.get(i - 1);
				// Connection between previous particle and this one
				djd.bodyA = previous.body;
				djd.bodyB = p.body;
				// Equilibrium length
				djd.length = box2d.scalarPixelsToWorld(len);
				// These properties affect how springy the joint is
				djd.frequencyHz = 0;
				djd.dampingRatio = 0;

				// Make the joint. Note we aren't storing a reference to the
				// joint ourselves anywhere!
				// We might need to someday, but for now it's ok
				// DistanceJoint dj = (DistanceJoint)
				 dj = (DistanceJoint) box2d.world.createJoint(djd);
			}
		}
	}

	private void destroyJoint() {
		box2d.world.destroyJoint(jointB);
	}
	public void killBody(Box2DProcessing box2d) {
		if (dj != null) {
			box2d.world.destroyJoint(dj);
		}
		clear();
	}
	private void clear(){
		for(Point p:particlesLeft){
			p.killBody();
		}		
		particlesLeft.clear();	
		for(Point p:particlesRight){
			p.killBody();
		}		
		particlesRight.clear();	
	}

	protected void createJoint() {
		if (tm.getSeconds() > 3 && !stateJoint) {
			Point pB = particlesRight.get(particlesRight.size() - 1);
			jointB = joint(box2d, pB, numPoints, 4);
			tm.setSeconds(0);
			tm.Stop();
			stateJoint = !stateJoint;
		}
	}

	public void actionJoint() {
		if (stateJoint) {
			destroyJoint();
			tm.Count(1);
			stateJoint = !stateJoint;
		}
	}

	public boolean isDisplayText() {
		return displayText;
	}

	public void setDisplayText(boolean displayText) {
		this.displayText = displayText;
	}
}
