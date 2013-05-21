package hevs.gdx2d.components.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape.Type;

/**
 * A physical shape which collides as a box, see {@link AbstractPhysicsObject}
 * @author Pierre-André Mudry (mui)
 * @version 1.0 
 */
public class PhysicsBox extends AbstractPhysicsObject {
	
	public PhysicsBox(String name, Vector2 position, int width, int height) {		
		super(Type.Polygon, name, position, width, height, 1.0f, 0.3f, 0.3f, true);		
	}
	
	public PhysicsBox(String name, Vector2 position, int width, int height, float angle) {		
		super(Type.Polygon, name, position, width, height, 1.0f, 0.3f, 0.3f, angle, true);
	}
		
	public PhysicsBox(String name, Vector2 position, int width, int height, float density, float restitution, float friction) {
		super(Type.Polygon, name, position, width, height, density, restitution, friction, true);
	}	
	
}
