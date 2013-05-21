package hevs.gdx2d.demos.music;

import hevs.gdx2d.components.audio.MusicPlayer;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.graphics.Color;

/**
 * Shows how to play music in the framework
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class DemoMusicPlay extends PortableApplication {

	MusicPlayer f;

	@Override
	public void onGraphicRender(GdxGraphics g) {
		// Clear the screen
		g.clear();

		// Audio logic
		if (f.isPlaying()) {
			g.setColor(Color.WHITE);
			g.drawString(20, getWindowHeight() / 2, "Playing song");
		} else {
			g.setColor(Color.WHITE);
			g.drawString(20, getWindowHeight() / 2, "Press to play");
		}

		// Draws the school logo
		g.drawSchoolLogo();
	}

	@Override
	public void onInit() {
		// The song we want to play
		f = new MusicPlayer("data/Blues-Loop.mp3");
	}

	@Override
	public void onClick(int x, int y, int button) {
		if (f.isPlaying())
			f.stop();
		else
			f.loop();
	}

	public DemoMusicPlay(boolean onAndroid) {
		super(onAndroid);
	}

	public static void main(String[] args) {
		new DemoMusicPlay(false);
	}
}
