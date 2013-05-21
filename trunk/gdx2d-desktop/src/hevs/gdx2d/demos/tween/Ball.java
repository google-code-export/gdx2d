package hevs.gdx2d.demos.tween;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

/**
 * A simple ball object that will be drawn
 * @author Pierre-Andre Mudry (mui) 
 */
public class Ball implements DrawableObject{	
	float posx, posy;
	final static BitmapImage img = new BitmapImage("data/soccer.png");;
	
	public Ball(int x, int y){
		posx = x;
		posy = y;
	}
		
	@Override
	public void draw(GdxGraphics g) {
		g.drawPicture(posx, posy, img);
	}
}
