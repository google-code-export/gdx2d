package hevs.gdx2d.demos.physics.rocket;

import com.badlogic.gdx.math.Vector2;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.physics.PhysicsBox;
import hevs.gdx2d.lib.GdxGraphics;
import hevs.gdx2d.lib.interfaces.DrawableObject;

public class Spaceship implements DrawableObject {
	// The box that contains the simulatable body
	PhysicsBox box = null;
	
	// Motor related
	boolean thrustLeft = false, thrustRight = false;
	int thrustUp = 0;
	
	// Drawing related
	final static BitmapImage image = new BitmapImage("data/rocket_128.png");
	final float RAD2DEG = 57.2957795f;

	public Spaceship(Vector2 position) {
		box = new PhysicsBox("ship", position, 30, 30, 0.61f, 0.2f, 0.2f);
		box.body.setAngularDamping(0.2f);
		box.body.setLinearDamping(0.2f);
	}

	@Override
	public void draw(GdxGraphics g) {
		Vector2 leftT = box.body.getPosition();
		leftT.add(-5, -5);
		Vector2 rightT = box.body.getPosition();
		rightT.add(5, 5);

		if (thrustLeft)
			box.body.applyTorque(1200000, true);
		if (thrustRight)
			box.body.applyTorque(-1200000, true);

		box.body.applyForceToCenter((float) Math.cos(box.body.getAngle()) * thrustUp,
				(float) Math.sin(box.body.getAngle()) * thrustUp, true);

		g.drawTransformedPicture((int) box.body.getPosition().x,
				(int) box.body.getPosition().y,
				(float) box.body.getAngle() * RAD2DEG, .5f, image);
	}

}
