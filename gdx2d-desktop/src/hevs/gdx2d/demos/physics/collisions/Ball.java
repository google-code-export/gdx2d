package hevs.gdx2d.demos.physics.collisions;

import hevs.gdx2d.components.physics.AbstractPhysicsObject;
import hevs.gdx2d.components.physics.PhysicsCircle;

import com.badlogic.gdx.math.Vector2;

/**
 * Demonstrates how to implements collisions detection with box2d
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class Ball extends PhysicsCircle {

	public Ball(String name, Vector2 position, int radius) {
		super(name, position, radius);
	}

	public Ball(String name, Vector2 position, int radius, float density, float restitution, float friction) {
		super(name, position, radius, density, restitution, friction);
	}
	
	/**
	 * Called for every collision
	 */
	@Override
	public void collision(AbstractPhysicsObject a, AbstractPhysicsObject b, float energy) {
		if(energy > 10000)
			System.out.println(a.name + " collided " + b.name + " with energy " + energy);
	} 
}
