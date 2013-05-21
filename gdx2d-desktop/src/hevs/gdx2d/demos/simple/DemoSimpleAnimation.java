package hevs.gdx2d.demos.simple;

import com.badlogic.gdx.graphics.Color;

import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

/**
 * A very simple demonstration on how to 
 * display something animated with the library
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class DemoSimpleAnimation extends PortableApplication {

	int radius = 5, speed = 1;
	
	public DemoSimpleAnimation(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {
		setTitle("Simple demo, mui 2013");
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {		
		
		// Clears the screen
		g.clear();
		g.drawFilledCircle(g.getScreenWidth()/2, g.getScreenHeight()/2, radius, Color.BLUE);		

		// Update the circle radius
		if (radius >= 50|| radius <= 3)
			speed *= -1;

		radius += speed;
		
		g.drawSchoolLogo();
	}

	public static void main(String[] args) {
		new DemoSimpleAnimation(false);
	}

}
