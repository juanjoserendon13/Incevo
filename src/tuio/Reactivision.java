package tuio;

import processing.core.PApplet;
import processing.core.PFont;
import java.util.ArrayList;
import TUIO.*;

public class Reactivision extends PApplet {

	TuioProcessing tuioClient;

	// these are some helper variables which are used
	// to create scalable graphical feedback
	float cursor_size = 15;
	float object_size = 60;
	float table_size = 760;
	float scale_factor = 1;
	PFont font;
	boolean verbose = false; // print console debug messages
	boolean callback = true; // updates only after callbacks

	public static void main(String[] args) {
		String[] appletArgs = new String[] { "tuio.Reactivision" };
		if (args != null) {
			PApplet.main(concat(appletArgs, args));
		} else {
			PApplet.main(appletArgs);
		}
	}

	public void settings() {
		//size(CONFIG.width, CONFIG.height);
	}

	public void setup() {

		fill(0);
		// // periodic updates
		// if (!callback) {
		// frameRate(60); //<>//
		// loop();
		// } else noLoop(); // or callback updates

		font = createFont("Arial", 18);
		scale_factor = height / table_size;

		// finally we create an instance of the TuioProcessing client
		// since we add "this" class as an argument the TuioProcessing class
		// expects
		// an implementation of the TUIO callback methods in this class (see
		// below)
		tuioClient = new TuioProcessing(this);
	}

	// within the draw method we retrieve an ArrayList of type <TuioObject>,
	// <TuioCursor> or <TuioBlob>
	// from the TuioProcessing client and then loops over all lists to draw the
	// graphical feedback.
	public void draw() {

		background(255);
		textFont(font, 18 * scale_factor);
		float obj_size = object_size * scale_factor;
		float cur_size = cursor_size * scale_factor;

		ArrayList<TuioObject> tuioObjectList = tuioClient.getTuioObjectList();
		for (int i = 0; i < tuioObjectList.size(); i++) {
			TuioObject tobj = tuioObjectList.get(i);
			stroke(0);
			fill(0, 0, 0);
			pushMatrix();
			translate(tobj.getScreenX(width), tobj.getScreenY(height));
			rotate(tobj.getAngle());
			rect(-obj_size / 2, -obj_size / 2, obj_size, obj_size);
			popMatrix();
			fill(255);
			text("" + tobj.getSymbolID(), tobj.getScreenX(width), tobj.getScreenY(height));
		}

		ArrayList<TuioCursor> tuioCursorList = tuioClient.getTuioCursorList();
		for (int i = 0; i < tuioCursorList.size(); i++) {
			TuioCursor tcur = tuioCursorList.get(i);
			ArrayList<TuioPoint> pointList = tcur.getPath();

			if (pointList.size() > 0) {
				stroke(0, 0, 255);
				TuioPoint start_point = pointList.get(0);
				for (int j = 0; j < pointList.size(); j++) {
					TuioPoint end_point = pointList.get(j);
					line(start_point.getScreenX(width), start_point.getScreenY(height), end_point.getScreenX(width),
							end_point.getScreenY(height));
					start_point = end_point;
				}

				stroke(192, 192, 192);
				fill(192, 192, 192);
				ellipse(tcur.getScreenX(width), tcur.getScreenY(height), cur_size, cur_size);
				fill(0);
				text("" + tcur.getCursorID(), tcur.getScreenX(width) - 5, tcur.getScreenY(height) + 5);
			}
		}

		ArrayList<TuioBlob> tuioBlobList = tuioClient.getTuioBlobList();
		for (int i = 0; i < tuioBlobList.size(); i++) {
			TuioBlob tblb = tuioBlobList.get(i);
			stroke(0);
			fill(0);
			pushMatrix();
			translate(tblb.getScreenX(width), tblb.getScreenY(height));
			rotate(tblb.getAngle());
			ellipse(-1 * tblb.getScreenWidth(width) / 2, -1 * tblb.getScreenHeight(height) / 2,
					tblb.getScreenWidth(width), tblb.getScreenWidth(width));
			popMatrix();
			fill(255);
			text("" + tblb.getBlobID(), tblb.getScreenX(width), tblb.getScreenX(width));
		}
		
		///---end draw
	}

	// --------------------------------------------------------------
	// these callback methods are called whenever a TUIO event occurs
	// there are three callbacks for add/set/del events for each
	// object/cursor/blob type
	// the final refresh callback marks the end of each TUIO frame

	// called when an object is added to the scene
	public void addTuioObject(TuioObject tobj) {
		if (verbose)
			println("add obj " + tobj.getSymbolID() + " (" + tobj.getSessionID() + ") " + tobj.getX() + " "
					+ tobj.getY() + " " + tobj.getAngle());
	}

	// called when an object is moved
	public void updateTuioObject(TuioObject tobj) {
		if (verbose)
			println("set obj " + tobj.getSymbolID() + " (" + tobj.getSessionID() + ") " + tobj.getX() + " "
					+ tobj.getY() + " " + tobj.getAngle() + " " + tobj.getMotionSpeed() + " " + tobj.getRotationSpeed()
					+ " " + tobj.getMotionAccel() + " " + tobj.getRotationAccel());
	}

	// called when an object is removed from the scene
	public void removeTuioObject(TuioObject tobj) {
		if (verbose)
			println("del obj " + tobj.getSymbolID() + " (" + tobj.getSessionID() + ")");
	}

	// --------------------------------------------------------------
	// called when a cursor is added to the scene
	public void addTuioCursor(TuioCursor tcur) {
		if (verbose)
			println("add cur " + tcur.getCursorID() + " (" + tcur.getSessionID() + ") " + tcur.getX() + " "
					+ tcur.getY());
		// redraw();
	}

	// called when a cursor is moved
	public void updateTuioCursor(TuioCursor tcur) {
		if (verbose)
			println("set cur " + tcur.getCursorID() + " (" + tcur.getSessionID() + ") " + tcur.getX() + " "
					+ tcur.getY() + " " + tcur.getMotionSpeed() + " " + tcur.getMotionAccel());
		// redraw();
	}

	// called when a cursor is removed from the scene
	public void removeTuioCursor(TuioCursor tcur) {
		if (verbose)
			println("del cur " + tcur.getCursorID() + " (" + tcur.getSessionID() + ")");
		// redraw()
	}

	// --------------------------------------------------------------
	// called when a blob is added to the scene
	public void addTuioBlob(TuioBlob tblb) {
		if (verbose)
			println("add blb " + tblb.getBlobID() + " (" + tblb.getSessionID() + ") " + tblb.getX() + " " + tblb.getY()
					+ " " + tblb.getAngle() + " " + tblb.getWidth() + " " + tblb.getHeight() + " " + tblb.getArea());
		// redraw();
	}

	// called when a blob is moved
	public void updateTuioBlob(TuioBlob tblb) {
		if (verbose)
			println("set blb " + tblb.getBlobID() + " (" + tblb.getSessionID() + ") " + tblb.getX() + " " + tblb.getY()
					+ " " + tblb.getAngle() + " " + tblb.getWidth() + " " + tblb.getHeight() + " " + tblb.getArea()
					+ " " + tblb.getMotionSpeed() + " " + tblb.getRotationSpeed() + " " + tblb.getMotionAccel() + " "
					+ tblb.getRotationAccel());
		// redraw()
	}

	// called when a blob is removed from the scene
	public void removeTuioBlob(TuioBlob tblb) {
		if (verbose)
			println("del blb " + tblb.getBlobID() + " (" + tblb.getSessionID() + ")");
		// redraw()
	}

	// --------------------------------------------------------------
	// called at the end of each TUIO frame
	public void refresh(TuioTime frameTime) {
		if (verbose)
			println("frame #" + frameTime.getFrameID() + " (" + frameTime.getTotalMilliseconds() + ")");
		if (callback)
			redraw();
	}

	public TuioProcessing getTuioClient() {
		return tuioClient;
	}

	public void setTuioClient(TuioProcessing tuioClient) {
		this.tuioClient = tuioClient;
	}

}
