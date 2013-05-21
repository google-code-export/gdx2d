package hevs.gdx2d.demos.scrolling.objects;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

/**
 * An animated coin that scales with time
 * @author Pierre-Andre Mudry (mui) 
 */
public class Coin implements DrawableObject {

	int posx, posy;	
	float scale = 0.4f, direction = 0.01f;	
	BitmapImage img;
	
	public Coin(int x, int y){
		posx = x;
		posy = y;
		img = new BitmapImage("data/retro-coin.png");
	}
	
	@Override
	public void draw(GdxGraphics g) {
		g.drawTransformedPicture(posx, posy, 0, scale, img);
		
		// If too small or too big, change direction of scaling
		if(scale < 0.2 || scale > 0.45)
			direction *= -1;
		
		scale += direction;
	}

}
