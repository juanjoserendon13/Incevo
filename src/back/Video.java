package back;

import processing.core.PApplet;
import processing.video.Movie;

public class Video {

	private Movie movie;

	public Video(PApplet app, String v) {
		movie = new Movie(app, "data/" + v + ".mp4");
	}

	public void loop() {
		movie.loop();
	}

	public void show(PApplet app) {
		if (movie.available())
			movie.read();
		app.image(movie, 0, 0);
	}

	public void stop() {
		movie.stop();
	}
}
