package hevs.gdx2d.demos.tween;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

/**
 * A simple ball object that will be drawn with tweening.
 * 
 * @author Pierre-André Mudry (mui)
 * @author Christopher Metrailler (mei)
 * @version 1.2
 */
public class Ball implements DrawableObject {
	public float posx, posy;
	final BitmapImage img = new BitmapImage("data/soccer.png");;

	public Ball(float x, float y) {
		posx = x;
		posy = y;
	}

	@Override
	public void draw(GdxGraphics g) {
		draw(g, 1.0f);
	}
		
	public void draw(GdxGraphics g, float scale){
		g.drawTransformedPicture(posx, posy, 0, scale, img);
	}
}
