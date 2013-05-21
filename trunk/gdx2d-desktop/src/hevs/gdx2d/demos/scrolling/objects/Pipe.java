package hevs.gdx2d.demos.scrolling.objects;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

/**
 * A simple pipe that does nothing special
 * @author Pierre-Andre Mudry (mui)
 */

public class Pipe implements DrawableObject {

	int posx, posy;
	BitmapImage img;
	
	public Pipe(int x, int y){
		posx = x;
		posy = y;
		img = new BitmapImage("data/pipe.png");
	}
	
	@Override
	public void draw(GdxGraphics g) {
		g.drawPicture(posx, posy, img);
	}

}
