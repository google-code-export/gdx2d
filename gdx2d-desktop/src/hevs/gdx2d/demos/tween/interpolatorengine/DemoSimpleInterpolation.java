package hevs.gdx2d.demos.tween.interpolatorengine;

import hevs.gdx2d.demos.tween.Ball;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.PortableApplication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;

/**
 * Demonstrates the usage of i nterpolation (tweening) for animation using the
 * {@link Interpolation} class of libgdx.
 * 
 * @author Christopher Metrailler (mei)
 * @version 1.0
 * 
 */
public class DemoSimpleInterpolation extends PortableApplication {

	private final int DURATION = 4;

	private Ball ball1, ball2, ball3;
	private int height, width, margin;
	float currentTime;

	public DemoSimpleInterpolation(boolean onAndroid) {
		super(onAndroid);
	}

	@Override
	public void onInit() {
		setTitle("Simple interpolation, mei 2013");

		margin = Gdx.graphics.getWidth() / 10;
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		ball1 = new Ball(margin, height * 1 / 4);
		ball2 = new Ball(margin, height * 2 / 4);
		ball3 = new Ball(margin, height * 3 / 4);
	}

	@Override
	public void onGraphicRender(GdxGraphics g) {

		// Create an infinite "Yoyo effect"
		final float alpha = computeAlpha();

		// Apply different types of interpolation to the balls
		ball1.posx = Interpolation.linear.apply(margin, width - margin, alpha);
		ball2.posx = Interpolation.elastic.apply(margin, width - margin, alpha);
		ball3.posx = Interpolation.sine.apply(margin, width - margin, alpha);

		g.clear();

		ball1.draw(g);
		ball2.draw(g);
		ball3.draw(g);

		g.drawFPS();
		g.drawSchoolLogo();
	}

	private float computeAlpha() {

		float alpha = 0;
		currentTime += Gdx.graphics.getDeltaTime();

		// Divide the animation in 4 parts to add some delay
		if (currentTime < DURATION * 1 / 4)
			alpha = currentTime;
		else if (currentTime < DURATION * 2 / 4)
			alpha = 1;
		else if (currentTime < DURATION * 3 / 4)
			alpha = DURATION * 3 / 4 - currentTime;
		else if (currentTime < DURATION * 4 / 4)
			alpha = 0;
		else
			currentTime = 0;
		return alpha;
	}

	public static void main(String args[]) {
		new DemoSimpleInterpolation(false);
	}
}