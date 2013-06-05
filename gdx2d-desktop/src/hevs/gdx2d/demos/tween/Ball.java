package hevs.gdx2d.demos.tween;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

/**
 * A simple ball object that will be drawn with tweening.
 * 
 * @author Pierre-André Mudry (mui)
 * @author Christopher Metrailler (mei)
 * @version 1.1
 */
public class Ball implements DrawableObject {
	public float posx, posy;
	final static BitmapImage img = new BitmapImage("data/soccer.png");;

	public Ball(int x, int y) {
		posx = x;
		posy = y;
	}

	@Override
	public void draw(GdxGraphics g) {
		g.drawPicture(posx, posy, img);
	}
}
