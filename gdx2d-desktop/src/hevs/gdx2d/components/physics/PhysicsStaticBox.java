package hevs.gdx2d.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape.Type;

/**
 * A physical box that does not move, see {@link AbstractPhysicsObject}
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class PhysicsStaticBox extends AbstractPhysicsObject {
	
	public PhysicsStaticBox(String name, Vector2 position, int width, int height) {
		super(Type.Polygon, name, position, width, height, 0, 0, 0.5f, false);
	}
	
	public PhysicsStaticBox(String name, Vector2 position, int width, int height, float angle) {
		super(Type.Polygon, name, position, width, height, angle, 0, 0, 0.5f, false);
	}
}
