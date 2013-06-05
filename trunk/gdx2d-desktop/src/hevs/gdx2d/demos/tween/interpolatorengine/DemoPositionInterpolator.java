package hevs.gdx2d.demos.tween.interpolatorengine;

import hevs.gdx2d.demos.tween.Ball;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

/**
 * Demonstrates the usage of interpolation (tweening) for animation using the
 * {@link Interpolation} class of libgdx.
 * 
 * @author Christopher Metrailler (mei)
 * @author Pierre-André Mudry (mui)
 * @version 1.1
 * 
 */
public class DemoPositionInterpolator extends PortableApplication {

	private Ball ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8;
	private int height, width, margin;

	public DemoPositionInterpolator(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {
		setTitle("Position interpolators, mei 2013");

		margin = Gdx.graphics.getWidth() / 8;
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		ball1 = new Ball(margin, height * 1 / 10f);
		ball2 = new Ball(margin, height * 2 / 10f);
		ball3 = new Ball(margin, height * 3 / 10f);
		ball4 = new Ball(margin, height * 4 / 10f);
		ball5 = new Ball(margin, height * 5 / 10f);
		ball6 = new Ball(margin, height * 6 / 10f);
		ball7 = new Ball(margin, height * 7 / 10f);
		ball8 = new Ball(margin, height * 8 / 10f);
	}
	
	@Override
	public void onGraphicRender(GdxGraphics g) {
		// Create an infinite "Yoyo effect"
		final float animationPercentage = computePercentage();
		
		// Apply different types of interpolation to the balls between start position and
		// end position of the X attribute of the ball
		final float start = margin;
		final float end = width - margin;
		
		ball1.posx = Interpolation.linear.apply(start, end, animationPercentage);
		ball2.posx = Interpolation.elastic.apply(start, end, animationPercentage);
		ball3.posx = Interpolation.sine.apply(start, end, animationPercentage);
		ball4.posx = Interpolation.bounce.apply(start, end, animationPercentage);
		ball5.posx = Interpolation.circle.apply(start, end, animationPercentage);
		ball6.posx = Interpolation.swing.apply(start, end, animationPercentage);
		ball7.posx = Interpolation.pow2.apply(start, end, animationPercentage);
		ball8.posx = Interpolation.exp10.apply(start, end, animationPercentage);

		// Do the drawing
		g.clear();

		// Draw the two red boundaries
		g.setColor(Color.RED);
		g.drawLine(margin, height * 1 / 10f, margin, height * 8 / 10f);
		g.drawLine(width - margin, height * 1 / 10f, width - margin, height * 8 / 10f);
		
		// Draw the balls
		ball1.draw(g, 0.5f);
		ball2.draw(g, 0.5f);
		ball3.draw(g, 0.5f);
		ball4.draw(g, 0.5f);
		ball5.draw(g, 0.5f);
		ball6.draw(g, 0.5f);
		ball7.draw(g, 0.5f);
		ball8.draw(g, 0.5f);

		g.drawFPS();
		g.drawSchoolLogoUpperRight();
	}

	@Override
	public void onDispose() {	
		super.onDispose();		
	}
		
	final float ANIMATION_LENGTH = 1.3f; // Animation length (in seconds)
	float currentTime = 0f; // In seconds
	int direction = 1; // Direction of movement
	
	private float computePercentage()
	{
		if(direction == 1)
			currentTime += Gdx.graphics.getDeltaTime();
		else
			currentTime -= Gdx.graphics.getDeltaTime();
		
		if(currentTime >= ANIMATION_LENGTH || currentTime <= 0)
			direction *= -1;
				
		return currentTime / ANIMATION_LENGTH;
	}
	
	public static void main(String args[]) {
		new DemoPositionInterpolator(false);
	}
}